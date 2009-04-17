package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ApartmentNumber;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import static org.flexpay.common.util.CollectionUtils.treeSet;
import static org.flexpay.common.util.CollectionUtils.list;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Collections;

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
		SortedSet<ApartmentNumber> addresses1 = treeSet(a1.getApartmentNumbers());
		SortedSet<ApartmentNumber> addresses2 = treeSet(a2.getApartmentNumbers());
		Iterator<ApartmentNumber> it1 = addresses1.iterator();
		Iterator<ApartmentNumber> it2 = addresses2.iterator();

		Date cursor = ApplicationConfig.getPastInfinite();
		String previousValue = null;
		ApartmentNumber n1 = null;
		ApartmentNumber n2 = null;

		while (it1.hasNext() || it2.hasNext()) {
			n1 = n1 == null && it1.hasNext() ? it1.next() : n1;
			n2 = n2 == null && it2.hasNext() ? it2.next() : n2;

			Date begin1 = n1 != null ? n1.getBegin() : ApplicationConfig.getFutureInfinite();
			Date begin2 = n2 != null ? n2.getBegin() : ApplicationConfig.getFutureInfinite();
			Date end1 = n1 != null ? n1.getEnd() : ApplicationConfig.getFutureInfinite();
			Date end2 = n2 != null ? n2.getEnd() : ApplicationConfig.getFutureInfinite();

			Date begin = Collections.min(list(begin1, begin2));
		}
	}

	private void addAddressDiff(Date begin, Date end, ApartmentNumber n1, ApartmentNumber n2, Diff diff) {

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
				masterIndexService.getMasterIndex(b2.getDistrict()));
		diff.addRecord(rec);
	}

	/**
	 * Apply diff to an object
	 *
	 * @param apartment Object to apply diff to
	 * @param diff	  Diff to apply
	 */
	public void patch(@NotNull Apartment apartment, @NotNull Diff diff) {
	}
}
