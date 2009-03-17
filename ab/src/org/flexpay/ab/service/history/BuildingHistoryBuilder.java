package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
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

	protected Logger log = LoggerFactory.getLogger(getClass());

	private AddressAttributeTypeService addressAttributeTypeService;

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

		Building buildingOld = b1 == null ? new Building() : b1;

		// create town reference diff
		buildDistrictReferenceDiff(buildingOld, b2, diff);

		buildAddressDiff(buildingOld, b2, diff);
	}

	private void buildAddressDiff(@NotNull Building b1, @NotNull Building b2, @NotNull Diff diff) {

		Set<Street> streets = b1.getStreets();
		streets.addAll(b2.getStreets());

		for (Street street : streets) {
			BuildingAddress addr1 = b1.getAddressOnStreet(street);
			BuildingAddress addr2 = b2.getAddressOnStreet(street);

			buildAddressOnStreetDiff(addr1, addr2, street, diff);
			buildPrimaryAddressDiff(addr1, addr2, diff, street);

		}
	}

	private void buildPrimaryAddressDiff(BuildingAddress addr1, BuildingAddress addr2, Diff diff, Street street) {
		// second, setup diff for primary status
		boolean statusSame = addr1 == null && addr2 == null;
		statusSame = statusSame || (addr1 == null && addr2.isNotPrimary());
		statusSame = statusSame || (addr2 == null && addr1.isNotPrimary());
		statusSame = statusSame || (addr1 != null && addr1.isPrimary() && addr2 != null && addr2.isPrimary());
		if (statusSame) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_PRIMARY_ADDRESS);
		rec.setOldBoolValue(addr1 == null ? null : addr1.isPrimary());
		rec.setNewBoolValue(addr2 == null ? null : addr2.isPrimary());
		rec.setFieldKey(masterIndexService.getMasterIndex(street));
		diff.addRecord(rec);
	}

	private void buildAddressOnStreetDiff(BuildingAddress addr1, BuildingAddress addr2, Street street, Diff diff) {
		// first, build diffs by attribute types
		for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
			AddressAttribute attr1 = addr1 != null ? addr1.getAttribute(type) : null;
			AddressAttribute attr2 = addr2 != null ? addr2.getAttribute(type) : null;

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

			// small trick, field key is a street + "|" + type to later restore references
			rec.setFieldKey(
					masterIndexService.getMasterIndex(street) +
					"|" +
					masterIndexService.getMasterIndex(type));
			diff.addRecord(rec);
		}
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
				default:
					log.warn("Unsupported field type {}", record);
			}
		}
	}

	private void patchDistrictReference(@NotNull Building building, @NotNull HistoryRecord record) {
		log.debug("Patching district reference {}", record);

		District district = null;
		if (record.getNewStringValue() != null) {
			String externalId = record.getNewStringValue();
			Stub<District> stub = correctionsService.findCorrection(
					externalId, District.class, masterIndexService.getMasterSourceDescription());
			if (stub == null) {
				throw new IllegalStateException("Cannot find district type by master index: " + externalId);
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
}
