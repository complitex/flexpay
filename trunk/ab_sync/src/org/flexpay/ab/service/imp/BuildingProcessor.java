package org.flexpay.ab.service.imp;

import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.CorrectionsService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	protected Buildings doCreateObject() throws Exception {

		Building building = new Building();

		Buildings buildings = new Buildings();
		buildings.setBuilding(building);

		Set<Buildings> buildingses = new HashSet<Buildings>();
		building.setBuildingses(buildingses);

		return buildings;
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected Buildings readObject(Buildings stub) {
		Buildings buildings = buildingsDao.read(stub.getId());
		Set<Buildings> buildingses = new HashSet<Buildings>();
		buildingses.add(buildings);

		Building buildingStub = new Building(buildings.getBuilding().getId());
		buildings.setBuilding(buildingStub);
		buildingStub.setBuildingses(buildingses);

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
	public void setProperty(DomainObject object, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs)
			throws Exception {
		Buildings buildings = (Buildings) object;
		if (PROP_DISTRICT_ID.equals(record.getFieldName())) {
			setDistrictId(buildings.getBuilding(), record, sd, cs);
		} else if (PROP_STREET_ID.equals(record.getFieldName())) {
			setStreetId(buildings, record, sd, cs);
		} else if (PROP_BUILDING_NUMBER.equals(record.getFieldName())) {
			setBuildingNumber(buildings, record);
		} else if (PROP_BUILDING_BULK.equals(record.getFieldName())) {
			setBuildingBulk(buildings, record);
		}
	}

	private void setBuildingNumber(Buildings buildings, HistoryRecord record) throws Exception {

		String value = buildings.getNumber();
		if (value != null && !value.equals(record.getCurrentValue())) {
			log.warn("Building renumber, history loss");
		}

		List<BuildingAttribute> attributes = buildings.getBuildingAttributes();
		if (attributes.isEmpty()) {
			// will save attributes at the end of the method
			attributes = new ArrayList<BuildingAttribute>();
		}

		// Check if number already exists
		boolean found = false;
		for (BuildingAttribute attribute : attributes) {
			if (attribute.getBuildingAttributeType().getType() == BuildingAttributeType.TYPE_NUMBER) {
				attribute.setValue(record.getCurrentValue());
				found = true;
				break;
			}
		}

		// Create a new attribute
		if (!found) {
			BuildingAttributeType type = buildingService.getAttributeType(BuildingAttributeType.TYPE_BULK);
			BuildingAttribute attribute = new BuildingAttribute();
			attribute.setValue(record.getCurrentValue());
			attribute.setBuildings(buildings);
			attribute.setBuildingAttributeType(type);
		}

		buildings.setBuildingAttributes(attributes);
	}

	private void setBuildingBulk(Buildings buildings, HistoryRecord record) throws Exception {

		String value = buildings.getBulk();
		if (value != null && !value.equals(record.getCurrentValue())) {
			log.warn("Building bulk renumber, history loss");
		}

		List<BuildingAttribute> attributes = buildings.getBuildingAttributes();
		if (attributes.isEmpty()) {
			// will save attributes at the end of the method
			attributes = new ArrayList<BuildingAttribute>();
		}

		// Check if bulk already exists
		boolean found = false;
		for (BuildingAttribute attribute : attributes) {
			if (attribute.getBuildingAttributeType().getType() == BuildingAttributeType.TYPE_BULK) {
				attribute.setValue(record.getCurrentValue());
				found = true;
				break;
			}
		}

		// Create a new attribute
		if (!found) {
			BuildingAttributeType type = buildingService.getAttributeType(BuildingAttributeType.TYPE_BULK);
			BuildingAttribute attribute = new BuildingAttribute();
			attribute.setValue(record.getCurrentValue());
			attribute.setBuildings(buildings);
			attribute.setBuildingAttributeType(type);
		}

		buildings.setBuildingAttributes(attributes);
	}

	private void setDistrictId(Building building, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs) {
		District stub = cs.findCorrection(record.getCurrentValue(), District.class, sd);
		if (stub == null) {
			throw new RuntimeException(String.format("No correction for district #%s DataSourceDescription %d, " +
					"cannot set up reference for building", record.getCurrentValue(), sd.getId()));
		}

		if (districtDao.read(stub.getId()) == null) {
			throw new RuntimeException(String.format("Correction for district #%s DataSourceDescription %d is invalid, " +
					"no district with id %d, cannot set up reference for building",
					record.getCurrentValue(), sd.getId(), stub.getId()));
		}

		building.setDistrict(stub);
	}

	private void setStreetId(Buildings building, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs) {
		Street stub = cs.findCorrection(record.getCurrentValue(), Street.class, sd);
		if (stub == null) {
			throw new RuntimeException(String.format("No correction for street #%s DataSourceDescription %d, " +
					"cannot set up reference for building", record.getCurrentValue(), sd.getId()));
		}

		if (streetDao.read(stub.getId()) == null) {
			throw new RuntimeException(String.format("Correction for street #%s DataSourceDescription %d is invalid, " +
					"no street with id %d, cannot set up reference for building",
					record.getCurrentValue(), sd.getId(), stub.getId()));
		}

		building.setStreet(stub);
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
