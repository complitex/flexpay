package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import static org.flexpay.common.util.CollectionUtils.ar;
import org.flexpay.common.util.EqualsHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class BuildingHistoryBuilder extends HistoryBuilderBase<Building> {

	public static final int FIELD_DISTRICT_ID = 1;
	public static final int FIELD_ADDRESS_VALUE = 2;
	public static final int FIELD_PRIMARY_ADDRESS = 3;
	public static final int FIELD_DELETED_ADDRESS = 4;

	protected Logger log = LoggerFactory.getLogger(getClass());

	private AddressAttributeTypeService addressAttributeTypeService;
	private ObjectsFactory factory;

	/**
	 * Build necessary diff records
	 *
	 * @param b1   First object
	 * @param b2   Second object
	 * @param diff Diff object
	 */
	protected void doDiff(@Nullable Building b1, @NotNull Building b2, @NotNull Diff diff) {

		log.debug("creating new buildings diff");

		if (!b2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		Building buildingOld = b1 == null ? factory.newBuilding() : b1;

		// create district reference diff
		buildDistrictReferenceDiff(buildingOld, b2, diff);

		buildAddressDiff(buildingOld, b2, diff);
	}

	private void buildAddressDiff(@NotNull Building b1, @NotNull Building b2, @NotNull Diff diff) {

		Set<Street> streets = b1.getStreets();
		streets.addAll(b2.getStreets());

		log.debug("Streets to build diff on: {}", streets);

		for (Street street : streets) {
			BuildingAddress addr1 = b1.getAddressOnStreet(street);
			BuildingAddress addr2 = b2.getAddressOnStreet(street);

			buildAddressOnStreetDiff(addr1, addr2, street, diff);
			buildPrimaryAddressDiff(addr1, addr2, diff, street);
		}
	}

	private void buildPrimaryAddressDiff(BuildingAddress addr1, BuildingAddress addr2, Diff diff, Street street) {

		// setup diff for primary status
		boolean prim1 = addr1 != null && addr1.isPrimary();
		boolean prim2 = addr2 != null && addr2.isPrimary();
		if (prim1 == prim2) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_PRIMARY_ADDRESS);
		rec.setOldBoolValue(prim1);
		rec.setNewBoolValue(prim2);
		rec.setFieldKey(masterIndexService.getMasterIndex(street));
		diff.addRecord(rec);
		log.debug("Added primary status diff record: {}", rec);
	}

	private void buildAddressOnStreetDiff(BuildingAddress addr1, BuildingAddress addr2, Street street, Diff diff) {

		if (log.isDebugEnabled()) {
			log.debug("Building address diff on street {}, addresses:\n{}\n{}", ar(street, addr1, addr2));
		}

		// first, build diffs by attribute types
		for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
			AddressAttribute attr1 = addr1 != null ? addr1.getAttribute(type) : null;
			AddressAttribute attr2 = addr2 != null ? addr2.getAttribute(type) : null;

			// check value
			boolean sameValue = EqualsHelper.strEquals(
					attr1 == null ? null : attr1.getValue(),
					attr2 == null ? null : attr2.getValue());
			if (sameValue) {
				continue;
			}

			HistoryRecord rec = new HistoryRecord();
			rec.setFieldType(FIELD_ADDRESS_VALUE);
			rec.setOldStringValue(attr1 == null ? null : attr1.getValue());
			rec.setNewStringValue(attr2 == null ? null : attr2.getValue());

			// field key is a street + field key 2 is a type
			rec.setFieldKey(masterIndexService.getMasterIndex(street));
			rec.setFieldKey2(masterIndexService.getMasterIndex(type));
			diff.addRecord(rec);
			log.debug("Added address diff record: {}", rec);
		}

		// check deleted status
		boolean addr1Act = addr1 != null && addr1.isActive();
		boolean addr2Act = addr2 != null && addr2.isActive();
		boolean sameActiveStatus = addr1Act == addr2Act;
		if (sameActiveStatus || (addr1 == null && addr2Act)) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_DELETED_ADDRESS);
		rec.setOldBoolValue(addr1Act);
		rec.setNewBoolValue(addr2Act);

		// field key is a street
		rec.setFieldKey(masterIndexService.getMasterIndex(street));
		diff.addRecord(rec);
		log.debug("Added address deleted record: {}", rec);
	}

	private void buildDistrictReferenceDiff(@NotNull Building b1, @NotNull Building b2, @NotNull Diff diff) {

		boolean noParent = (b1.getDistrict() == null || b1.getDistrict().isNew()) &&
						   (b2.getDistrict() == null || b2.getDistrict().isNew());

		// no parent found in both objects, nothing to do
		if (noParent) {
			return;
		}

		boolean sameParent = b1.getDistrict() != null && b1.getDistrict().isNotNew() &&
							 b2.getDistrict() != null && b2.getDistrict().isNotNew() &&
							 b1.getDistrictStub().equals(b2.getDistrictStub());
		// same parent found in both objects, nothing to do
		if (sameParent) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_DISTRICT_ID);
		rec.setOldStringValue(
				b1.getDistrict() == null || b1.getDistrict().isNew() ?
				null :
				masterIndexService.getMasterIndex(b1.getDistrict()));
		rec.setNewStringValue(
				b2.getDistrict() == null || b2.getDistrict().isNew() ?
				null :
				masterIndexService.getMasterIndex(b2.getDistrict()));
		diff.addRecord(rec);
		log.debug("Added district ref diff record: {}", rec);
	}

	/**
	 * Apply diff to an object
	 *
	 * @param building Object to apply diff to
	 * @param diff	 Diff to apply
	 */
	public void patch(@NotNull Building building, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_DISTRICT_ID:
					patchDistrictReference(building, record);
					break;
				case FIELD_ADDRESS_VALUE:
					patchAddress(building, record);
					break;
				case FIELD_PRIMARY_ADDRESS:
					patchPrimaryStatus(building, record);
					break;
				case FIELD_DELETED_ADDRESS:
					patchDeletedAddress(building, record);
					break;
				default:
					log.warn("Unsupported field type {}", record);
			}
		}
	}

	private void patchDeletedAddress(@NotNull Building building, @NotNull HistoryRecord record) {

		// find street
		BuildingAddress addr = findStreetAddress(building, record.getFieldKey());
		Boolean active = record.getNewBoolValue();
		if (addr != null && active != null) {
			if (active) {
				addr.activate();
			} else {
				addr.disable();
			}
		}

		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchPrimaryStatus(@NotNull Building building, @NotNull HistoryRecord record) {

		log.debug("Patching primary status: {}", record);

		BuildingAddress addr = findStreetAddress(building, record.getFieldKey());
		Boolean value = record.getNewBoolValue();
		boolean isPrimary = value != null ? value : false;

		addr.setPrimaryStatus(isPrimary);
		building.addAddress(addr);

		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private BuildingAddress findStreetAddress(Building building, String streetId) {

		// find street
		Stub<Street> streetStub = correctionsService.findCorrection(
				streetId, Street.class, masterIndexService.getMasterSourceDescription());
		if (streetStub == null) {
			throw new IllegalStateException("Cannot find street by master index: " + streetId);
		}
		Street street = new Street(streetStub);

		// find address on street
		BuildingAddress addr = building.getAddressOnStreet(street);
		if (addr == null) {
			addr = new BuildingAddress();
			addr.setStreet(street);
		}

		return addr;
	}

	private void patchAddress(@NotNull Building building, @NotNull HistoryRecord record) {

		// split street-id and type-id
		String streetMasterIndex = record.getFieldKey();
		String typeMasterIndex = record.getFieldKey2();

		BuildingAddress addr = findStreetAddress(building, streetMasterIndex);

		// find attribute type
		Stub<AddressAttributeType> typeStub = correctionsService.findCorrection(
				typeMasterIndex, AddressAttributeType.class, masterIndexService.getMasterSourceDescription());
		if (typeStub == null) {
			throw new IllegalStateException("Cannot find address attribute type by master index: " + typeMasterIndex);
		}

		// setup address value and add address to building
		AddressAttributeType type = new AddressAttributeType(typeStub);
		addr.setBuildingAttribute(record.getNewStringValue(), type);

		building.addAddress(addr);

		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchDistrictReference(@NotNull Building building, @NotNull HistoryRecord record) {
		log.debug("Patching district reference {}", record);

		District district = null;
		if (record.getNewStringValue() != null) {
			String externalId = record.getNewStringValue();
			Stub<District> stub = correctionsService.findCorrection(
					externalId, District.class, masterIndexService.getMasterSourceDescription());
			if (stub == null) {
				throw new IllegalStateException("Cannot find district by master index: " + externalId);
			}
			district = new District(stub);
		}

		building.setDistrict(district);
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	@Required
	public void setAddressAttributeTypeService(AddressAttributeTypeService addressAttributeTypeService) {
		this.addressAttributeTypeService = addressAttributeTypeService;
	}

	@Required
	public void setFactory(ObjectsFactory factory) {
		this.factory = factory;
	}
}
