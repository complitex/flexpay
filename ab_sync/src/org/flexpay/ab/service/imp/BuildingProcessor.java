package org.flexpay.ab.service.imp;

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

public class BuildingProcessor extends AbstractProcessor<BuildingAddress> {

	public BuildingProcessor() {
		super(BuildingAddress.class);
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
	protected BuildingAddress doCreateObject() throws Exception {

		Building building = new Building();

		BuildingAddress buildingAddress = new BuildingAddress();
		buildingAddress.setBuilding(building);

		Set<BuildingAddress> buildingses = new HashSet<BuildingAddress>();
		buildingses.add(buildingAddress);
		building.addAll(buildingses);

		return buildingAddress;
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected BuildingAddress readObject(@NotNull Stub<BuildingAddress> stub) {
		BuildingAddress buildingAddress = buildingsDao.readFull(stub.getId());
		Set<BuildingAddress> buildingses = new HashSet<BuildingAddress>();
		buildingses.add(buildingAddress);

		Building building = buildingDao.read(buildingAddress.getBuilding().getId());
		buildingAddress.setBuilding(building);
		building.addAll(buildingses);

		log.debug("Read building: {}", building);

		return buildingAddress;
	}

	/**
	 * Save DomainObject
	 *
	 * @param object	 Object to save
	 * @param externalId External object identifier
	 */
	protected void doSaveObject(BuildingAddress object, String externalId) {
		Building building = object.getBuilding();
		if (object.getId() == null) {
			buildingDao.create(building);
			log.debug("Created building: {}", building);
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
		BuildingAddress buildingAddress = (BuildingAddress) object;
		switch (record.getFieldType()) {
			case DistrictId:
				setDistrictId(buildingAddress.getBuilding(), record, sd, cs);
				break;
			case StreetId:
				setStreetId(buildingAddress, record, sd, cs);
				break;
			case HouseNumber:
				setBuildingNumber(buildingAddress, record);
				break;
			case Bulk:
				setBuildingBulk(buildingAddress, record);
				break;
			default:
				log.debug("Unknown building property type: {}", record.getFieldType());
		}
	}

	private void setBuildingNumber(BuildingAddress buildingAddress, HistoryRecord record)
			throws Exception {

		log.info("Setting building number");

		String value = buildingAddress.getNumber();
		if (value != null && !value.equals(record.getCurrentValue())) {
			log.warn("Building renumber, history loss");
		}

		buildingAddress.setBuildingAttribute(record.getCurrentValue(), ApplicationConfig.getBuildingAttributeTypeNumber());

		log.info("Building number to set: {}, actual number: {}", record.getCurrentValue(), buildingAddress.getNumber());
	}

	private void setBuildingBulk(BuildingAddress buildingAddress, HistoryRecord record)
			throws Exception {

		String value = buildingAddress.getBulk();
		if (value != null && !value.equals(record.getCurrentValue())) {
			log.warn("Building bulk renumber, history loss. Object id: {}", record.getObjectId());
		}

		buildingAddress.setBuildingAttribute(record.getCurrentValue(), ApplicationConfig.getBuildingAttributeTypeBulk());

		log.info("Building bulk to set: {}, actual value: {}", record.getCurrentValue(), buildingAddress.getBulk());
	}

	private void setDistrictId(Building building, HistoryRecord record,
							   DataSourceDescription sd, CorrectionsService cs) {
		Stub<District> stub = cs.findCorrection(record.getCurrentValue(), District.class, sd);
		if (stub == null) {
			log.error("No correction for district #{} DataSourceDescription {}, cannot set up reference for building",
					record.getCurrentValue(), sd.getId());
			return;
		}

		if (districtDao.read(stub.getId()) == null) {
			log.error("Correction for district #{} DataSourceDescription {} is invalid, " +
					"no district with id {}, cannot set up reference for building",
					new Object[] {record.getCurrentValue(), sd.getId(), stub.getId()});
			return;
		}

		building.setDistrict(new District(stub));
	}

	private void setStreetId(BuildingAddress building, HistoryRecord record,
							 DataSourceDescription sd, CorrectionsService cs) {
		Stub<Street> stub = cs.findCorrection(record.getCurrentValue(), Street.class, sd);
		if (stub == null) {
			log.error("No correction for street #{} DataSourceDescription {}, cannot set up reference for building",
					record.getCurrentValue(), sd.getId());
			return;
		}

		if (streetDao.read(stub.getId()) == null) {
			log.error("Correction for street #{} DataSourceDescription {} is invalid, " +
					"no street with id {}, cannot set up reference for building",
					new Object[] {record.getCurrentValue(), sd.getId(), stub.getId()});
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
	protected Stub<BuildingAddress> findPersistentObject(BuildingAddress object,
												   DataSourceDescription sd, CorrectionsService cs) throws FlexPayException {
		String number = object.getNumber();
		String bulk = object.getBulk();
		if (number == null) {
			return null;
		}

		BuildingAddress buildingAddress = buildingService.findBuildings(stub(object.getStreet()), number, bulk);
		return buildingAddress != null ? stub(buildingAddress) : null;
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
