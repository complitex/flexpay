package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TimeLine;

import java.util.List;

/**
 * Town
 */
public class Town extends NameTimeDependentChild<TownName, TownNameTemporal> {

	private TimeLine<TownType, TownTypeTemporal> typesTimeLine;

//	private Set<District> districts = new HashSet<District>(0);
//	private Set<Street> streets = new HashSet<Street>(0);
//	private Set<Building> buildings = new HashSet<Building>(0);

	/**
	 * Constructs a new Town.
	 */
	public Town() {
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
	public void setTypeTemporals(List<TownTypeTemporal> temporals) {
		typesTimeLine = new TimeLine<TownType, TownTypeTemporal>(temporals);
	}

	/**
	 * Getter for property 'typeTemporals'.
	 *
	 * @return Value for property 'typeTemporals'.
	 */
	public List<TownTypeTemporal> getTypeTemporals() {
		return typesTimeLine.getIntervals();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(typesTimeLine)
				.appendSuper(super.hashCode())
				.toHashCode();
	}
}
