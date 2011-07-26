package org.flexpay.ab.service.impl;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class BuildingProcessor extends AbstractProcessor<BuildingAddress> {

	public BuildingProcessor() {
		super(BuildingAddress.class);
	}

	private BuildingService buildingService;
	private StreetService streetService;
	private DistrictService districtService;
	private ObjectsFactory factory;

	/**
	 * Create new DomainObject from HistoryRecord
	 */
	@NotNull
    @Override
	protected BuildingAddress doCreateObject() throws Exception {

		Building building = factory.newBuilding();
		BuildingAddress buildingAddress = new BuildingAddress();
		building.addAddress(buildingAddress);

		return buildingAddress;
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	@Nullable
    @Override
	protected BuildingAddress readObject(@NotNull Stub<BuildingAddress> stub) {

		BuildingAddress buildingAddress = buildingService.readFullAddress(stub);
		if (buildingAddress == null) {
			return null;
		}
		Building building = buildingService.readFull(buildingAddress.getBuildingStub());

		log.debug("Read building: {}", building);

		return building.getAddress(stub);
	}

	/**
	 * Save DomainObject
	 *
	 * @param object	 Object to save
	 * @param externalId External object identifier
	 */
    @Override
	protected void doSaveObject(BuildingAddress object, String externalId) throws FlexPayExceptionContainer {
		Building building = object.getBuilding();
		if (object.getId() == null) {
			buildingService.create(building);
			log.debug("Created building: {}", building);
		} else {
			buildingService.update(building);
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
    @Override
	public void setProperty(@NotNull DomainObject object, @NotNull HistoryRec record,
							Stub<DataSourceDescription> sd, CorrectionsService cs) throws Exception {
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

	private void setBuildingNumber(BuildingAddress buildingAddress, HistoryRec record)
			throws Exception {

		log.info("Setting building number");

		String value = buildingAddress.getNumber();
		if (value != null && !value.equals(record.getCurrentValue())) {
			log.warn("Building renumber, history loss");
		}

		buildingAddress.setBuildingAttribute(record.getCurrentValue(), ApplicationConfig.getBuildingAttributeTypeNumber());

		log.info("Building number to set: {}, actual number: {}", record.getCurrentValue(), buildingAddress.getNumber());
	}

	private void setBuildingBulk(BuildingAddress buildingAddress, HistoryRec record)
			throws Exception {

		String value = buildingAddress.getBulk();
		if (value != null && !value.equals(record.getCurrentValue())) {
			log.warn("Building bulk renumber, history loss. Object id: {}", record.getObjectId());
		}

		buildingAddress.setBuildingAttribute(record.getCurrentValue(), ApplicationConfig.getBuildingAttributeTypeBulk());

		log.info("Building bulk to set: {}, actual value: {}", record.getCurrentValue(), buildingAddress.getBulk());
	}

	private void setDistrictId(Building building, HistoryRec record,
							   Stub<DataSourceDescription> sd, CorrectionsService cs) {
		Stub<District> stub = cs.findCorrection(record.getCurrentValue(), District.class, sd);
		if (stub == null) {
			log.error("No correction for district #{} DataSourceDescription {}, cannot set up reference for building",
					record.getCurrentValue(), sd.getId());
			return;
		}

		District district = districtService.readFull(stub);
		if (district == null) {
			log.error("Correction for district #{} DataSourceDescription {} is invalid, " +
					  "no district with id {}, cannot set up reference for building",
					new Object[]{record.getCurrentValue(), sd.getId(), stub.getId()});
			return;
		}

		log.debug("Set district");
		building.setDistrict(district);
	}

	private void setStreetId(BuildingAddress building, HistoryRec record,
							 Stub<DataSourceDescription> sd, CorrectionsService cs) {
		Stub<Street> stub = cs.findCorrection(record.getCurrentValue(), Street.class, sd);
		if (stub == null) {
			log.error("No correction for street #{} DataSourceDescription {}, cannot set up reference for building",
					record.getCurrentValue(), sd.getId());
			return;
		}

		Street street = streetService.readFull(stub);
		if (street == null) {
			log.error("Correction for street #{} DataSourceDescription {} is invalid, " +
					  "no street with id {}, cannot set up reference for building",
					new Object[]{record.getCurrentValue(), sd.getId(), stub.getId()});
			return;
		}

		log.debug("Set street");
		building.setStreet(street);
	}

	/**
	 * Try to find persistent object by set properties
	 *
	 * @param object DomainObject
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 * @return Persistent object stub if exists, or <code>null</code> otherwise
	 */
    @Override
	protected Stub<BuildingAddress> findPersistentObject(BuildingAddress object,
														 Stub<DataSourceDescription> sd, CorrectionsService cs) throws FlexPayException {
		String number = object.getNumber();
		String bulk = object.getBulk();
		if (number == null) {
			return null;
		}

		BuildingAddress buildingAddress = buildingService.findAddresses(
				object.getStreetStub(), buildingService.attributes(number, bulk));
		return buildingAddress != null ? stub(buildingAddress) : null;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setFactory(ObjectsFactory factory) {
		this.factory = factory;
	}
}
