package org.flexpay.eirc.service.importexport;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.importexport.ImportService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.persistence.workflow.TransitionNotAllowed;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class EircImportService extends ImportService {

	private RawConsumerDataConverter consumerDataConverter;
	private ConsumerService consumerService;
	private IdentityTypeService typeService;

	private RegistryRecordWorkflowManager recordWorkflowManager;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
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
		long now = System.currentTimeMillis();
		while (dataSource.hasNext()) {

			logTime(now, 0);

			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawConsumerData data = dataSource.next(typeHolder);

			if (data == null) {
				log.info("Empty data read");
				continue;
			}

			try {
				recordWorkflowManager.startProcessing(data.getRegistryRecord());
			} catch (TransitionNotAllowed e) {
				if (log.isInfoEnabled()) {
					log.info("Skipping record, processing not allowed: " + data.getExternalSourceId());
				}
				continue;
			}

			logTime(now, 1);

			try {
				// Find object by correction
				Consumer persistentObj = correctionsService.findCorrection(
						data.getCorrectionId(), Consumer.class, sd);

				logTime(now, 2);
				if (persistentObj != null) {
					log.info("Found existing consumer correction: #" + data.getExternalSourceId());
					postSaveConsumer(data, persistentObj);
					continue;
				}

				Consumer rawConsumer = consumerDataConverter.fromRawData(data, sd, correctionsService);

				logTime(now, 3);
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
				log.info("Found apartment: " + data.getApartmentId());

				logTime(now, 4);
				if (rawConsumer.getResponsiblePerson() == null) {
					Person person = findPerson(data);
					if (person == null) {
						logTime(now, 51);
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
				log.info("Found responsible person: " + data.getPersonCorrectionId());

				logTime(now, 5);
				if (rawConsumer.getService() == null) {
					log.error("Not found provider #" + data.getRegistry().getSenderCode() +
							" service by code: " + data.getRegistryRecord().getServiceCode());
					continue;
				}
				log.info("Found service");

				logTime(now, 6);
				Consumer persistent = consumerService.findConsumer(rawConsumer);
				if (persistent == null) {
					log.info("Creating new consumer: " + data.getCorrectionId());
					addToStack(rawConsumer);
					persistent = rawConsumer;
				}

				// Consumer found/created, add new correction
				if (log.isInfoEnabled()) {
					log.info("Creating new consumer correction: " + data.getCorrectionId());
				}
				DataCorrection corr = correctionsService.getStub(
						data.getCorrectionId(), persistent, sd);
				addToStack(corr);

				logTime(now, 7);

				postSaveConsumer(data, persistent);
				logTime(now, 8);
			} catch (Exception e) {
				log.error("Failed getting consumer: " + data.toString(), e);
				throw new RuntimeException(e);
			} finally {
				now = System.currentTimeMillis();
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

	private void logTime(long start, int label) {
		if (log.isInfoEnabled()) {
			log.info("Time spent " + label + ": " + ((System.currentTimeMillis() - start) / 1000) + " ms. ");
		}
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

	protected void addToStack(DomainObject object) {
		super.addToStack(object);
		flushStack();
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
