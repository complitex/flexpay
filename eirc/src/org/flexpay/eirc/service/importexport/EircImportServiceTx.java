package org.flexpay.eirc.service.importexport;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.importexport.ImportService;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional (readOnly = true)
public class EircImportServiceTx extends ImportService {

	private ConsumerService consumerService;
	private ImportUtil importUtil;
	private RegistryRecordWorkflowManager recordWorkflowManager;

	@Transactional (readOnly = false)
	public boolean processBatch(long[] counters, boolean inited,
								DataSourceDescription sd, RawDataSource<RawConsumerData> dataSource,
								Map<String, List<Street>> nameObjsMap) {

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
				log.debug("Starting record processing: {}", data.getRegistryRecord());
				recordWorkflowManager.startProcessing(data.getRegistryRecord());
			} catch (TransitionNotAllowed e) {
				log.info("Skipping record, processing not allowed: {}", data.getExternalSourceId());
				continue;
			}

			RegistryRecord record = data.getRegistryRecord();
			EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
			if (props.getConsumer() != null) {
				log.info("Record already has consumer set up");
				continue;
			}

			if (props.getApartment() != null &&
				props.getPerson() != null &&
				props.getService() != null) {
				log.info("Record already have apartment, person and service set");
				continue;
			}

			try {
				Stub<Consumer> persistentObj = correctionsService.findCorrection(
						data.getShortConsumerId(), Consumer.class, sd);

				if (persistentObj != null) {
					log.info("Found existing consumer correction: #{}", data.getExternalSourceId());
					// todo do we need to fetch this?
					postSaveRecord(data, consumerService.read(persistentObj));
					continue;
				}

				if (data.isPersonalInfoEmpty()) {
					log.info("Cannot find consumer by short info: {}", data.getShortConsumerId());
					ImportError error = addImportError(sd, data.getExternalSourceId(), Consumer.class, dataSource);
					error.setErrorId("error.eirc.import.consumer_not_found");
					setConsumerError(data, error);
					continue;
				}

				// set apartment
				if (props.getApartment() == null) {
					// Find apartment
					Apartment apartment = findApartment(nameObjsMap, sd, data, dataSource);
					if (apartment == null) {
						addImportError(sd, data.getExternalSourceId(), Apartment.class, dataSource);
						continue;
					}

					props.setApartment(apartment);
					log.info("Found apartment: {}", data.getApartmentId());
				}

				// set person
				if (props.getPerson() == null) {
					Person person = findPerson(sd, data, dataSource);
					if (person != null) {
						props.setPerson(person);
						log.info("Found responsible person: {}", data.getPersonFIO());
					} else {
						log.info("No person found");
					}
				}

				// set service if not found
				EircRegistryProperties regProps = (EircRegistryProperties) data.getRegistryRecord().getRegistry().getProperties();
				Service service = props.getService();
				Stub<ServiceProvider> spStub = new Stub<ServiceProvider>(regProps.getServiceProvider());
				if (service == null) {
					service = consumerService.findService(spStub, data.getServiceCode());
					if (service == null) {
						log.warn("Unknown service code: {}", data.getServiceCode());
						ImportError error = addImportError(sd, data.getExternalSourceId(), Service.class, dataSource);
						error.setErrorId("error.eirc.import.service_not_found");
						setConsumerError(data, error);
						continue;
					}

					props.setService(service);
				}

				// try to find consumer (correction lost or service code came in a different format?)
				Consumer consumer = consumerService.findConsumer(spStub, data.getAccountNumber(), data.getServiceCode());
				if (consumer != null) {
					// consumer found save correction
					DataCorrection corr = correctionsService.getStub(data.getShortConsumerId(), consumer, sd);
					addToStack(corr);
				}

				postSaveRecord(data, consumer);
			} catch (Exception e) {
				log.error("Failed getting consumer: " + data.toString(), e);
				throw new RuntimeException(e);
			}
		}

		flushStack();

		log.debug("Imported {} records so far", counters[0]);

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
		EircRegistryRecordProperties props = (EircRegistryRecordProperties) data.getRegistryRecord().getProperties();
		props.setConsumer(consumer);
		addToStack(data.getRegistryRecord());
	}

	private void setConsumerError(RawConsumerData data, ImportError error) throws Exception {
		recordWorkflowManager.setNextErrorStatus(data.getRegistryRecord(), error);
	}

	@Nullable
	private Person findPerson(DataSourceDescription sd, @NotNull RawConsumerData data,
							  RawDataSource<RawConsumerData> dataSource) throws Exception {

		// first try to find person via set correction
		Stub<Person> personById = correctionsService.findCorrection(
				data.getPersonFIOId(), Person.class, sd);
		if (personById != null) {
			log.info("Found person correction");
			return new Person(personById);
		}

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) data.getRegistryRecord().getProperties();
		ImportError error = addImportError(sd, data.getExternalSourceId(), Person.class, dataSource);
		Person person = importUtil.findPersonByFIO(props.getApartmentStub(),
				data.getFirstName(), data.getMiddleName(), data.getLastName(), error);
		if (person != null) {
			return person;
		}

		if (error.getErrorId() != null) {
//			setConsumerError(data, error);
		}

		return null;
	}

	private Apartment findApartment(Map<String, List<Street>> nameObjsMap,
									DataSourceDescription sd, RawConsumerData data,
									RawDataSource<RawConsumerData> dataSource) throws Exception {

		// try to find by apartment correction
		log.info("Checking for apartment correction: {}", data.getApartmentId());
		Stub<Apartment> apartmentById = correctionsService.findCorrection(
				data.getApartmentId(), Apartment.class, sd);
		if (apartmentById != null) {
			log.info("Found apartment correction");
			return new Apartment(apartmentById);
		}

		// try to find building and later apartment in it
		Stub<BuildingAddress> buildingsById = correctionsService.findCorrection(
				data.getBuildingId(), BuildingAddress.class, sd);
		if (buildingsById != null) {
			log.info("Found buildingAddress correction: {}", data.getBuildingId());
			BuildingAddress buildingAddress = buildingService.readFull(buildingsById);
			return findApartment(data, buildingAddress, sd, dataSource);
		}

		// try to find street by correction
		Stub<Street> streetById = correctionsService.findCorrection(
				data.getStreetId(), Street.class, sd);
		if (streetById != null) {
			log.info("Found street correction: {}", data.getStreetId());
			return findApartment(data, new Street(streetById), sd, dataSource);
		}

		Street streetByName = findStreet(nameObjsMap, data, sd, dataSource);
		if (streetByName == null) {
			log.warn("Failed getting street for account #{}", data.getExternalSourceId());
			return null;
		}

		DataCorrection corr = correctionsService.getStub(data.getStreetId(), streetByName, sd);
		log.info("Adding street correction: {}", data.getStreetId());
		addToStack(corr);

		return findApartment(data, streetByName, sd, dataSource);
	}

	private Street findStreet(Map<String, List<Street>> nameObjsMap,
							  RawConsumerData data,
							  DataSourceDescription sd, RawDataSource<RawConsumerData> dataSource) throws Exception {

		StreetType streetType = findStreetType(data, sd);
		if (streetType == null) {
			log.warn("No street type was found: {}, for street {}", data.getAddressStreetType(), data.getAddressStreet());
			ImportError error = addImportError(sd, data.getExternalSourceId(), StreetType.class, dataSource);
			error.setErrorId("error.eirc.import.street_type_not_found");
			setConsumerError(data, error);
			return null;
		}

		List<Street> streets = nameObjsMap.get(data.getAddressStreet().toLowerCase());
		// no candidates
		if (streets == null) {
			log.info("No candidates for street: {}", data.getAddressStreet());
			ImportError error = addImportError(sd, data.getExternalSourceId(), Street.class, dataSource);
			error.setErrorId("error.eirc.import.street_not_found");
			setConsumerError(data, error);
			return null;
		}

		log.debug("Street candidates: {}", streets);

		List<Street> filteredStreets = filterStreetsbyType(streets, streetType);
		if (filteredStreets.isEmpty()) {
			log.warn("Cannot find street by type: {}, {}", data.getAddressStreetType(), data.getAddressStreet());
			ImportError error = addImportError(sd, data.getExternalSourceId(), Street.class, dataSource);
			error.setErrorId("error.eirc.import.street_type_invalid");
			setConsumerError(data, error);
			return null;
		}

		if (filteredStreets.size() == 1) {
			log.info("Street filtered by type: {}", data.getAddressStreet());
			return filteredStreets.get(0);
		}

		log.warn("Cannot find street candidate, even by type: {}, {}",
				data.getAddressStreetType(), data.getAddressStreet());
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
				log.warn("No type for street {}", street);
				continue;
			}
			if (stub(current).getId().equals(type.getId())) {
				filteredStreets.add(street);
			}
		}

		return filteredStreets;
	}

	@Nullable
	private StreetType findStreetType(RawConsumerData data, DataSourceDescription sd) throws Exception {

		StreetType type = streetTypeService.findTypeByName(data.getAddressStreetType());
		if (type != null) {
			return type;
		}

		Stub<StreetType> stub = correctionsService.findCorrection(data.getAddressStreetType(),
				StreetType.class, sd);
		return stub != null ? new StreetType(stub.getId()) : null;
	}

	private Apartment findApartment(RawConsumerData data, Street street, DataSourceDescription sd, RawDataSource<RawConsumerData> dataSource)
			throws Exception {
		BuildingAddress buildingAddress = buildingService.findBuildings(stub(street), data.getAddressHouse(), data.getAddressBulk());
		if (buildingAddress == null) {
			log.warn("Failed getting building for consumer, Street({}, {}), Building({}, {}) ",
					new Object[]{street.getId(), data.getAddressStreet(), data.getAddressHouse(), data.getAddressBulk()});
			ImportError error = addImportError(sd, data.getExternalSourceId(), BuildingAddress.class, dataSource);
			error.setErrorId("error.eirc.import.building_not_found");
			setConsumerError(data, error);
			return null;
		}

		DataCorrection corr = correctionsService.getStub(data.getBuildingId(), buildingAddress, sd);
		log.info("Adding buildingAddress correction: {}", data.getBuildingId());
		addToStack(corr);

		return findApartment(data, buildingAddress, sd, dataSource);
	}

	private Apartment findApartment(RawConsumerData data, BuildingAddress buildingAddress, DataSourceDescription sd, RawDataSource<RawConsumerData> dataSource)
			throws Exception {
		Building building = buildingAddress.getBuilding();

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
		log.info("Adding apartment correction: {}", data.getApartmentId());
		addToStack(corr);

		return new Apartment(stub);
	}

	@Required
	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	@Required
	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}

	@Required
	public void setImportUtil(ImportUtil importUtil) {
		this.importUtil = importUtil;
	}
}
