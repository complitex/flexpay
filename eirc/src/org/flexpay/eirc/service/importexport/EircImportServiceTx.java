package org.flexpay.eirc.service.importexport;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.importexport.ImportService;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.persistence.workflow.TransitionNotAllowed;
import org.flexpay.eirc.service.ConsumerService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional (readOnly = true)
public class EircImportServiceTx extends ImportService {

	private ConsumerService consumerService;
	private RegistryRecordWorkflowManager recordWorkflowManager;

	@Transactional (readOnly = false)
	public boolean processBatch(long[] counters, boolean inited,
								DataSourceDescription sd, RawDataSource<RawConsumerData> dataSource,
								Map<String, List<Street>> nameObjsMap, Map<String, StreetType> nameTypeMap) {

		log.debug("Fetching for next batch");

		// get next butch
		List<RawConsumerData> datum = readRawDataBatch(inited, dataSource);
		if (datum.isEmpty()) {
			return false;
		}

		log.debug("Fetched next batch");

		for (RawConsumerData data : datum) {
			++counters[0];

			if (data == null) {
				log.info("Empty data read");
				++counters[1];
				continue;
			}

			try {
				if (log.isDebugEnabled()) {
					log.debug("Starting record processing: " + data.getRegistryRecord());
				}
				recordWorkflowManager.startProcessing(data.getRegistryRecord());
			} catch (TransitionNotAllowed e) {
				if (log.isInfoEnabled()) {
					log.info("Skipping record, processing not allowed: " + data.getExternalSourceId());
				}
				continue;
			}

			if (data.getRegistryRecord().getConsumer() != null) {
				log.info("Record already has consumer set up");
				continue;
			}

			try {
				Stub<Consumer> persistentObj = correctionsService.findCorrection(
						data.getShortConsumerId(), Consumer.class, sd);

				if (persistentObj != null) {
					log.info("Found existing consumer correction: #" + data.getExternalSourceId());
					// todo do we need to fetch this?
					postSaveRecord(data, consumerService.read(persistentObj));
					continue;
				}

				if (data.isPersonalInfoEmpty()) {
					log.info("Cannot find consumer by short info: " + data.getShortConsumerId());
					ImportError error = addImportError(sd, data.getExternalSourceId(), Consumer.class, dataSource);
					error.setErrorId("error.eirc.import.consumer_not_found");
					setConsumerError(data, error);
					continue;
				}


				// Find apartment
				Apartment apartment = findApartment(nameObjsMap, nameTypeMap, sd, data, dataSource);
				if (apartment == null) {
					addImportError(sd, data.getExternalSourceId(), Apartment.class, dataSource);
					continue;
				}

				data.getRegistryRecord().setApartment(apartment);
				if (log.isInfoEnabled()) {
					log.info("Found apartment: " + data.getApartmentId());
				}

				Person person = findPerson(data);
				if (person == null) {
					log.error("Person not found: " + data.getPersonFIO());
					ImportError error = addImportError(sd, data.getExternalSourceId(), Person.class, dataSource);
					error.setErrorId("error.eirc.import.person_not_found");
					setConsumerError(data, error);
					continue;
				}
				data.getRegistryRecord().setPerson(person);
				log.info("Found responsible person: " + data.getPersonFIO());

				Service service = data.getRegistryRecord().getService();
				if (service == null) {
					service = consumerService.findService(
							data.getRegistryRecord().getSpRegistry().getServiceProvider(), data.getServiceCode());
					if (service == null) {
						log.warn("Unknown service code: " + data.getServiceCode());
						ImportError error = addImportError(sd, data.getExternalSourceId(), Service.class, dataSource);
						error.setErrorId("error.eirc.import.consumer_not_found");
						setConsumerError(data, error);
						continue;
					}

					data.getRegistryRecord().setService(service);
				}


				postSaveRecord(data, null);
			} catch (Exception e) {
				log.error("Failed getting consumer: " + data.toString(), e);
				throw new RuntimeException(e);
			}
		}

		flushStack();

		if (log.isDebugEnabled()) {
			log.debug("Imported " + counters[0] + " records so far.");
		}

		return true;
	}

