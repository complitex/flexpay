package org.flexpay.ab.service.importexport;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.importexport.BuildingsJdbcDataSource;
import org.flexpay.ab.dao.importexport.DistrictJdbcDataSource;
import org.flexpay.ab.dao.importexport.StreetJdbcDataSource;
import org.flexpay.ab.dao.importexport.StreetTypeJdbcDataSource;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.ab.service.importexport.imp.AllObjectsDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.*;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.util.config.ApplicationConfig;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class ImportService {

	private static Logger log = Logger.getLogger(ImportService.class);

	private RawDistrictDataConverter districtDataConverter;
	private RawStreetTypeDataConverter streetTypeDataConverter;
	private RawStreetDataConverter streetDataConverter;
	private RawBuildingsDataConverter buildingsDataConverter;

	private DistrictJdbcDataSource districtDataSource;
	private StreetTypeJdbcDataSource streetTypeDataSource;
	private StreetJdbcDataSource streetDataSource;
	private BuildingsJdbcDataSource buildingsDataSource;

	private AllObjectsDao allObjectsDao;

	private CorrectionsService correctionsService;
	private DistrictService districtService;
	private StreetTypeService streetTypeService;
	private StreetService streetService;
	private BuildingService buildingService;

	private List<DomainObject> objectsStack = new ArrayList<DomainObject>();

	@Transactional (readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void flushStack() {
		DomainObject prevObj = null;
		for (DomainObject object : objectsStack) {
			if (object instanceof DataCorrection) {
				DataCorrection corr = (DataCorrection) object;
				if (corr.getInternalObjectId() == null) {
					if (prevObj == null) {
						log.error("Failed saving correction, object not specified: " + corr);
					} else {
						corr.setInternalObjectId(prevObj.getId());
					}
				}
			}
			// Save building itself instead of its buildings address
			if (object instanceof Buildings) {
				log.info("Saving building!");
				allObjectsDao.save(((Buildings) object).getBuilding());
				log.info("Saved: buildings id: " + object.getId());
			} else {
				allObjectsDao.save(object);
			}
			prevObj = object;
		}
		objectsStack.clear();
	}

	private void addToStack(DomainObject object) {
		if (objectsStack.size() >= 20 && objectsStack.get(objectsStack.size() - 1) instanceof DataCorrection) {
			flushStack();
		}
		objectsStack.add(object);
	}

	@Transactional (readOnly = false)
	public void importDistricts(Town town, DataSourceDescription sourceDescription)
			throws FlexPayException {

		long time = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Starting import districts at " + new Date());
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
				District persistentObj = (District) correctionsService.findCorrection(
						data.getExternalSourceId(), District.class, sourceDescription);

				// Find object by its name
				District nameMatchObj = findObject(nameObjsMap, district);

				// no corrections found
				if (persistentObj == null) {
					// this is a new object
					if (nameMatchObj == null) {
						log.info("Creating new District: " + data.getName());
						addToStack(district);
					} else {
						// already existing object
						district = nameMatchObj;
					}
					log.info("Creating new district correction: " + data.getName());
					DataCorrection correction = correctionsService.getStub(
							data.getExternalSourceId(), district, sourceDescription);
					addToStack(correction);
				} else {
					if (nameMatchObj == null) {
						log.warn("Invalid correction found, no district found: " + data.getName());
						addToStack(district);
						DataCorrection correction = correctionsService.getStub(
								data.getExternalSourceId(), district, sourceDescription);
						addToStack(correction);
					} else {
						// correction found but objects do not match
						if (!nameMatchObj.getId().equals(persistentObj.getId())) {
							log.warn("Found by name district does not match correction");
							// TODO decide what to do here
						} else {
							if (log.isInfoEnabled()) {
								log.info("District " + data.getName() + " found");
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("Failed getting district: " + data.getName(), e);
			}
		}
		flushStack();

		if (log.isInfoEnabled()) {
			log.info("End import districts at " + new Date() + ", total time: " +
					 (System.currentTimeMillis() - time) + "ms");
		}
	}

	@Transactional (readOnly = false)
	public void importStreets(Town town, DataSourceDescription sourceDescription)
			throws FlexPayException {

		long time = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Starting import streets at " + new Date());
		}

		ArrayStack filters = new ArrayStack();
		filters.push(new TownFilter(town.getId()));
		List<Street> townStreets = streetService.find(filters);
		if (log.isDebugEnabled()) {
			log.debug("Town streets: " + townStreets);
		}
		Map<String, List<Street>> nameObjsMap = initializeNamesToObjectsMap(townStreets);
		streetDataSource.initialize();
		long tm = System.currentTimeMillis();
		while (streetDataSource.hasNext()) {
			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawStreetData data = streetDataSource.next(typeHolder);

			try {
				Street street = streetDataConverter.fromRawData(data, sourceDescription, correctionsService);
				street.setParent(town);

				// Find object by correction
				Street persistentObj = (Street) correctionsService.findCorrection(
						data.getExternalSourceId(), Street.class, sourceDescription);
				boolean found = correctionsService.existsCorrection(
						data.getExternalSourceId(), Street.class, sourceDescription);
				if (log.isInfoEnabled()) {
					log.info((found ? "F" : "Not f") + "ound street " + data.getName() + ", total time: " +
							 (System.currentTimeMillis() - tm) + "ms");
				}

				// Find object by its name
//				Street nameMatchObj = null;
				Street nameMatchObj = findObject(nameObjsMap, street);

				saveStreetCorrection(sourceDescription, data, street, persistentObj, nameMatchObj);

				tm = System.currentTimeMillis();
			} catch (Exception e) {
				log.error("Failed getting street: " + data.getName(), e);
				throw new RuntimeException(e);
			}
		}
		flushStack();

		if (log.isInfoEnabled()) {
			log.info("End import streets at " + new Date() + ", total time: " +
					 (System.currentTimeMillis() - time) + "ms");
		}
	}

	private void saveStreetCorrection(DataSourceDescription sourceDescription, RawStreetData data, Street street,
									  Street persistentObj, Street nameMatchObj) {
		// no corrections found
		if (persistentObj == null) {
			// this is a new object
			if (nameMatchObj == null) {
				log.info("Creating new Street: " + data.getName());
				addToStack(street);
			} else {
				// already existing object
				street = nameMatchObj;
			}
			log.info("Creating new street correction: " + data.getName());
			DataCorrection correction = correctionsService.getStub(
					data.getExternalSourceId(), street, sourceDescription);
			addToStack(correction);
		} else {
			if (nameMatchObj == null) {
				log.warn("Invalid correction found, no street found: " + data.getName());
				addToStack(street);
				DataCorrection correction = correctionsService.getStub(
						data.getExternalSourceId(), street, sourceDescription);
				addToStack(correction);
			} else {
				//	correction found but objects do not match
				if (!nameMatchObj.getId().equals(persistentObj.getId())) {
					log.warn("Found by name street does not match correction: " + data.getName());
					// TODO decide what to do here
				} else {
					if (log.isInfoEnabled()) {
						log.info("Street " + data.getName() + " found");
					}
				}
			}
		}
	}

	@SuppressWarnings ({"unchecked"})
	private <NTD extends NameTimeDependentChild> NTD findObject(Map<String, List<NTD>> ntdsMap, NTD newObj)
			throws FlexPayException {

		TemporaryName newName = (TemporaryName) newObj.getCurrentName();
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
	 * @throws FlexPayException if language configuration is invalid
	 */
	@SuppressWarnings ({"unchecked"})
	private <NTD extends NameTimeDependentChild> Map<String, List<NTD>> initializeNamesToObjectsMap(List<NTD> ntds)
			throws FlexPayException {

		Map<String, List<NTD>> stringNTDMap = new HashMap<String, List<NTD>>(ntds.size());
		for (NTD object : ntds) {
			TemporaryName tmpName = (TemporaryName) object.getCurrentName();
			Translation defTranslation = getDefaultLangTranslation(tmpName.getTranslations());
			String name = defTranslation.getName().toLowerCase();
			List<NTD> val = stringNTDMap.containsKey(name) ?
							stringNTDMap.get(name) : new ArrayList<NTD>();
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

	private Translation getDefaultLangTranslation(Collection<? extends Translation> translations)
			throws FlexPayException {

		Long defaultLangId = ApplicationConfig.getInstance().getDefaultLanguage().getId();
		for (Translation translation : translations) {
			if (translation.getLang().getId().equals(defaultLangId)) {
				return translation;
			}
		}

		throw new IllegalArgumentException("No default lang translation found");
	}

	/**
	 * Import street types only builds corrections and does not add any new street types to
	 * the system, use User Interface, or plain SQL to add new types.
	 *
	 * @param sourceDescription Data source description
	 */
	@Transactional (readOnly = false)
	public void importStreetTypes(DataSourceDescription sourceDescription) {
		streetTypeDataSource.initialize();
		List<StreetType> streetTypes = streetTypeService.getEntities();

		if (log.isDebugEnabled()) {
			log.debug("Street types: " + streetTypes);
		}

		while (streetTypeDataSource.hasNext()) {
			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawStreetTypeData data = streetTypeDataSource.next(typeHolder);
			try {
				StreetType streetType = streetTypeDataConverter.fromRawData(data, sourceDescription, correctionsService);
				StreetType correction = (StreetType) correctionsService.findCorrection(
						data.getExternalSourceId(), StreetType.class, sourceDescription);
				if (correction != null) {
					log.info("Found street type correction!");
					continue;
				}

				// Try to find general correction by type name
				StreetType generalCorrection = (StreetType) correctionsService.findCorrection(
						data.getName(), StreetType.class, null);

				// found general correction, create specific one
				if (generalCorrection != null) {
					DataCorrection corr = correctionsService.getStub(
							data.getExternalSourceId(), generalCorrection, sourceDescription);
					correctionsService.save(corr);
					log.info("Found general street type correction, adding special one!");
					continue;
				}

				// try to find correction by name
				findInternalStreetTypeByName(sourceDescription, streetTypes, data, streetType);

			} catch (Exception e) {
				log.error("Failed getting street type with id: " + data.getExternalSourceId(), e);
			}
		}
	}

	private void findInternalStreetTypeByName(
			DataSourceDescription sourceDescription, List<StreetType> streetTypes,
			RawStreetTypeData data, StreetType extStreetType) throws FlexPayException {

		Translation tr = getDefaultLangTranslation(extStreetType.getTranslations());
		String extTypeName = tr.getName().toLowerCase();
		for (StreetType type : streetTypes) {
			Translation ourType = getDefaultLangTranslation(type.getTranslations());

			// found translation in default lang, add correction
			if (ourType.getName().toLowerCase().equals(extTypeName)) {
				DataCorrection corr = correctionsService.getStub(
						data.getExternalSourceId(), type, sourceDescription);
				correctionsService.save(corr);
				log.info("Creating correction by street type name: " + tr.getName());
				return;
			}
		}

		log.error("Cannot map external street type: " + tr.getName());
	}

	@Transactional (readOnly = false)
	public void importBuildings(DataSourceDescription sourceDescription) throws Exception {
		buildingsDataSource.initialize();

		long time = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Starting import buildings at " + new Date());
		}

		while (buildingsDataSource.hasNext()) {
			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawBuildingsData data = buildingsDataSource.next(typeHolder);

			try {
				Buildings buildings = buildingsDataConverter.fromRawData(
						data, sourceDescription, correctionsService);

				Buildings correction = (Buildings) correctionsService.findCorrection(
						data.getExternalSourceId(), Buildings.class, sourceDescription);

				if (correction != null) {
					if (log.isInfoEnabled()) {
						log.info("Found correction for building #" + data.getExternalSourceId());
					}
					continue;
				}

				Buildings persistent = buildingService.findBuildings(
						buildings.getStreet(), buildings.getBuilding().getDistrict(),
						buildings.getNumber(), buildings.getBulk());
				if (persistent == null) {
					addToStack(buildings);
					persistent = buildings;
					if (log.isInfoEnabled()) {
						log.info("Creating new building: " + buildings.getNumber() +
								 " - " + buildings.getBulk());
					}
				}

				DataCorrection corr = correctionsService.getStub(
						data.getExternalSourceId(), persistent, sourceDescription);
				addToStack(corr);

				log.info("Creating new building correction");

			} catch (Exception e) {
				log.error("Failed getting building with id: " + data.getExternalSourceId(), e);
				throw e;
			}
		}

		flushStack();

		if (log.isInfoEnabled()) {
			log.info("End import buildings at " + new Date() + ", total time: " +
					 (System.currentTimeMillis() - time) + "ms");
		}
	}

	/**
	 * Setter for property 'districtDataConverter'.
	 *
	 * @param districtDataConverter Value to set for property 'districtDataConverter'.
	 */
	public void setDistrictDataConverter(RawDistrictDataConverter districtDataConverter) {
		this.districtDataConverter = districtDataConverter;
	}

	/**
	 * Setter for property 'streetDataConverter'.
	 *
	 * @param streetDataConverter Value to set for property 'streetDataConverter'.
	 */
	public void setStreetDataConverter(RawStreetDataConverter streetDataConverter) {
		this.streetDataConverter = streetDataConverter;
	}

	/**
	 * Setter for property 'districtDataSource'.
	 *
	 * @param districtDataSource Value to set for property 'districtDataSource'.
	 */
	public void setDistrictDataSource(DistrictJdbcDataSource districtDataSource) {
		this.districtDataSource = districtDataSource;
	}

	/**
	 * Setter for property 'streetDataSource'.
	 *
	 * @param streetDataSource Value to set for property 'streetDataSource'.
	 */
	public void setStreetDataSource(StreetJdbcDataSource streetDataSource) {
		this.streetDataSource = streetDataSource;
	}

	/**
	 * Setter for property 'streetService'.
	 *
	 * @param streetService Value to set for property 'streetService'.
	 */
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	/**
	 * Setter for property 'correctionsService'.
	 *
	 * @param correctionsService Value to set for property 'correctionsService'.
	 */
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	/**
	 * Setter for property 'districtService'.
	 *
	 * @param districtService Value to set for property 'districtService'.
	 */
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	/**
	 * Setter for property 'streetTypeDataConverter'.
	 *
	 * @param streetTypeDataConverter Value to set for property 'streetTypeDataConverter'.
	 */
	public void setStreetTypeDataConverter(RawStreetTypeDataConverter streetTypeDataConverter) {
		this.streetTypeDataConverter = streetTypeDataConverter;
	}

	/**
	 * Setter for property 'streetTypeDataSource'.
	 *
	 * @param streetTypeDataSource Value to set for property 'streetTypeDataSource'.
	 */
	public void setStreetTypeDataSource(StreetTypeJdbcDataSource streetTypeDataSource) {
		this.streetTypeDataSource = streetTypeDataSource;
	}

	public void setBuildingsDataSource(BuildingsJdbcDataSource buildingsDataSource) {
		this.buildingsDataSource = buildingsDataSource;
	}

	/**
	 * Setter for property 'streetTypeService'.
	 *
	 * @param streetTypeService Value to set for property 'streetTypeService'.
	 */
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	public void setBuildingsDataConverter(RawBuildingsDataConverter buildingsDataConverter) {
		this.buildingsDataConverter = buildingsDataConverter;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	public void setAllObjectsDao(AllObjectsDao allObjectsDao) {
		this.allObjectsDao = allObjectsDao;
	}
}
