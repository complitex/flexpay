package org.flexpay.eirc.service.importexport;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.importexport.ImportService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.ImportErrorService;
import org.flexpay.common.service.importexport.ImportErrorsSupport;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.eirc.dao.importexport.PersonalAccountJdbcDataSource;
import org.flexpay.eirc.dao.importexport.RawPersonalAccountDataSource;
import org.flexpay.eirc.persistence.PersonalAccount;
import org.flexpay.eirc.service.PersonalAccountService;
import org.flexpay.eirc.util.PersonalAccountUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EircImportService extends ImportService {

	private PersonalAccountJdbcDataSource personalAccountDataSource;
	private RawPersonalAccountDataConverter personalAccountDataConverter;

	private PersonalAccountService personalAccountService;

	private ImportErrorService importErrorService;
	private ImportErrorsSupport errorsSupport;
	private ClassToTypeRegistry registry;

	public void importPersonalAccounts(Town town, DataSourceDescription sd, RawPersonalAccountDataSource sataSource)
			throws FlexPayException {

		ArrayStack filters = new ArrayStack();
		filters.push(new TownFilter(town.getId()));
		List<Street> townStreets = streetService.find(filters);
		if (log.isDebugEnabled()) {
			log.debug("Town streets: " + townStreets);
		}

		Map<String, List<Street>> nameObjsMap = initializeNamesToObjectsMap(townStreets);
		Map<String, StreetType> nameTypeMap = initializeTypeNamesToObjectsMap();

		sataSource.initialize();
		while (sataSource.hasNext()) {
			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawPersonalAccountData data = sataSource.next(typeHolder);

			if (data == null) {
				log.info("Empty data read");
				continue;
			}

			try {
				// Find object by correction
				PersonalAccount persistentObj = (PersonalAccount) correctionsService.findCorrection(
						data.getExtAccount(), PersonalAccount.class, sd);

				if (persistentObj != null) {
					log.info("Found existing personal account correction: #" + data.getExternalSourceId());
					continue;
				}

				PersonalAccount rawAccount = personalAccountDataConverter.fromRawData(
						data, sd, correctionsService);
				rawAccount.setAccountNumber(PersonalAccountUtil.nextPersonalAccount());

				// if data source is considered trusted - add new PersonalAccount
				if (sataSource.trusted()) {

					if (log.isInfoEnabled()) {
						log.info("Creating new person, personal account and correction: #"
								 + data.getExternalSourceId());
					}

					addToStack(rawAccount.getResponsiblePerson());
					addToStack(rawAccount);

					DataCorrection corr = correctionsService.getStub(
							data.getExtAccount(), rawAccount, sd);
					addToStack(corr);
				} else {
					// Find apartment
					Apartment apartment = findApartment(nameObjsMap, nameTypeMap, sd, data);

					if (apartment == null) {
						addImportError(sd, data);
						continue;
					}

					// OK, apartment found, now scan for personal accounts
					PersonalAccount account = findPersonalAccount(apartment, data);
					if (account == null) {
						addImportError(sd, data);
						continue;
					}

					// Account found, add new correction
					DataCorrection corr = correctionsService.getStub(
							data.getExtAccount(), account, sd);
					addToStack(corr);
				}
			} catch (Exception e) {
				log.error("Failed getting personal account: " + data.toString(), e);
				throw new RuntimeException(e);
			}
		}

		flushStack();
	}

	private void addImportError(DataSourceDescription ds, RawPersonalAccountData data) {
		ImportError error = new ImportError();
		error.setSourceDescription(ds);
		error.setSourceObjectId(data.getExtAccount());
		error.setObjectType(registry.getType(PersonalAccount.class));
		errorsSupport.setDataSourceBean(error, personalAccountDataSource);

		importErrorService.addError(error);
	}

	private PersonalAccount findPersonalAccount(Apartment apartment, RawPersonalAccountData data) {

		List<PersonalAccount> filteredAccounts = new ArrayList<PersonalAccount>();
		List<PersonalAccount> accounts = personalAccountService.findAccounts(apartment);
		if (accounts.isEmpty()) {
			log.warn("No Personal accounts were created for apartment #" + apartment.getId());
			return null;
		}

		String lastName = data.getLastName().toLowerCase();
		for (PersonalAccount account : accounts) {
			Person person = account.getResponsiblePerson();
			for (PersonIdentity identity : person.getPersonIdentities()) {
				if (identity.getLastName().toLowerCase().equals(lastName)) {
					filteredAccounts.add(account);
					break;
				}
			}
		}

		if (filteredAccounts.isEmpty()) {
			log.warn("No personal accounts were found by last name for apartment #" + apartment.getId());
			return null;
		}
		if (filteredAccounts.size() > 1) {
			log.warn("Several personal accounts were found by last name for apartment #" + apartment.getId());
			return null;
		}

		return filteredAccounts.get(0);
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
									DataSourceDescription sourceDescription, RawPersonalAccountData data) {

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
							  Map<String, StreetType> nameTypeMap, RawPersonalAccountData data) {
		List<Street> streets = nameObjsMap.get(data.getStreet().toLowerCase());
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
			log.warn("Found several streets, but no type was found: " + data.getStreetType() +
					 ", " + data.getStreet());
			return null;
		}

		List<Street> filteredStreets = filterStreetsbyType(streets, streetType);
		if (filteredStreets.size() == 1) {
			return filteredStreets.get(0);
		}

		log.warn("Cannot find street candidate, even by type: " + data.getStreetType() +
				 ", " + data.getStreet());
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

	private StreetType findStreetType(Map<String, StreetType> nameTypeMap, RawPersonalAccountData data) {

		StreetType typeById = (StreetType) correctionsService.findCorrection(
				data.getStreetType(), StreetType.class, null);
		if (typeById != null) {
			return typeById;
		}

		return nameTypeMap.get(data.getStreetType().toLowerCase());
	}

	private Apartment findApartment(RawPersonalAccountData data, Street street) {
		Buildings buildings = buildingService.findBuildings(street, data.getBuilding(), data.getBulk());
		if (buildings == null) {
			log.warn("Failed getting building for account #" + data.getExternalSourceId());
			return null;
		}

		return findApartment(data, buildings);
	}

	private Apartment findApartment(RawPersonalAccountData data, Buildings buildings) {
		Building building = buildings.getBuilding();
		if (building == null) {
			building = buildingService.findBuilding(buildings);
			if (building == null) {
				log.warn("Failed getting building for buildings #" + buildings.getId());
				return null;
			}
		}

		return apartmentService.findApartmentStub(building, data.getApartment());
	}

	public void setPersonalAccountDataSource(PersonalAccountJdbcDataSource personalAccountDataSource) {
		this.personalAccountDataSource = personalAccountDataSource;
	}

	public void setPersonalAccountDataConverter(RawPersonalAccountDataConverter personalAccountDataConverter) {
		this.personalAccountDataConverter = personalAccountDataConverter;
	}

	public void setPersonalAccountService(PersonalAccountService personalAccountService) {
		this.personalAccountService = personalAccountService;
	}

	/**
	 * Setter for property 'importErrorService'.
	 *
	 * @param importErrorService Value to set for property 'importErrorService'.
	 */
	public void setImportErrorService(ImportErrorService importErrorService) {
		this.importErrorService = importErrorService;
	}

	/**
	 * Setter for property 'errorsSupport'.
	 *
	 * @param errorsSupport Value to set for property 'errorsSupport'.
	 */
	public void setErrorsSupport(ImportErrorsSupport errorsSupport) {
		this.errorsSupport = errorsSupport;
	}

	/**
	 * Setter for property 'registry'.
	 *
	 * @param registry Value to set for property 'registry'.
	 */
	public void setRegistry(ClassToTypeRegistry registry) {
		this.registry = registry;
	}
}
