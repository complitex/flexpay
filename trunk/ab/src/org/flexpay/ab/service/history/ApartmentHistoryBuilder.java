package org.flexpay.ab.service.history;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ApartmentNumber;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.treeSet;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Collections.max;
import static java.util.Collections.min;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;

public class ApartmentHistoryBuilder extends HistoryBuilderBase<Apartment> {

	public static final int FIELD_BUILDING_ID = 1;
	public static final int FIELD_NUMBER_VALUE = 2;

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Build necessary diff records
	 *
	 * @param a1   First object
	 * @param a2   Second object
	 * @param diff Diff object
	 */
	protected void doDiff(@Nullable Apartment a1, @NotNull Apartment a2, @NotNull Diff diff) {

		log.debug("creating new buildings diff");

		Apartment apartmentOld = a1 == null ? new Apartment() : a2;

		buildBuildingReferenceDiff(apartmentOld, a2, diff);

		buildAddressDiff(apartmentOld, a2, diff);
	}

	private void buildAddressDiff(@NotNull Apartment a1, @NotNull Apartment a2, @NotNull Diff diff) {

		// compare sorted by begin date apartment numbers and build according diffs if found
		SortedSet<ApartmentNumber> addresses1 = treeSet(a1.getApartmentNumbers());
		SortedSet<ApartmentNumber> addresses2 = treeSet(a2.getApartmentNumbers());
		Iterator<ApartmentNumber> it1 = addresses1.iterator();
		Iterator<ApartmentNumber> it2 = addresses2.iterator();

		Date cursor = ApplicationConfig.getPastInfinite();
		ApartmentNumber n1 = null;
		ApartmentNumber n2 = null;

		while (it1.hasNext() || it2.hasNext()) {
			n1 = n1 == null && it1.hasNext() ? it1.next() : n1;
			n2 = n2 == null && it2.hasNext() ? it2.next() : n2;

			// setup next intervals boundaries
			Date begin1 = n1 != null ? n1.getBegin() : ApplicationConfig.getFutureInfinite();
			Date begin2 = n2 != null ? n2.getBegin() : ApplicationConfig.getFutureInfinite();
			Date end1 = n1 != null ? n1.getEnd() : ApplicationConfig.getFutureInfinite();
			Date end2 = n2 != null ? n2.getEnd() : ApplicationConfig.getFutureInfinite();

			// setup lower and upper bound for a next pair of intervals to build diffs on
			Date beginMin = min(list(begin1, begin2));
			Date beginMax = max(list(begin1, begin2));
			Date end = min(list(end1, end2));
			Date beginLower = min(list(beginMax, end));

			// add diff in interval from cursor to min among begins
			addAddressDiff(cursor, beginMin, n1, n2, diff);

			// set cursor to next point - first begin, or cursor if it was after begin
			cursor = max(list(beginMin, cursor));
			// now add diff from cursor to lower value of bigger begin and two ends
			addAddressDiff(cursor, beginLower, n1, n2, diff);

			cursor = beginLower;
			// if not reached any of ends, add diff from max begin to min end
			if (cursor.before(end)) {
				addAddressDiff(cursor, end, n1, n2, diff);
				cursor = end;
			}

			// if the first end was reached - fetch next value
			if (cursor.compareTo(end1) >= 0) {
				n1 = null;
			}
			// if the second end was reached - fetch next value
			if (cursor.compareTo(end2) >= 0) {
				n2 = null;
			}
		}
	}

	private void addAddressDiff(Date begin, Date end, ApartmentNumber n1, ApartmentNumber n2, Diff diff) {

		if (begin.compareTo(ApplicationConfig.getFutureInfinite()) >= 0) {
			return;
		}
		if (end.compareTo(ApplicationConfig.getPastInfinite()) <= 0) {
			return;
		}
		if (begin.after(end) || (empty(n1) && empty(n2))) {
			return;
		}
		if (n1 != null && n2 != null && n1.getValue().equals(n2.getValue())) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_NUMBER_VALUE);
		rec.setBeginDate(begin);
		rec.setEndDate(end);
		rec.setOldStringValue(n1 != null ? n1.getValue() : null);
		rec.setNewStringValue(n2 != null ? n2.getValue() : null);
		diff.addRecord(rec);

		log.debug("Address diff {}", rec);
	}

	private boolean empty(ApartmentNumber an) {
		return an == null || StringUtils.isBlank(an.getValue());
	}

	private void buildBuildingReferenceDiff(@NotNull Apartment a1, @NotNull Apartment a2, @NotNull Diff diff) {

		Building b1 = a1.getBuilding();
		Building b2 = a2.getBuilding();
		boolean noParent = (b1 == null || b1.isNew()) && (b2 == null || b2.isNew());

		// no parent found in both objects, nothing to do
		if (noParent) {
			return;
		}

		boolean sameParent = b1 != null && b1.isNotNew() &&
							 b2 != null && b2.isNotNew() &&
							 b1.equals(b2);
		// same parent found in both objects, nothing to do
		if (sameParent) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_BUILDING_ID);
		rec.setOldStringValue(
				b1 == null || b1.isNew() ?
				null :
				masterIndexService.getMasterIndex(b1));
		rec.setNewStringValue(
				b2 == null || b2.isNew() ?
				null :
				masterIndexService.getMasterIndex(b2));
		diff.addRecord(rec);

		log.debug("Biolding ref diff {}", rec);
	}

	/**
	 * Apply diff to an object
	 *
	 * @param apartment Object to apply diff to
	 * @param diff	  Diff to apply
	 */
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
			}
		}
	}

	private void patchAddress(@NotNull  Apartment apartment, @NotNull HistoryRecord record) {

		log.debug("Patching number {}", record);
		apartment.setNumberForDates(record.getBeginDate(), record.getEndDate(), record.getNewStringValue());
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchBuildingReference(@NotNull  Apartment apartment, @NotNull HistoryRecord record) {
		log.debug("Patching building reference {}", record);

		Building building = null;
		if (record.getNewStringValue() != null) {
			String externalId = record.getNewStringValue();
			Stub<Building> stub = correctionsService.findCorrection(
					externalId, Building.class, masterIndexService.getMasterSourceDescription());
			if (stub == null) {
				throw new IllegalStateException("Cannot find building by master index: " + externalId);
			}
			building = new Building(stub);
		}

		apartment.setBuilding(building);
		log.debug("Patched building reference {}", apartment.getBuilding());
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}
}
