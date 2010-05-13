package org.flexpay.ab.service.importexport;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.importexport.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.*;
import org.flexpay.ab.service.importexport.impl.AllObjectsDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.*;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.importexport.*;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional (readOnly = true)
public class ImportServiceImpl implements ImportService {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private RawDistrictDataConverter districtDataConverter;
	private RawStreetTypeDataConverter streetTypeDataConverter;
	private DataConverter streetDataConverter;
	private RawBuildingsDataConverter buildingsDataConverter;
	private RawApartmentDataConverter apartmentDataConverter;
	private RawPersonDataConverter personDataConverter;

	private DistrictJdbcDataSource districtDataSource;
	private StreetTypeJdbcDataSource streetTypeDataSource;
	private RawDataSource<? extends RawData<Street>> streetDataSource;
	private BuildingsJdbcDataSource buildingsDataSource;
	private ApartmentJdbcDataSource apartmentDataSource;
	private PersonJdbcDataSource personDataSource;

	protected AllObjectsDao allObjectsDao;

	protected CorrectionsService correctionsService;
	private DistrictService districtService;
	protected StreetTypeService streetTypeService;
	protected StreetService streetService;
	protected BuildingService buildingService;
	protected ApartmentService apartmentService;
	protected PersonService personService;

	private ImportErrorService importErrorService;
	private ImportErrorsSupport errorsSupport;
	private ClassToTypeRegistry registry;


	private static final int STACK_SIZE = 500;

	private List<DomainObject> objectsStack = new ArrayList<DomainObject>(STACK_SIZE + 5);

	protected void addPersonImportError(Stub<DataSourceDescription> sd, RawPersonData data) {
		ImportError error = addImportError(sd, data.getExternalSourceId(), Person.class, personDataSource);
		importErrorService.addError(error);
	}

	protected ImportError addImportError(Stub<DataSourceDescription> sd, String externalSourceId,
										 Class<? extends DomainObject> clazz, RawDataSource<? extends RawData> source) {

		ImportError error = new ImportError();
		error.setSourceDescription(new DataSourceDescription(sd));
		error.setSourceObjectId(externalSourceId);
		error.setObjectType(registry.getType(clazz));
		errorsSupport.setDataSourceBean(error, source);

		return error;
	}

	/**
	 * Run flush objects operation, also flushes and clear current session
	 */
	@Transactional (readOnly = false, propagation = Propagation.REQUIRED)
	protected void flushStack() {

		log.debug("Starting flushing stack, size: {}", objectsStack.size());

		try {
			DomainObject prevObj = null;
			for (DomainObject object : objectsStack) {
				// get previous object id for correction (may be null for new objects)
				if (object instanceof DataCorrection) {
					DataCorrection corr = (DataCorrection) object;
					if (corr.getInternalObjectId() == null) {
						if (prevObj == null) {
							log.error("Failed saving correction, object not specified: {}", corr);
						} else {
							corr.setInternalObjectId(prevObj.getId());
						}
					}
				}

				// Save building itself instead of its buildings address
				log.debug("Saving object: {}", object);
				if (object instanceof BuildingAddress) {
					allObjectsDao.saveOrUpdate(((BuildingAddress) object).getBuilding());
				} else if (object instanceof DataCorrection) {
					correctionsService.save((DataCorrection) object);
				} else {
					allObjectsDao.saveOrUpdate(object);
				}
				prevObj = object;
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed saving objects", e);
		} finally {
			allObjectsDao.flushAndClear();
		}
		objectsStack.clear();
	}

	protected void addToStack(DomainObject object) {
		if (objectsStack.size() >= STACK_SIZE) {
			flushStack();
		}
		objectsStack.add(object);
	}

	@Transactional (readOnly = false)
	@Override
	public void importDistricts(Town town, DataSourceDescription sourceDescription)
			throws FlexPayException {

		long time = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Starting import districts at {}", new Date());
		}

		ArrayStack filters = new ArrayStack();
		filters.push(new TownFilter(town.getId()));
		List<District> townDistricts = districtService.find(filters);
		Map<String, List<District>> nameObjsMap = initializeNamesToObjectsMap(townDistricts);
		districtDataSource.initialize();
		while (districtDataSource.hasNext()) {
			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawDistrictData data = districtDataSource.next(typeHolder);
			try {
				District district = districtDataConverter.fromRawData(data, sourceDescription, correctionsService);
				district.setParent(town);

				// Find object by correction
				Stub<District> persistentObj = correctionsService.findCorrection(
						data.getExternalSourceId(), District.class, stub(sourceDescription));

				// Find object by its name
				District nameMatchObj = findObject(nameObjsMap, district);

				// no corrections found
				if (persistentObj == null) {
					// this is a new object
					if (nameMatchObj == null) {
						log.info("Creating new District: {}", data.getName());
						addToStack(district);
					} else {
						// already existing object
						district = nameMatchObj;
					}
					log.info("Creating new district correction: {}", data.getName());
					DataCorrection correction = correctionsService.getStub(
							data.getExternalSourceId(), district, stub(sourceDescription));
					addToStack(correction);
				} else {
					if (nameMatchObj == null) {
						log.warn("Invalid correction found, no district found: {}", data.getName());
						addToStack(district);
						DataCorrection correction = correctionsService.getStub(
								data.getExternalSourceId(), district, stub(sourceDescription));
						addToStack(correction);
					} else {
						// correction found but objects do not match
						if (!nameMatchObj.getId().equals(persistentObj.getId())) {
							log.warn("Found by name district does not match correction");
							// TODO decide what to do here
						} else {
							log.info("District {} found", data.getName());
						}
					}
				}
			} catch (Exception e) {
				log.error("Failed getting district: " + data.getName(), e);
			}
		}
		flushStack();

		if (log.isInfoEnabled()) {
			log.info("End import districts at {}, total time: {} ms", new Date(), (System.currentTimeMillis() - time));
		}
	}