	private List<RawConsumerData> readRawDataBatch(boolean inited, RawDataSource<RawConsumerData> dataSource) {

		log.debug("Reading batch");

		if (!inited) {
			dataSource.initialize();

			log.debug("Inited");
		}


		List<RawConsumerData> result = dataSource.nextPage();
		log.debug("Listing records for update");

		return result;
	}

	private void postSaveRecord(RawConsumerData data, Consumer consumer) {
		data.getRegistryRecord().setConsumer(consumer);
		addToStack(data.getRegistryRecord());
	}

	private void setConsumerError(RawConsumerData data, ImportError error) throws Exception {
		recordWorkflowManager.setNextErrorStatus(data.getRegistryRecord(), error);
	}

	@Nullable
	private Person findPerson(@NotNull RawConsumerData data) {

		List<Person> persons = personService.findRegisteredPersons(
				stub(data.getRegistryRecord().getApartment()));
		if (persons.isEmpty()) {
			return null;
		}

		if (persons.size() == 1) {
			return persons.get(0);
		}

		// try to filter persons by FIO
		List<Person> candidates = CollectionUtils.list();
		for (Person person : persons) {
			for (PersonIdentity identity : person.getPersonIdentities()) {
				boolean sameFIO = new EqualsBuilder()
						.append(identity.getFirstName(), data.getFirstName())
						.append(identity.getMiddleName(), data.getMiddleName())
						.append(identity.getLastName(), data.getLastName())
						.isEquals();
				if (sameFIO) {
					candidates.add(person);
					break;
				}
			}
		}

		if (candidates.size() == 1) {
			return candidates.get(0);
		}

		return null;
	}

	private Apartment findApartment(Map<String, List<Street>> nameObjsMap,
									Map<String, StreetType> nameTypeMap,
									DataSourceDescription sd, RawConsumerData data,
									RawDataSource<RawConsumerData> dataSource) throws Exception {

		// try to find by apartment correction
		if (log.isInfoEnabled()) {
			log.info("Checking for apartment correction: " + data.getApartmentId());
		}
		Stub<Apartment> apartmentById = correctionsService.findCorrection(
				data.getApartmentId(), Apartment.class, sd);
		if (apartmentById != null) {
			log.info("Found apartment correction");
			return new Apartment(apartmentById);
		}

		// try to find building and later apartment in it
		Stub<Buildings> buildingsById = correctionsService.findCorrection(
				data.getBuildingId(), Buildings.class, sd);
		if (buildingsById != null) {
			log.info("Found buildings correction: " + data.getBuildingId());
			Buildings buildings = buildingService.readFull(buildingsById);
			return findApartment(data, buildings, sd, dataSource);
		}

		// try to find street by correction
		Stub<Street> streetById = correctionsService.findCorrection(
				data.getStreetId(), Street.class, sd);
		if (streetById != null) {
			log.info("Found street correction: " + data.getStreetId());
			return findApartment(data, new Street(streetById), sd, dataSource);
		}

		Street streetByName = findStreet(nameObjsMap, nameTypeMap, data, sd, dataSource);
		if (streetByName == null) {
			log.warn("Failed getting street for account #" + data.getExternalSourceId());
			return null;
		}

		DataCorrection corr = correctionsService.getStub(
				data.getStreetId(), streetByName, sd);
		log.info("Adding street correction: " + data.getStreetId());
		addToStack(corr);

		return findApartment(data, streetByName, sd, dataSource);
	}

