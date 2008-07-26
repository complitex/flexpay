package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class BuildingProcessor extends AbstractProcessor<Buildings> {

	public BuildingProcessor() {
		super(Buildings.class);
	}

	private BuildingDao buildingDao;
	private BuildingsDao buildingsDao;
	private DistrictDao districtDao;
	private StreetDao streetDao;
	private BuildingService buildingService;

	/**
	 * Create new DomainObject from HistoryRecord
	 */
	@NotNull
	protected Buildings doCreateObject() throws Exception {

		Building building = new Building();

		Buildings buildings = new Buildings();
		buildings.setBuilding(building);

		Set<Buildings> buildingses = new HashSet<Buildings>();
		buildingses.add(buildings);
		building.setBuildingses(buildingses);

		return buildings;
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected Buildings readObject(@NotNull Stub<Buildings> stub) {
		Buildings buildings = buildingsDao.readFull(stub.getId());
		Set<Buildings> buildingses = new HashSet<Buildings>();
		buildingses.add(buildings);

		Building building = buildingDao.read(buildings.getBuilding().getId());
		buildings.setBuilding(building);
		building.setBuildingses(buildingses);

		if (log.isDebugEnabled()) {
			log.debug("Read building: " + building);
		}

		return buildings;
	}

	/**
	 * Save DomainObject
	 *
	 * @param object Object to save
	 */
	protected void doSaveObject(Buildings object) {
		Building building = object.getBuilding();
		if (object.getId() == null) {
			buildingDao.create(building);

			if (log.isDebugEnabled()) {
				log.debug(String.format("Created building: %s", building
						.toString()));
			}
		} else {
			buildingDao.update(building);
		}
	}

	/**
	 * Update DomainObject from HistoryRecord
	 *
	 * @param object DomainObject to set properties on
	 * @param record HistoryRecord
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 * @throws Exception if failure occurs
	 */
	public void setProperty(@NotNull DomainObject object, @NotNull HistoryRecord record,
							DataSourceDescription sd, CorrectionsService cs) throws Exception {
		Buildings buildings = (Buildings) object;
		switch (record.getFieldType()) {
			case DistrictId:
				setDistrictId(buildings.getBuilding(), record, sd, cs);
				break;
			case StreetId:
				setStreetId(buildings, record, sd, cs);
				break;
			case HouseNumber:
				setBuildingNumber(buildings, record);
				break;
			case Bulk:
				setBuildingBulk(buildings, record);
				break;
			default:
				if (log.isDebugEnabled()) {
					log
							.debug("Unknown building property: "
								   + record.getFieldType());
				}
		}
	}

	private void setBuildingNumber(Buildings buildings, HistoryRecord record)
			throws Exception {

		log.info("Setting building number");

		String value = buildings.getNumber();
		if (value != null && !value.equals(record.getCurrentValue())) {
			log.warn("Building renumber, history loss");
		}

		Set<BuildingAttribute> attributes = buildings.getBuildingAttributes();
		if (attributes.isEmpty()) {
			// will save attributes at the end of the method
			attributes = new HashSet<BuildingAttribute>();
		}

		// Check if number already exists
		BuildingAttribute numberAttribute = null;
		for (BuildingAttribute attribute : attributes) {
			if (attribute == null) {
				continue;
			}
			if (attribute.getBuildingAttributeType().isBuildingNumber()) {
				numberAttribute = attribute;
				break;
			}
		}

		if (StringUtils.isEmpty(record.getCurrentValue())) {
			if (numberAttribute != null) {
				attributes.remove(numberAttribute);
				buildings.setBuildingAttributes(attributes);
			}
			return;
		}

		// Create a new attribute
		if (numberAttribute == null) {
			BuildingAttributeType type = ApplicationConfig.getBuildingAttributeTypeNumber();
			BuildingAttribute attribute = new BuildingAttribute();
			attribute.setBuildings(buildings);
			attribute.setBuildingAttributeType(type);
			numberAttribute = attribute;
		}

		numberAttribute.setValue(record.getCurrentValue());
		attributes.add(numberAttribute);

		buildings.setBuildingAttributes(attributes);

		if (log.isInfoEnabled()) {
			log.info(String.format(
					"Building number to set: %s, actual number: %s", record
					.getCurrentValue(), buildings.getNumber()));
		}
	}

	private void setBuildingBulk(Buildings buildings, HistoryRecord record)
			throws Exception {

		String value = buildings.getBulk();
		if (value != null && !value.equals(record.getCurrentValue())) {
			log.warn("Building bulk renumber, history loss");
		}

		Set<BuildingAttribute> attributes = buildings.getBuildingAttributes();
		if (attributes.isEmpty()) {
			// will save attributes at the end of the method
			attributes = new HashSet<BuildingAttribute>();
		}

		// Check if bulk already exists
		BuildingAttribute numberAttribute = null;
		for (BuildingAttribute attribute : attributes) {
			if (attribute == null) {
				continue;
			}
			if (attribute.getBuildingAttributeType().isBulkNumber()) {
				numberAttribute = attribute;
				break;
			}
		}

		if (StringUtils.isEmpty(record.getCurrentValue())) {
			if (numberAttribute != null) {
				attributes.remove(numberAttribute);
				buildings.setBuildingAttributes(attributes);
			}
			return;
		}

		// Create a new attribute
		if (numberAttribute == null) {
			BuildingAttributeType type = ApplicationConfig.getBuildingAttributeTypeBulk();
			BuildingAttribute attribute = new BuildingAttribute();
			attribute.setBuildings(buildings);
			attribute.setBuildingAttributeType(type);
			numberAttribute = attribute;
		}

		numberAttribute.setValue(record.getCurrentValue());
		attributes.add(numberAttribute);

		buildings.setBuildingAttributes(attributes);
	}

	private void setDistrictId(Building building, HistoryRecord record,
							   DataSourceDescription sd, CorrectionsService cs) {
		Stub<District> stub = cs.findCorrection(record.getCurrentValue(),
				District.class, sd);
		if (stub == null) {
			log.error(String.format(
					"No correction for district #%s DataSourceDescription %d, "
					+ "cannot set up reference for building", record
					.getCurrentValue(), sd.getId()));
			return;
		}

		if (districtDao.read(stub.getId()) == null) {
			log
					.error(String
							.format(
							"Correction for district #%s DataSourceDescription %d is invalid, "
							+ "no district with id %d, cannot set up reference for building",
							record.getCurrentValue(), sd.getId(), stub
							.getId()));
			return;
		}

		building.setDistrict(new District(stub));
	}

	private void setStreetId(Buildings building, HistoryRecord record,
							 DataSourceDescription sd, CorrectionsService cs) {
		Stub<Street> stub = cs.findCorrection(record.getCurrentValue(), Street.class, sd);
		if (stub == null) {
			log.error(String.format(
					"No correction for street #%s DataSourceDescription %d, "
					+ "cannot set up reference for building", record
					.getCurrentValue(), sd.getId()));
			return;
		}

		if (streetDao.read(stub.getId()) == null) {
			log.error(String.format(
					"Correction for street #%s DataSourceDescription %d is invalid, "
					+ "no street with id %d, cannot set up reference for building",
					record.getCurrentValue(), sd.getId(), stub.getId()));
			return;
		}

		building.setStreet(new Street(stub));
	}

	/**
	 * Try to find persistent object by set properties
	 *
	 * @param object DomainObject
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 * @return Persistent object stub if exists, or <code>null</code> otherwise
	 */
	protected Stub<Buildings> findPersistentObject(Buildings object,
											 DataSourceDescription sd, CorrectionsService cs) throws FlexPayException {
		String number = object.getNumber();
		String bulk = object.getBulk();
		if (number == null) {
			return null;
		}

		Buildings buildings = buildingService.findBuildings(stub(object.getStreet()), number, bulk);
		return buildings != null ? stub(buildings) : null;
	}

	public void setBuildingDao(BuildingDao buildingDao) {
		this.buildingDao = buildingDao;
	}

	public void setBuildingsDao(BuildingsDao buildingsDao) {
		this.buildingsDao = buildingsDao;
	}

	public void setDistrictDao(DistrictDao districtDao) {
		this.districtDao = districtDao;
	}

	public void setStreetDao(StreetDao streetDao) {
		this.streetDao = streetDao;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}
