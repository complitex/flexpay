package org.flexpay.eirc.service.importexport;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.importexport.ImportService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.util.config.ApplicationConfig;

import java.util.*;

public class EircImportService extends ImportService {

	private RawConsumerDataConverter consumerDataConverter;
	private ConsumerService consumerService;
	private IdentityTypeService typeService;

	public void importConsumers(DataSourceDescription sd, RawConsumersDataSource dataSource)
			throws FlexPayException {

		Town defaultTown = ApplicationConfig.getInstance().getDefaultTown();
		ArrayStack filters = new ArrayStack();
		filters.push(new TownFilter(defaultTown.getId()));
		List<Street> townStreets = streetService.find(filters);
		if (log.isDebugEnabled()) {
			log.debug("Town streets: " + townStreets);
		}

		Map<String, List<Street>> nameObjsMap = initializeNamesToObjectsMap(townStreets);
		Map<String, StreetType> nameTypeMap = initializeTypeNamesToObjectsMap();

		dataSource.initialize();
		while (dataSource.hasNext()) {

			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawConsumerData data = dataSource.next(typeHolder);

			if (data == null) {
				log.info("Empty data read");
				continue;
			}

			try {
				// Find object by correction
				Consumer persistentObj = (Consumer) correctionsService.findCorrection(
						data.getCorrectionId(), Consumer.class, sd);

				if (persistentObj != null) {
					log.info("Found existing consumer correction: #" + data.getExternalSourceId());
					continue;
				}

				Consumer rawConsumer = consumerDataConverter.fromRawData(
						data, sd, correctionsService);

				if (rawConsumer.getApartment() == null) {
					// Find apartment
					Apartment apartment = findApartment(nameObjsMap, nameTypeMap, sd, data);
					if (apartment == null) {
						addImportError(sd, data.getExternalSourceId(), Apartment.class, dataSource);
						continue;
					}
					rawConsumer.setApartment(apartment);
				}

				if (rawConsumer.getResponsiblePerson() == null) {
					Person person = findPerson(data);
					if (person == null) {
						addImportError(sd, data.getExternalSourceId(), Person.class, dataSource);
						continue;
					}
					// found person by name
					DataCorrection corr = correctionsService.getStub(
							data.getPersonCorrectionId(), person, sd);
					addToStack(corr);
					rawConsumer.setResponsiblePerson(person);
				}

				if (rawConsumer.getService() == null) {
					log.error("Not found provider #" + data.getRegistry().getSenderCode() +
							  " service by code: " + data.getRegistryRecord().getServiceCode());
					continue;
				}

				Consumer persistent = consumerService.findConsumer(rawConsumer);
				if (persistent == null) {
					log.info("Creating new consumer: " + data.getCorrectionId());
					addToStack(rawConsumer);
					persistent = rawConsumer;
				}

				// Consumer found/created, add new correction
				if (log.isInfoEnabled()) {
					log.info("Creating new consumer: " + data.getCorrectionId());
				}
				DataCorrection corr = correctionsService.getStub(
						data.getCorrectionId(), persistent, sd);
				addToStack(corr);
			} catch (Exception e) {
				log.error("Failed getting consumer: " + data.toString(), e);
				throw new RuntimeException(e);
			}
		}

		flushStack();
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
									DataSourceDescription sourceDescription, RawConsumerData data) {

		// try to find by apartment correction
		Apartment apartmentById = (Apartment) correctionsService.findCorrection(
				data.getApartmentId(), Apartment.class, sourceDescription);
		if (apartmentById != null) {
			log.info("Found apartment correction");
			return apartmentById;
		}

		Buildings buildingsById = (Buildings) correctionsService.findCorrection(
				data.getBuildingId(), Buildings.class, sourceDescription);
		if (buildingsById != null) {
			log.info("Found buildings correction");
			return findApartment(data, buildingsById);
		}

		Street streetById = (Street) correctionsService.findCorrection(
				data.getStreetId(), Street.class, sourceDescription);
		if (streetById != null) {
			log.info("Found street correction");
			return findApartment(data, streetById);
		}

		Street streetByName = findStreet(nameObjsMap, nameTypeMap, data);
		if (streetByName == null) {
			log.warn("Failed getting street for account #" + data.getExternalSourceId());
			return null;
		}
		return findApartment(data, streetByName);
	}

	private Street findStreet(Map<String, List<Street>> nameObjsMap,
							  Map<String, StreetType> nameTypeMap, RawConsumerData data) {
		List<Street> streets = nameObjsMap.get(data.getAddressStreet().toLowerCase());
		// no candidates
		if (streets == null) {
			return null;
		}

		// the only candidate
		if (streets.size() == 1) {
			return streets.get(0);
		}

		StreetType streetType = findStreetType(nameTypeMap, data);
		if (streetType == null) {
			log.warn("Found several streets, but no type was found: " + data.getAddressStreetType() +
					 ", " + data.getAddressStreet());
			return null;
		}

		List<Street> filteredStreets = filterStreetsbyType(streets, streetType);
		if (filteredStreets.size() == 1) {
			return filteredStreets.get(0);
		}

		log.warn("Cannot find street candidate, even by type: " + data.getAddressStreetType() +
				 ", " + data.getAddressStreet());
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

		StreetType typeById = (StreetType) correctionsService.findCorrection(
				data.getAddressStreetType(), StreetType.class, null);
		if (typeById != null) {
			return typeById;
		}

		return nameTypeMap.get(data.getAddressStreetType().toLowerCase());
	}

	private Apartment findApartment(RawConsumerData data, Street street) {
		Buildings buildings = buildingService.findBuildings(street, data.getAddressHouse(), data.getAddressBulk());
		if (buildings == null) {
			log.warn("Failed getting building for consumer: " + data.getExternalSourceId());
			return null;
		}

		return findApartment(data, buildings);
	}

	private Apartment findApartment(RawConsumerData data, Buildings buildings) {
		Building building = buildings.getBuilding();
		if (building == null) {
			building = buildingService.findBuilding(buildings);
			if (building == null) {
				log.warn("Failed getting building for buildings #" + buildings.getId());
				return null;
			}
		}

		return apartmentService.findApartmentStub(building, data.getAddressApartment());
	}

	/**
	 * Setter for property 'consumerDataConverter'.
	 *
	 * @param consumerDataConverter Value to set for property 'consumerDataConverter'.
	 */
	public void setConsumerDataConverter(RawConsumerDataConverter consumerDataConverter) {
		this.consumerDataConverter = consumerDataConverter;
	}

	/**
	 * Setter for property 'consumerService'.
	 *
	 * @param consumerService Value to set for property 'consumerService'.
	 */
	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	/**
	 * Setter for property 'typeService'.
	 *
	 * @param typeService Value to set for property 'typeService'.
	 */
	public void setTypeService(IdentityTypeService typeService) {
		this.typeService = typeService;
	}
}