	private Street findStreet(Map<String, List<Street>> nameObjsMap,
							  Map<String, StreetType> nameTypeMap, RawConsumerData data,
							  DataSourceDescription sd, RawDataSource<RawConsumerData> dataSource) throws Exception {
		List<Street> streets = nameObjsMap.get(data.getAddressStreet().toLowerCase());
		// no candidates
		if (streets == null) {
			log.info("No candidates for street: " + data.getAddressStreet());
			ImportError error = addImportError(sd, data.getExternalSourceId(), Street.class, dataSource);
			error.setErrorId("error.eirc.import.street_not_found");
			setConsumerError(data, error);
			return null;
		}

		if (log.isDebugEnabled()) {
			log.debug("Street candidates: " + streets);
		}

		StreetType streetType = findStreetType(nameTypeMap, data);
		if (streetType == null) {
			log.warn("Found several streets, but no type was found: " + data.getAddressStreetType() +
					 ", " + data.getAddressStreet());
			ImportError error = addImportError(sd, data.getExternalSourceId(), StreetType.class, dataSource);
			error.setErrorId("error.eirc.import.street_type_not_found");
			setConsumerError(data, error);
			return null;
		}

		List<Street> filteredStreets = filterStreetsbyType(streets, streetType);
		if (filteredStreets.isEmpty()) {
			log.warn("Cannot find street by type: " + data.getAddressStreetType() +
					 ", " + data.getAddressStreet());
			ImportError error = addImportError(sd, data.getExternalSourceId(), Street.class, dataSource);
			error.setErrorId("error.eirc.import.street_type_invalid");
			setConsumerError(data, error);
			return null;
		}

		if (filteredStreets.size() == 1) {
			log.info("Street filtered by type: " + data.getAddressStreet());
			return filteredStreets.get(0);
		}

		log.warn("Cannot find street candidate, even by type: " + data.getAddressStreetType() +
				 ", " + data.getAddressStreet());
		ImportError error = addImportError(sd, data.getExternalSourceId(), Street.class, dataSource);
		error.setErrorId("error.eirc.import.street_too_many_variants");
		setConsumerError(data, error);
		return null;
	}

	private List<Street> filterStreetsbyType(List<Street> streets, StreetType type) {
		List<Street> filteredStreets = new ArrayList<Street>();
		for (Street street : streets) {
			StreetType current = street.getCurrentType();
			if (current == null) {
				log.warn("No type for street " + street);
				continue;
			}
			if (stub(current).getId().equals(type.getId())) {
				filteredStreets.add(street);
			}
		}

		return filteredStreets;
	}

	private StreetType findStreetType(Map<String, StreetType> nameTypeMap, RawConsumerData data) {

		Stub<StreetType> typeById = correctionsService.findCorrection(
				data.getAddressStreetType(), StreetType.class, null);
		if (typeById != null) {
			return new StreetType(typeById);
		}

		return nameTypeMap.get(data.getAddressStreetType().toLowerCase());
	}

	private Apartment findApartment(RawConsumerData data, Street street, DataSourceDescription sd, RawDataSource<RawConsumerData> dataSource)
			throws Exception {
		Buildings buildings = buildingService.findBuildings(stub(street), data.getAddressHouse(), data.getAddressBulk());
		if (buildings == null) {
			log.warn(String.format("Failed getting building for consumer, Street(%d, %s), Building(%s, %s) ",
					street.getId(), data.getAddressStreet(), data.getAddressHouse(), data.getAddressBulk()));
			ImportError error = addImportError(sd, data.getExternalSourceId(), Buildings.class, dataSource);
			error.setErrorId("error.eirc.import.building_not_found");
			setConsumerError(data, error);
			return null;
		}

		DataCorrection corr = correctionsService.getStub(data.getBuildingId(), buildings, sd);
		log.info("Adding buildings correction: " + data.getBuildingId());
		addToStack(corr);


		return findApartment(data, buildings, sd, dataSource);
	}

	private Apartment findApartment(RawConsumerData data, Buildings buildings, DataSourceDescription sd, RawDataSource<RawConsumerData> dataSource)
			throws Exception {
		Building building = buildings.getBuilding();

		Stub<Apartment> stub = apartmentService.findApartmentStub(building, data.getAddressApartment());
		if (stub == null) {
			ImportError error = addImportError(sd, data.getExternalSourceId(), Apartment.class, dataSource);
			error.setErrorId("error.eirc.import.apartment_not_found");
			setConsumerError(data, error);
			return null;
		}

		// Add correction to ease further search 
		Apartment apartment = new Apartment(stub);
		DataCorrection corr = correctionsService.getStub(data.getApartmentId(), apartment, sd);
		log.info("Adding apartment correction: " + data.getApartmentId());
		addToStack(corr);

		return new Apartment(stub);
	}

	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}
}