	@SuppressWarnings ({"unchecked"})
	@Transactional (readOnly = false)
	@Override
	public void importStreets(Town town, DataSourceDescription sourceDescription)
			throws FlexPayException {

		long time = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Starting import streets at {}", new Date());
		}

		ArrayStack filters = new ArrayStack();
		filters.push(new TownFilter(town.getId()));
		List<Street> townStreets = streetService.find(filters);
		log.debug("Town streets: {}", townStreets);
		Map<String, List<Street>> nameObjsMap = initializeNamesToObjectsMap(townStreets);

		streetDataSource.initialize();
		long tm = System.currentTimeMillis();
		while (streetDataSource.hasNext()) {
			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawData<Street> data = streetDataSource.next(typeHolder);

			try {
				// todo: jenerify me
				Street street = (Street) streetDataConverter.fromRawData(data, sourceDescription, correctionsService);
				street.setParent(town);

				// Find object by correction
				Stub<Street> persistentObj = correctionsService.findCorrection(
						data.getExternalSourceId(), Street.class, stub(sourceDescription));
				boolean found = correctionsService.existsCorrection(
						data.getExternalSourceId(), Street.class, stub(sourceDescription));
				log.info("{}ound street {}, total time: {} ms",
						new Object[]{(found ? "F" : "Not f"), data, (System.currentTimeMillis() - tm)});

				// Find object by its name
				Street nameMatchObj = findObject(nameObjsMap, street);

				saveStreetCorrection(sourceDescription, data, street, persistentObj, nameMatchObj);

				tm = System.currentTimeMillis();
			} catch (Exception e) {
				log.error("Failed getting street: " + data, e);
				throw new RuntimeException(e);
			}
		}
		flushStack();

