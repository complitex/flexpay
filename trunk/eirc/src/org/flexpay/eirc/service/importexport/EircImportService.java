package org.flexpay.eirc.service.importexport;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.importexport.ImportService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.persistence.workflow.TransitionNotAllowed;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class EircImportService extends ImportService {

	private RawConsumerDataConverter consumerDataConverter;
	private ConsumerService consumerService;
	private IdentityTypeService typeService;

	private RegistryRecordWorkflowManager recordWorkflowManager;

	@Transactional(readOnly = false)
	public void importConsumers(DataSourceDescription sd, RawDataSource<RawConsumerData> dataSource)
			throws FlexPayException {

		if (log.isInfoEnabled()) {
			log.info("Starting importing consumers for data source: " + sd.getId());
		}

		Town defaultTown = ApplicationConfig.getInstance().getDefaultTown();
		ArrayStack filters = new ArrayStack();
		filters.push(new TownFilter(defaultTown.getId()));
		List<Street> townStreets = streetService.find(filters);

		Map<String, List<Street>> nameObjsMap = initializeNamesToObjectsMap(townStreets);
		Map<String, StreetType> nameTypeMap = initializeTypeNamesToObjectsMap();

		if (log.isInfoEnabled()) {
			log.info("Streets number: " + nameObjsMap.keySet().size());
			log.info("Street types: " + nameTypeMap.keySet());
		}

		dataSource.initialize();
		while (dataSource.hasNext()) {

			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawConsumerData data = dataSource.next(typeHolder);

			if (data == null) {
				log.info("Empty data read");
				continue;
			}

			try {
				if (log.isDebugEnabled()) {
					log.info("Starting record processing: " + data.getRegistryRecord());
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
				Consumer persistentObj = correctionsService.findCorrection(
						data.getShortConsumerId(), Consumer.class, sd);

				if (persistentObj != null) {
					log.info("Found existing consumer correction: #" + data.getExternalSourceId());
					postSaveConsumer(data, consumerService.read(persistentObj));
					continue;
				}

				if (data.isPersonalInfoEmpty()) {
					log.info("Cannot find consumer by short info");
					ImportError error = addImportError(sd, data.getExternalSourceId(), Consumer.class, dataSource);
					error.setErrorId("error.eirc.import.consumer_not_found");
					setConsumerError(data, error);
					continue;
				}

				Consumer rawConsumer = consumerDataConverter.fromRawData(data, sd, correctionsService);

				if (rawConsumer.getApartment() == null) {
					// Find apartment
					Apartment apartment = findApartment(nameObjsMap, nameTypeMap, sd, data, dataSource);
					if (apartment == null) {
						addImportError(sd, data.getExternalSourceId(), Apartment.class, dataSource);
						continue;
					}
					DataCorrection corr = correctionsService.getStub(
							data.getApartmentId(), apartment, sd);
					log.info("Adding apartment correction: " + data.getApartmentId());
					addToStack(corr);
					rawConsumer.setApartment(apartment);
				}
				data.getRegistryRecord().setApartment(rawConsumer.getApartment());
				log.info("Found apartment: " + data.getApartmentId());

				if (rawConsumer.getResponsiblePerson() == null) {
					Person person = findPerson(data);
					if (person == null) {
						log.error("Person not found: " + data.getPersonCorrectionId());
						ImportError error = addImportError(sd, data.getExternalSourceId(), Person.class, dataSource);
						error.setErrorId("error.eirc.import.person_not_found");
						setConsumerError(data, error);
						continue;
					}
					// found person by name
					DataCorrection corr = correctionsService.getStub(
							data.getPersonCorrectionId(), person, sd);
					addToStack(corr);
					rawConsumer.setResponsiblePerson(person);
				}
				data.getRegistryRecord().setPerson(rawConsumer.getResponsiblePerson());
				log.info("Found responsible person: " + data.getPersonCorrectionId());

				if (rawConsumer.getService() == null) {
					log.error("Not found provider #" + data.getRegistry().getSenderCode() +
							" service by code: " + data.getRegistryRecord().getServiceCode());
					ImportError error = addImportError(sd, data.getExternalSourceId(), Service.class, dataSource);
					error.setErrorId("error.eirc.import.service_not_found");
					setConsumerError(data, error);
					continue;
				}
				log.info("Found service");

				Consumer persistent = consumerService.findConsumer(rawConsumer);
				if (persistent != null) {
					// Consumer found, add new correction
					if (log.isInfoEnabled()) {
						log.info("Creating new consumer correction: " + data.getFullConsumerId());
					}

					addToStack(correctionsService.getStub(data.getFullConsumerId(), persistent, sd));
					addToStack(correctionsService.getStub(data.getShortConsumerId(), persistent, sd));
				} else {
					log.info("Consumer not found");
				}

				postSaveConsumer(data, persistent);
			} catch (Exception e) {
				log.error("Failed getting consumer: " + data.toString(), e);
				throw new RuntimeException(e);
			}
		}

		flushStack();
	}

	private void postSaveConsumer(RawConsumerData data, Consumer consumer) {
		data.getRegistryRecord().setConsumer(consumer);
		addToStack(data.getRegistryRecord());
	}

	private void setConsumerError(RawConsumerData data, ImportError error) throws Exception {
		recordWorkflowManager.setNextErrorStatus(data.getRegistryRecord(), error);
	}

	private Person findPerson(RawConsumerData data) {

		PersonIdentity identity = new PersonIdentity();
		identity.setFirstName(data.getFirstName());
		identity.setMiddleName(data.getMiddleName());
		identity.setLastName(data.getLastName());
		identity.setDefault(true);
		identity.setIdentityType(typeService.getType(IdentityType.TYPE_NAME_PASSPORT));

		Person example = new Person();
		Set<PersonIdentity> identities = new HashSet<PersonIdentity>();
		identities.add(identity);
		example.setPersonIdentities(identities);

		return personService.findPersonStub(example);
	}

	private Map<String, StreetType> initializeTypeNamesToObjectsMap() {
		Map<String, StreetType> nameTypeMap = new HashMap<String, StreetType>();
		for (StreetType type : streetTypeService.getEntities()) {
			for (StreetTypeTranslation translation : type.getTranslations()) {
				nameTypeMap.put(translation.getName().toLowerCase(), type);
			}
		}

		return nameTypeMap;
	}

	private Apartment findApartment(Map<String, List<Street>> nameObjsMap,
									Map<String, StreetType> nameTypeMap,
									DataSourceDescription sd, RawConsumerData data,
									RawDataSource<RawConsumerData> dataSource) throws Exception {

		// try to find by apartment correction
		log.info("Checking for apartment correction: " + data.getApartmentId());
		Apartment apartmentById = correctionsService.findCorrection(
				data.getApartmentId(), Apartment.class, sd);
		if (apartmentById != null) {
			log.info("Found apartment correction");
			return apartmentById;
		}

		Buildings buildingsById = correctionsService.findCorrection(
				data.getBuildingId(), Buildings.class, sd);
		if (buildingsById != null) {
			log.info("Found buildings correction");
			return findApartment(data, buildingsById, sd, dataSource);
		}

		Street streetById = correctionsService.findCorrection(
				data.getStreetId(), Street.class, sd);
		if (streetById != null) {
			log.info("Found street correction");
			return findApartment(data, streetById, sd, dataSource);
		}

		Street streetByName = findStreet(nameObjsMap, nameTypeMap, data, sd, dataSource);
		if (streetByName == null) {
			log.warn("Failed getting street for account #" + data.getExternalSourceId());
			return null;
		}
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
			if (street.getCurrentType().getId().equals(type.getId())) {
				filteredStreets.add(street);
			}
		}

		return filteredStreets;
	}

	private StreetType findStreetType(Map<String, StreetType> nameTypeMap, RawConsumerData data) {

		StreetType typeById = correctionsService.findCorrection(
				data.getAddressStreetType(), StreetType.class, null);
		if (typeById != null) {
			return typeById;
		}

		return nameTypeMap.get(data.getAddressStreetType().toLowerCase());
	}

	private Apartment findApartment(RawConsumerData data, Street street, DataSourceDescription sd, RawDataSource<RawConsumerData> dataSource)
			throws Exception {
		Buildings buildings = buildingService.findBuildings(street, data.getAddressHouse(), data.getAddressBulk());
		if (buildings == null) {
			log.warn(String.format("Failed getting building for consumer, Street(%d, %s), Building(%s, %s) ",
					street.getId(), data.getAddressStreet(), data.getAddressHouse(), data.getAddressBulk()));
			ImportError error = addImportError(sd, data.getExternalSourceId(), Buildings.class, dataSource);
			error.setErrorId("error.eirc.import.building_not_found");
			setConsumerError(data, error);
			return null;
		}

		return findApartment(data, buildings, sd, dataSource);
	}

	private Apartment findApartment(RawConsumerData data, Buildings buildings, DataSourceDescription sd, RawDataSource<RawConsumerData> dataSource)
			throws Exception {
		Building building = buildings.getBuilding();
		if (building == null) {
			building = buildingService.findBuilding(buildings);
			if (building == null) {
				log.warn("Failed getting building for buildings #" + buildings.getId());
				ImportError error = addImportError(sd, data.getExternalSourceId(), Buildings.class, dataSource);
				error.setErrorId("error.eirc.import.building_not_found");
				setConsumerError(data, error);
				return null;
			}
		}

		Apartment stub = apartmentService.findApartmentStub(building, data.getAddressApartment());
		if (stub == null) {
			ImportError error = addImportError(sd, data.getExternalSourceId(), Apartment.class, dataSource);
			error.setErrorId("error.eirc.import.apartment_not_found");
			setConsumerError(data, error);
			return null;
		}

		return stub;
	}

	public void setConsumerDataConverter(RawConsumerDataConverter consumerDataConverter) {
		this.consumerDataConverter = consumerDataConverter;
	}

	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	public void setTypeService(IdentityTypeService typeService) {
		this.typeService = typeService;
	}

	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}
}
