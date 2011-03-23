package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ApartmentNumber;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.TemporalObjectsHistoryBuildHelper;
import org.flexpay.common.persistence.history.TemporalObjectsHistoryBuildHelper.TemporalDataExtractor;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class ApartmentHistoryBuilder extends HistoryBuilderBase<Apartment> {

	public static final int FIELD_BUILDING_ID = 1;
	public static final int FIELD_NUMBER_VALUE = 2;

	protected Logger log = LoggerFactory.getLogger(getClass());

	private ObjectsFactory factory;

	/**
	 * Build necessary diff records
	 *
	 * @param a1   First object
	 * @param a2   Second object
	 * @param diff Diff object
	 */
	@Override
	protected void doDiff(@Nullable Apartment a1, @NotNull Apartment a2, @NotNull Diff diff) {

		log.debug("creating new apartments diff");

		Apartment apartmentOld = a1 == null ? factory.newApartment() : a1;

		buildBuildingReferenceDiff(apartmentOld, a2, diff);

		buildAddressDiff(apartmentOld, a2, diff);
	}

	private void buildAddressDiff(@NotNull Apartment a1, @NotNull Apartment a2, @NotNull Diff diff) {

		// compare sorted by begin date apartment numbers and build according diffs if found
		List<ApartmentNumber> addresses1 = list(a1.getApartmentNumbers());
		Collections.sort(addresses1);
		List<ApartmentNumber> addresses2 = list(a2.getApartmentNumbers());
		Collections.sort(addresses2);

		TemporalObjectsHistoryBuildHelper.buildDiff(new TemporalDataExtractor<ApartmentNumber>() {
			@Override
			public Date getBeginDate(ApartmentNumber obj) {
				return obj.getBegin();
			}

			@Override
			public Date getEndDate(ApartmentNumber obj) {
				return obj.getEnd();
			}

			@Override
			public void buildDiff(Date begin, Date end, ApartmentNumber t1, ApartmentNumber t2, Diff df) {
				addAddressDiff(begin, end, t1, t2, df);
			}
		}, addresses1, addresses2, diff);
	}

	private void addAddressDiff(Date begin, Date end, ApartmentNumber n1, ApartmentNumber n2, Diff diff) {

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_NUMBER_VALUE);
		rec.setBeginDate(begin);
		rec.setEndDate(end);
		rec.setOldStringValue(n1 != null ? n1.getValue() : null);
		rec.setNewStringValue(n2 != null ? n2.getValue() : null);
		diff.addRecord(rec);

		log.debug("Address diff {}", rec);
	}

	private boolean isNew(DomainObject object) {
		Long id = object.getId();
		return id == null || id <= 0;
	}

	private boolean isNotNew(DomainObject object) {
		return !isNew(object);
	}

	private void buildBuildingReferenceDiff(@NotNull Apartment a1, @NotNull Apartment a2, @NotNull Diff diff) {

		Building b1 = a1.getBuilding();
		Building b2 = a2.getBuilding();
		boolean noParent = (b1 == null || isNew(b1)) && (b2 == null || isNew(b2));

		// no parent found in both objects, nothing to do
		if (noParent) {
			return;
		}

		boolean sameParent = b1 != null && isNotNew(b1) &&
							 b2 != null && isNotNew(b2) &&
							 b1.equals(b2);
		// same parent found in both objects, nothing to do
		if (sameParent) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_BUILDING_ID);
		rec.setOldStringValue(
				b1 == null || isNew(b1) ?
				null :
				masterIndexService.getMasterIndex(b1));
		rec.setNewStringValue(
				b2 == null || isNew(b2) ?
				null :
				masterIndexService.getMasterIndex(b2));
		diff.addRecord(rec);

		log.debug("Building ref diff {}", rec);
	}

	/**
	 * Apply diff to an object
	 *
	 * @param apartment Object to apply diff to
	 * @param diff	  Diff to apply
	 */
	@Override
	public void patch(@NotNull Apartment apartment, @NotNull Diff diff) {
		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_BUILDING_ID:
					patchBuildingReference(apartment, record);
					break;
				case FIELD_NUMBER_VALUE:
					patchAddress(apartment, record);
					break;
				default:
					log.warn("Unsupported field type {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}
	}

	private void patchAddress(@NotNull Apartment apartment, @NotNull HistoryRecord record) {

		log.debug("Patching number {}", record);
		apartment.setNumberForDates(record.getNewStringValue(), record.getBeginDate(), record.getEndDate());
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchBuildingReference(@NotNull Apartment apartment, @NotNull HistoryRecord record) {
		log.debug("Patching building reference {}", record);

		Building building = null;
		if (record.getNewStringValue() != null) {
			String externalId = record.getNewStringValue();
			Stub<Building> stub = correctionsService.findCorrection(
					externalId, Building.class, masterIndexService.getMasterSourceDescriptionStub());
			if (stub == null) {
				throw new IllegalStateException("Cannot find building by master index: " + externalId);
			}
			building = new Building(stub);
		}

		apartment.setBuilding(building);
		log.debug("Patched building reference {}", apartment.getBuilding());
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	@Required
	public void setFactory(ObjectsFactory factory) {
		this.factory = factory;
	}

}
