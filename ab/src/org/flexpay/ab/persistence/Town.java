package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Town
 */
public class Town extends NameTimeDependentChild<TownName, TownNameTemporal> {

	private TimeLine<TownType, TownTypeTemporal> typesTimeLine;

	private Set<District> districts = Collections.emptySet();
	private Set<Street> streets = Collections.emptySet();

	/**
	 * Constructs a new Town.
	 */
	public Town() {
	}

	/**
	 * Constructs a new Town.
	 *
	 * @param id Town id
	 */
	public Town(Long id) {
		super(id);
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
	public TimeLine<TownType, TownTypeTemporal> getTypesTimeLine() {
		return typesTimeLine;
	}

	/**
	 * Setter for property 'typesTimeLine'.
	 *
	 * @param typesTimeLine Value to set for property 'typesTimeLine'.
	 */
	public void setTypesTimeLine(TimeLine<TownType, TownTypeTemporal> typesTimeLine) {
		this.typesTimeLine = typesTimeLine;
	}

	/**
	 * Setter for property 'typeTemporals'.
	 *
	 * @param temporals Value to set for property 'typeTemporals'.
	 */
	public void setTypeTemporals(SortedSet<TownTypeTemporal> temporals) {
		typesTimeLine = new TimeLine<TownType, TownTypeTemporal>(temporals);
	}

	/**
	 * Getter for property 'typeTemporals'.
	 *
	 * @return Value for property 'typeTemporals'.
	 */
	public SortedSet<TownTypeTemporal> getTypeTemporals() {
		return typesTimeLine.getIntervalsSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", getId())
				.append("Status", getStatus())
				.append("Names", getNamesTimeLine())
				.append("Types", typesTimeLine)
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
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

	@NotNull
	public Stub<Region> getRegionStub() {
		return new Stub<Region>(getParentStub().getId());
	}

	/**
	 * Find value for date
	 *
	 * @param dt Date to get value for
	 * @return Value which interval includes specified date, or <code>null</code> if not found
	 */
	@Nullable
	public TownType getTypeForDate(Date dt) {
		if (typesTimeLine == null) {
			return null;
		}
		List<TownTypeTemporal> intervals = typesTimeLine.getIntervals();
		for (TownTypeTemporal di : intervals) {
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
	public TownType getCurrentType() {
		return getTypeForDate(DateUtil.now());
	}


}
