package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TimeLine;
import static org.flexpay.common.util.CollectionUtils.treeSet;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.DateUtil;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Town
 */
public class Town extends NameTimeDependentChild<TownName, TownNameTemporal> {

	private static final SortedSet<TownTypeTemporal> EMPTY_SORTED_SET =
			Collections.unmodifiableSortedSet(new TreeSet<TownTypeTemporal>());

	private SortedSet<TownTypeTemporal> typeTemporals = EMPTY_SORTED_SET;
	private TimeLine<TownType, TownTypeTemporal> typesTimeLine;

	private Set<District> districts = Collections.emptySet();
	private Set<Street> streets = Collections.emptySet();

	public Town() {
	}

	public Town(Long id) {
		super(id);
	}

	public Town(@NotNull Stub<Town> stub) {
		super(stub.getId());
	}

	/**
	 * Create a new empty temporal
	 *
	 * @return empty temporal
	 */
    @Override
	protected TownNameTemporal getEmptyTemporal() {
		return new TownNameTemporal(this);
	}

	/**
	 * Getter for property 'districts'.
	 *
	 * @return Value for property 'districts'.
	 */
	public Set<District> getDistricts() {
		return districts;
	}

	/**
	 * Setter for property 'districts'.
	 *
	 * @param districts Value to set for property 'districts'.
	 */
	public void setDistricts(Set<District> districts) {
		this.districts = districts;
	}

	@NotNull
	public Set<Street> getStreets() {
		return streets;
	}

	public void setStreets(Set<Street> streets) {
		this.streets = streets;
	}

	/**
	 * Getter for property 'typesTimeLine'.
	 *
	 * @return Value for property 'typesTimeLine'.
	 */
	@NotNull
	public TimeLine<TownType, TownTypeTemporal> getTypesTimeLine() {

		if (typesTimeLine == null) {
			TownTypeTemporal temporal = new TownTypeTemporal();
			temporal.setTown(this);
			return new TimeLine<TownType, TownTypeTemporal>(temporal);
		}

		return typesTimeLine;
	}

	/**
	 * Setter for property 'typesTimeLine'.
	 *
	 * @param typesTimeLine Value to set for property 'typesTimeLine'.
	 */
	public void setTypesTimeLine(TimeLine<TownType, TownTypeTemporal> typesTimeLine) {
		this.typesTimeLine = typesTimeLine;

		if (typeTemporals == EMPTY_SORTED_SET) {
			typeTemporals = treeSet();
		}
		typeTemporals.addAll(typesTimeLine.getIntervals());
	}

	/**
	 * Setter for property 'typeTemporals'.
	 *
	 * @param temporals Value to set for property 'typeTemporals'.
	 */
	public void setTypeTemporals(SortedSet<TownTypeTemporal> temporals) {
		this.typeTemporals = temporals;
		typesTimeLine = new TimeLine<TownType, TownTypeTemporal>(temporals);
	}

	/**
	 * Getter for property 'typeTemporals'.
	 *
	 * @return Value for property 'typeTemporals'.
	 */
	public SortedSet<TownTypeTemporal> getTypeTemporals() {

		for (TownTypeTemporal temporal : typeTemporals) {
			if (temporal.getValue() != null && temporal.getValue().isNew()) {
				temporal.setValue(null);
			}
		}

		return typeTemporals;
	}

	@NotNull
	public Stub<Region> getRegionStub() {
		return new Stub<Region>(getParentStub().getId());
	}

	/**
	 * Find temporal for date
	 *
	 * @return Value which interval includes specified date, or <code>null</code> if not found
	 */
	@Nullable
	public TownTypeTemporal getCurrentTypeTemporal() {
		return getTypeTemporalForDate(DateUtil.now());
	}

	/**
	 * Find temporal for date
	 *
	 * @param dt Date to get value for
	 * @return Value which interval includes specified date, or <code>null</code> if not found
	 */
	@Nullable
	public TownTypeTemporal getTypeTemporalForDate(Date dt) {
		if (typesTimeLine == null) {
			return null;
		}
		List<TownTypeTemporal> intervals = typesTimeLine.getIntervals();
		for (TownTypeTemporal di : intervals) {
			if (DateIntervalUtil.includes(dt, di)) {
				return di;
			}
		}

		return null;
	}

	/**
	 * Find value for date
	 *
	 * @param dt Date to get value for
	 * @return Value which interval includes specified date, or <code>null</code> if not found
	 */
	@Nullable
	public TownType getTypeForDate(Date dt) {
		TownTypeTemporal di = getTypeTemporalForDate(dt);
		return di != null ? di.getValue() : null;
	}

	/**
	 * Find value for current date
	 *
	 * @return Value which interval includes specified date, or <code>null</code> if not found
	 */
	@Nullable
	public TownType getCurrentType() {
		TownTypeTemporal di = getCurrentTypeTemporal();
		return di != null ? di.getValue() : null;
	}

	public void addTypeTemporal(TownTypeTemporal temporal) {
		if (typesTimeLine == null) {
			typesTimeLine = new TimeLine<TownType, TownTypeTemporal>(temporal);
		} else {
			typesTimeLine = DateIntervalUtil.addInterval(typesTimeLine, temporal);
		}

		if (typeTemporals == EMPTY_SORTED_SET) {
			typeTemporals = treeSet();
		}
		typeTemporals.addAll(typesTimeLine.getIntervals());
	}

	public void setRegion(Region region) {
		setParent(region);
	}

	public Region getRegion() {
		return (Region) getParent();
	}

	public Country getCountry() {
		return getRegion().getCountry();
	}

	public void setName(TownName name) {
		setNameForDate(name, DateUtil.now());
	}

	public void setNameForDate(TownName name, Date beginDate) {
		setNameForDates(name, beginDate, getFutureInfinite());
	}

	public void setNameForDates(TownName name, Date beginDate, Date endDate) {
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

		TownNameTemporal temporal = new TownNameTemporal();
		temporal.setBegin(beginDate);
		temporal.setEnd(endDate);
		temporal.setValue(name);
		temporal.setObject(this);

		addNameTemporal(temporal);
	}

	public void setType(TownType type) {
		setTypeForDate(type, DateUtil.now());
	}

	public void setTypeForDate(TownType type, Date beginDate) {
		setTypeForDates(type, beginDate, getFutureInfinite());
	}

	public void setTypeForDates(TownType type, Date beginDate, Date endDate) {
		TownTypeTemporal temporal = new TownTypeTemporal();
		temporal.setBegin(beginDate);
		temporal.setEnd(endDate);
		temporal.setValue(type);
		temporal.setTown(this);

		addTypeTemporal(temporal);
	}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Town)) {
            return false;
        }

        Town that = (Town) obj;

        return new EqualsBuilder()
                .append(typesTimeLine, that.typesTimeLine)
                .appendSuper(super.equals(obj))
                .isEquals();
    }

}