		if (log.isInfoEnabled()) {
			log.info("End import streets at {}, total time: {} ms", new Date(), (System.currentTimeMillis() - time));
		}
	}

	private void saveStreetCorrection(DataSourceDescription sourceDescription, RawData<Street> data, Street street,
									  Stub<Street> persistentObj, Street nameMatchObj) {
		// no corrections found
		if (persistentObj == null) {
			// this is a new object
			if (nameMatchObj == null) {
				log.info("Creating new Street: {}", data);
				addToStack(street);
			} else {
				// already existing object
				street = nameMatchObj;
			}
			log.info("Creating new street correction: {}", data);
			DataCorrection correction = correctionsService.getStub(
					data.getExternalSourceId(), street, stub(sourceDescription));
			addToStack(correction);
		} else {
			if (nameMatchObj == null) {
				log.warn("Invalid correction found, no street found: {}", data);
				addToStack(street);
				DataCorrection correction = correctionsService.getStub(
						data.getExternalSourceId(), street, stub(sourceDescription));
				addToStack(correction);
			} else {
				//	correction found but objects do not match
				if (!persistentObj.getId().equals(nameMatchObj.getId())) {
					log.warn("Found by name street does not match correction: {}", data);
					// TODO decide what to do here
				} else {
					log.info("Street {} found", data);
				}
			}
		}
	}

	@SuppressWarnings ({"unchecked"})
	private <NTD extends NameTimeDependentChild> NTD findObject(Map<String, List<NTD>> ntdsMap, NTD newObj)
			throws FlexPayException {

		TemporaryName newName = (TemporaryName) newObj.getCurrentName();
		if (newName == null) {
			log.error("No current name for object: {}", newObj);
			return null;
		}
		Translation newTranslation = getDefaultLangTranslation(newName.getTranslations());
		String newObjName = newTranslation.getName().toLowerCase();

		if (!ntdsMap.containsKey(newObjName)) {
			return null;
		}
		List<NTD> ntds = ntdsMap.get(newObjName);
		for (NTD oldObj : ntds) {

			if (nonNameAttributesEquals(oldObj, newObj)) {
				return oldObj;
			}
		}

		return null;
	}

	/**
	 * Build mapping from object names to objects themself
	 *
	 * @param ntds List of objects
	 * @return mapping
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if language configuration is invalid
	 */
	@SuppressWarnings ({"unchecked"})
	protected <NTD extends NameTimeDependentChild> Map<String, List<NTD>> initializeNamesToObjectsMap(List<NTD> ntds)
			throws FlexPayException {

		Map<String, List<NTD>> stringNTDMap = new HashMap<String, List<NTD>>(ntds.size());
		for (NTD object : ntds) {
			TemporaryName tmpName = (TemporaryName) object.getCurrentName();
			if (tmpName == null) {
				log.error("No current name for object: {}", object);
				continue;
			}
			Translation defTranslation = getDefaultLangTranslation(tmpName.getTranslations());
			String name = defTranslation.getName().toLowerCase();
			List<NTD> val = stringNTDMap.containsKey(name) ?
							stringNTDMap.get(name) : CollectionUtils.<NTD>list();
			val.add(object);
			stringNTDMap.put(name, val);
		}

		return stringNTDMap;
	}

	@SuppressWarnings ({"ParameterAlwaysCastBeforeUse"})
	private boolean nonNameAttributesEquals(Object oldObj, Object newObj) {
		if (oldObj instanceof Street) {
			Street stOld = (Street) oldObj, stNew = (Street) newObj;
			StreetType typeOld = stOld.getCurrentType();
			StreetType typeNew = stNew.getCurrentType();
			if (typeOld == null || typeOld.getId() == null) {
				throw new IllegalArgumentException(
						"Street does not have type: " + stOld.getCurrentName());
			}
			return typeOld.getId().equals(typeNew.getId());
		}

		return true;
	}

	private Translation getDefaultLangTranslation(Collection<? extends Translation> translations) {

		Long defaultLangId = ApplicationConfig.getDefaultLanguage().getId();
		for (Translation translation : translations) {
			if (translation.getLang().getId().equals(defaultLangId)) {
				return translation;
			}
		}

		throw new IllegalArgumentException("No default lang translation found");
	}

	/**
	 * Import street types only builds corrections and does not add any new street types to the system, use User Interface,
	 * or plain SQL to add new types.
	 *
	 * @param sourceDescription Data source description
	 */
	@Transactional (readOnly = false)
	@Override
	public void importStreetTypes(DataSourceDescription sourceDescription) {
		streetTypeDataSource.initialize();

		while (streetTypeDataSource.hasNext()) {
			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawStreetTypeData data = streetTypeDataSource.next(typeHolder);
			try {
				Stub<StreetType> correction = correctionsService.findCorrection(
						data.getExternalSourceId(), StreetType.class, stub(sourceDescription));
				if (correction != null) {
					log.info("Found street type correction!");
					continue;
				}

				// Try to find general correction by type name
				Stub<StreetType> generalStub = correctionsService.findCorrection(
						data.getName(), StreetType.class, null);

				// found general correction, create specific one
				if (generalStub != null) {
					DataCorrection corr = correctionsService.getStub(
							data.getExternalSourceId(), new StreetType(generalStub), stub(sourceDescription));
					correctionsService.save(corr);
					log.info("Found general street type correction, adding special one!");
					continue;
				}

				StreetType streetType = streetTypeDataConverter.fromRawData(data, sourceDescription, correctionsService);

				// try to find correction by name
				setStreetTypeByName(sourceDescription, data, streetType);

			} catch (Exception e) {
				log.error("Failed getting street type with id: " + data.getExternalSourceId(), e);
				throw new RuntimeException(e);
			}
		}
	}

	private void setStreetTypeByName(DataSourceDescription sd, RawStreetTypeData data, StreetType extStreetType)
			throws FlexPayException {

		Translation tr = getDefaultLangTranslation(extStreetType.getTranslations());
		String extTypeName = tr.getName().toLowerCase();
		Stub<StreetType> stub = streetTypeService.findTypeByName(extTypeName);
		if (stub == null) {
			log.error("Cannot map external street type: {}", tr.getName());
			return;
		}

		DataCorrection corr = correctionsService.getStub(data.getExternalSourceId(), new StreetType(stub), stub(sd));
		correctionsService.save(corr);
		log.info("Creating correction by street type name: {}", tr.getName());
	}

	@Transactional (readOnly = false)
	@Override
	public void importBuildings(DataSourceDescription sourceDescription) throws Exception {
		buildingsDataSource.initialize();

		long time = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Starting import buildings at {}", new Date());
		}

		try {
			while (buildingsDataSource.hasNext()) {
				ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
				RawBuildingsData data = buildingsDataSource.next(typeHolder);

				try {
					Stub<BuildingAddress> correction = correctionsService.findCorrection(
							data.getExternalSourceId(), BuildingAddress.class, stub(sourceDescription));

					if (correction != null) {
						log.info("Found correction for building #{}", data.getExternalSourceId());
						continue;
					}

					BuildingAddress buildingAddress = buildingsDataConverter.fromRawData(
							data, sourceDescription, correctionsService);

					BuildingAddress persistent = buildingService.findAddresses(
							buildingAddress.getStreetStub(), buildingAddress.getDistrictStub(),
							buildingAddress.getBuildingAttributes());
					if (persistent == null) {
						// TODO: return back
						addToStack(buildingAddress);
//						addToStack(buildings.getBuilding());
						persistent = buildingAddress;
						log.info("Creating new building: {} - {}", buildingAddress.getNumber(), buildingAddress.getBulk());
					}

					DataCorrection corr = correctionsService.getStub(
							data.getExternalSourceId(), persistent, stub(sourceDescription));
					addToStack(corr);

					log.info("Creating new building correction");

				} catch (Exception e) {
					log.error("Failed getting building with id: " + data.getExternalSourceId(), e);
					throw e;
				}
			}

			flushStack();

			if (log.isInfoEnabled()) {
				log.info("End import buildings at {}, total time: {} ms", new Date(), (System.currentTimeMillis() - time));
			}
		} catch (Throwable t) {
			log.error("Failure", t);
			throw new RuntimeException(t.getMessage(), t);
		}
	}

	@Transactional (readOnly = false, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public void importApartments(DataSourceDescription sourceDescription) throws Exception {
		apartmentDataSource.initialize();

		long time = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Starting import apartments at {}", new Date());
		}

		long counter = 0;
		long cycleTime = System.currentTimeMillis();
		while (apartmentDataSource.hasNext()) {

			if (log.isInfoEnabled()) {
				long now = System.currentTimeMillis();
				log.info("Apartment #{}, time spent: {}", ++counter, (now - cycleTime));
				cycleTime = now;
			}

			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawApartmentData data = apartmentDataSource.next(typeHolder);

			try {
				Stub<Apartment> correction = correctionsService.findCorrection(
						data.getExternalSourceId(), Apartment.class, stub(sourceDescription));

				if (correction != null) {
					log.debug("Found correction for apartment #{}", data.getExternalSourceId());
					continue;
				}

				Apartment apartment = apartmentDataConverter.fromRawData(
						data, sourceDescription, correctionsService);

				Stub<Apartment> persistent = apartmentService.findApartmentStub(
						apartment.getBuilding(), apartment.getNumber());
				if (persistent == null) {
					addToStack(apartment);
					persistent = stub(apartment);
					log.debug("Creating new apartment: {}", apartment.getNumber());

				}

				DataCorrection corr = correctionsService.getStub(
						data.getExternalSourceId(), new Apartment(persistent), stub(sourceDescription));
				addToStack(corr);

				log.debug("Creating new apartment correction");

			} catch (Exception e) {
				log.error("Failed getting apartment with id: " + data.getExternalSourceId(), e);
				throw e;
			}
		}

		flushStack();
		if (log.isInfoEnabled()) {
			log.info("End import apartments at {}, total time: {}ms", new Date(), (System.currentTimeMillis() - time));
		}
	}

	@Transactional (readOnly = false, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public void importPersons(Stub<DataSourceDescription> sd) throws Exception {
		personDataSource.initialize();

		long time = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Starting import persons at {}", new Date());
		}

		long counter = 0;
		long cycleTime = System.currentTimeMillis();
		while (personDataSource.hasNext()) {

			if (log.isInfoEnabled()) {
				long now = System.currentTimeMillis();
				log.info("Person #{}, time spent: {}", ++counter, (now - cycleTime));
				cycleTime = now;
			}

			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawPersonData data = personDataSource.next(typeHolder);
			if (data == null) {
				continue;
			}

			try {
				Stub<Person> correction = correctionsService.findCorrection(
						data.getExternalSourceId(), Person.class, sd);

				if (correction != null) {
					log.info("Found correction for person {}", data.getExternalSourceId());
					continue;
				}

				Person person = personDataConverter.fromRawData(
						data, new DataSourceDescription(sd), correctionsService);

				Stub<Person> persistent = personService.findPersonStub(person);
				if (persistent == null) {
					if (personDataSource.trusted()) {
						addToStack(person);
						persistent = stub(person);
						log.info("Creating new person: {}", person);
					} else {
						addPersonImportError(sd, data);
						log.warn("Cannot find person: {}", person);
						continue;
					}
				}

				// persistent person found, set up correction
				DataCorrection corr = correctionsService.getStub(
						data.getExternalSourceId(), new Person(persistent), sd);
				addToStack(corr);

				log.info("Creating new person correction");

			} catch (Exception e) {
				log.error("Failed getting person: " + data.getExternalSourceId(), e);
				throw e;
			}
		}

		flushStack();
		if (log.isInfoEnabled()) {
			log.info("End import persons at {}, total time: {} ms", new Date(), (System.currentTimeMillis() - time));
		}
	}

	@Required
	public void setDistrictDataConverter(RawDistrictDataConverter districtDataConverter) {
		this.districtDataConverter = districtDataConverter;
	}

	@Required
	public void setStreetDataConverter(DataConverter streetDataConverter) {
		this.streetDataConverter = streetDataConverter;
	}

	@Required
	public void setPersonDataConverter(RawPersonDataConverter personDataConverter) {
		this.personDataConverter = personDataConverter;
	}

	@Required
	public void setDistrictDataSource(DistrictJdbcDataSource districtDataSource) {
		this.districtDataSource = districtDataSource;
	}

	@Required
	public void setStreetDataSource(RawDataSource<? extends RawData<Street>> streetDataSource) {
		this.streetDataSource = streetDataSource;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@Required
	public void setStreetTypeDataConverter(RawStreetTypeDataConverter streetTypeDataConverter) {
		this.streetTypeDataConverter = streetTypeDataConverter;
	}

	@Required
	public void setStreetTypeDataSource(StreetTypeJdbcDataSource streetTypeDataSource) {
		this.streetTypeDataSource = streetTypeDataSource;
	}

	@Required
	public void setBuildingsDataSource(BuildingsJdbcDataSource buildingsDataSource) {
		this.buildingsDataSource = buildingsDataSource;
	}

	@Required
	public void setApartmentDataSource(ApartmentJdbcDataSource apartmentDataSource) {
		this.apartmentDataSource = apartmentDataSource;
	}

	@Required
	public void setPersonDataSource(PersonJdbcDataSource personDataSource) {
		this.personDataSource = personDataSource;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@Required
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	@Required
	public void setBuildingsDataConverter(RawBuildingsDataConverter buildingsDataConverter) {
		this.buildingsDataConverter = buildingsDataConverter;
	}

	@Required
	public void setApartmentDataConverter(RawApartmentDataConverter apartmentDataConverter) {
		this.apartmentDataConverter = apartmentDataConverter;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	@Required
	public void setAllObjectsDao(AllObjectsDao allObjectsDao) {
		this.allObjectsDao = allObjectsDao;
	}

	@Required
	public void setImportErrorService(ImportErrorService importErrorService) {
		this.importErrorService = importErrorService;
	}

	@Required
	public void setErrorsSupport(ImportErrorsSupport errorsSupport) {
		this.errorsSupport = errorsSupport;
	}

	@Required
	public void setRegistry(ClassToTypeRegistry registry) {
		this.registry = registry;
	}

}
