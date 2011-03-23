package org.flexpay.ab.persistence;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.flexpay.common.util.CollectionUtils.treeSet;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;

/**
 * Street
 */
public class Street extends NameTimeDependentChild<StreetName, StreetNameTemporal> {

	private static final SortedSet<StreetTypeTemporal> EMPTY_SORTED_SET =
			Collections.unmodifiableSortedSet(new TreeSet<StreetTypeTemporal>());

	private Set<StreetDistrictRelation> streetDistricts = Collections.emptySet();
	private SortedSet<StreetTypeTemporal> typeTemporals = EMPTY_SORTED_SET;
	private TimeLine<StreetType, StreetTypeTemporal> typesTimeLine;
	private Set<BuildingAddress> buildingses = Collections.emptySet();

	public Street() {
	}

	public Street(Long id) {
		super(id);
	}

	public Street(Stub<Street> stub) {
		super(stub.getId());
	}

	/**
	 * Create a new empty temporal
	 *
	 * @return empty temporal
	 */
    @Override
	protected StreetNameTemporal getEmptyTemporal() {
		return new StreetNameTemporal(this);
	}

	public Set<StreetDistrictRelation> getStreetDistricts() {
		return streetDistricts;
	}

	public void setStreetDistricts(Set<StreetDistrictRelation> streetDistricts) {
		this.streetDistricts = streetDistricts;
	}

	public Set<BuildingAddress> getBuildingses() {
		return buildingses;
	}

	public void setBuildingses(Set<BuildingAddress> buildingses) {
		this.buildingses = buildingses;
	}

	@NotNull
	public TimeLine<StreetType, StreetTypeTemporal> getTypesTimeLine() {

		if (typesTimeLine == null) {
			StreetTypeTemporal temporal = new StreetTypeTemporal();
			temporal.setStreet(this);
			return new TimeLine<StreetType, StreetTypeTemporal>(temporal);
		}

		return typesTimeLine;
	}

	public Street addTypeTemporal(StreetTypeTemporal temporal) {
		TimeLine<StreetType, StreetTypeTemporal> tlNew;
		if (typesTimeLine == null) {
			tlNew = new TimeLine<StreetType, StreetTypeTemporal>(temporal);
		} else {
			tlNew = DateIntervalUtil.addInterval(typesTimeLine, temporal);
		}

		if (typesTimeLine == tlNew) {
			// nothing to do, timeline did not changed
			return this;
		}

		if (typeTemporals == EMPTY_SORTED_SET) {
			typeTemporals = treeSet();
		}

		// add all new intervals
		typeTemporals.addAll(tlNew.getIntervals());
		typesTimeLine = tlNew;

		for (StreetTypeTemporal typeTemporal : typeTemporals) {
			if (typeTemporal.getValue() != null && typeTemporal.getValue().isNew()) {
				typeTemporal.setValue(null);
			}
		}

		return this;
	}

	public void setType(StreetType type) {
		setTypeForDate(type, DateUtil.now());
	}

	public void setTypeForDate(StreetType type, Date beginDate) {
		setTypeForDates(type, beginDate, getFutureInfinite());
	}

	public void setTypeForDates(StreetType type, Date beginDate, Date endDate) {
		StreetTypeTemporal temporal = new StreetTypeTemporal();
		temporal.setBegin(beginDate);
		temporal.setEnd(endDate);
		temporal.setValue(type);
		temporal.setObject(this);

		addTypeTemporal(temporal);
	}

	public void setTypesTimeLine(TimeLine<StreetType, StreetTypeTemporal> typesTimeLine) {
		this.typesTimeLine = typesTimeLine;

		if (typeTemporals == EMPTY_SORTED_SET) {
			typeTemporals = treeSet();
		}
		typeTemporals.addAll(typesTimeLine.getIntervals());
	}

	public void setTypeTemporals(SortedSet<StreetTypeTemporal> typeTemporals) {
		this.typeTemporals = typeTemporals;
		typesTimeLine = new TimeLine<StreetType, StreetTypeTemporal>(typeTemporals);
	}

	@NotNull
	public SortedSet<StreetTypeTemporal> getTypeTemporals() {
		return typeTemporals;
	}

	/**
	 * Find value for date
	 *
	 * @param dt Date to get value for
	 * @return Value which interval includes specified date, or <code>null</code> if not found
	 */
	@Nullable
	public StreetType getTypeForDate(Date dt) {
		if (typesTimeLine == null) {
			return null;
		}
		List<StreetTypeTemporal> intervals = typesTimeLine.getIntervals();
		for (StreetTypeTemporal di : intervals) {
			if (DateIntervalUtil.includes(dt, di)) {
				return di.getValue();
			}
		}

		return null;
	}

	/**
	 * Find value for current date
	 *
	 * @return Value which interval includes specified date, or <code>null</code> if not found
	 */
	@Nullable
	public StreetType getCurrentType() {
		return getTypeForDate(DateUtil.now());
	}

	@NotNull
	public String format(@NotNull Locale locale, boolean shortMode) throws FlexPayException {
		StringBuilder formatted = new StringBuilder();

		StreetTypeTranslation typeTanslation = getTypeTranslation(locale);
		if (typeTanslation != null) {
			if (shortMode) {
				formatted.append(typeTanslation.getShortName()).append(".");
			} else {
				formatted.append(typeTanslation.getName());
			}
		}

		StreetNameTranslation nameTranslation = getNameTranslation(locale);
		if (nameTranslation != null) {
			formatted.append(" ").append(nameTranslation.getName());
		}

		return formatted.toString();
	}

	@Nullable
	private StreetNameTranslation getNameTranslation(@NotNull Locale locale) {
		StreetName name = getCurrentName();
		StreetNameTranslation nameTranslation = null;
		if (name != null) {
			nameTranslation = TranslationUtil.getTranslation(name.getTranslations(), locale);
		}
		return nameTranslation;
	}

	@Nullable
	private StreetTypeTranslation getTypeTranslation(@NotNull Locale locale) {
		StreetType type = getCurrentType();
		StreetTypeTranslation typeTanslation = null;
		if (type != null) {
			typeTanslation = TranslationUtil.getTranslation(type.getTranslations(), locale);
		}
		return typeTanslation;
	}

	@NotNull
	public Town getTown() {
		return (Town) getParent();
	}

	@NotNull
	public Stub<Town> getTownStub() {
		return new Stub<Town>(getTown());
	}

	@NotNull
	public Region getRegion() {
		return getTown().getRegion();
	}

	@NotNull
	public Country getCountry() {
		return getTown().getCountry();
	}

	public void setName(StreetName name) {
		setNameForDate(name, DateUtil.now());
	}

	public void setNameForDate(StreetName name, Date beginDate) {
		setNameForDates(name, beginDate, getFutureInfinite());
	}

	public void setNameForDates(StreetName name, Date beginDate, Date endDate) {
		if (beginDate.after(endDate)) {
			throw new RuntimeException("Invalid begin-end dates: [" + DateUtil.format(beginDate) +
									   ", " + DateUtil.format(endDate) + "]");
		}
		if (beginDate.before(getPastInfinite())) {
			beginDate = getPastInfinite();
		}
		if (endDate.after(getFutureInfinite())) {
			endDate = getFutureInfinite();
		}

		name.setObject(this);

		StreetNameTemporal temporal = new StreetNameTemporal();
		temporal.setBegin(beginDate);
		temporal.setEnd(endDate);
		temporal.setValue(name);
		temporal.setObject(this);

		addNameTemporal(temporal);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Street && super.equals(obj);
	}

}
