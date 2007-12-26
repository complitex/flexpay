package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.temp.Town;
import org.flexpay.common.persistence.TimeLine;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Region
 */
public class Region implements java.io.Serializable {

	public static final int STATUS_ACTIVE = 0;
	public static final int STATUS_DISABLED = 1;

	private Long id;
	private Country country;
	private int status;
	private TimeLine<RegionName, RegionNameTemporal> namesTimeLine;
	private Set<Town> towns = Collections.emptySet();

	// Region name in current locale
	private transient RegionName regionName;

	/**
	 * Constructs a new Region.
	 */
	public Region() {
	}

	/**
	 * Getter for property 'id'.
	 *
	 * @return Value for property 'id'.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Setter for property 'id'.
	 *
	 * @param id Value to set for property 'id'.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for property 'country'.
	 *
	 * @return Value for property 'country'.
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * Setter for property 'country'.
	 *
	 * @param country Value to set for property 'country'.
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * Getter for property 'status'.
	 *
	 * @return Value for property 'status'.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Setter for property 'status'.
	 *
	 * @param status Value to set for property 'status'.
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Getter for property 'namesTimeLine'.
	 *
	 * @return Value for property 'namesTimeLine'.
	 */
	public TimeLine<RegionName, RegionNameTemporal> getNamesTimeLine() {
		return namesTimeLine;
	}

	/**
	 * Setter for property 'namesTimeLine'.
	 *
	 * @param namesTimeLine Value to set for property 'namesTimeLine'.
	 */
	public void setNamesTimeLine(TimeLine<RegionName, RegionNameTemporal> namesTimeLine) {
		this.namesTimeLine = namesTimeLine;
	}

	public void setNameTemporals(List<RegionNameTemporal> nameTemporals) {
		namesTimeLine = new TimeLine<RegionName, RegionNameTemporal>(nameTemporals);
	}

	public List<RegionNameTemporal> getNameTemporals() {
		return namesTimeLine.getIntervals();
	}

	/**
	 * Getter for property 'towns'.
	 *
	 * @return Value for property 'towns'.
	 */
	public Set<Town> getTowns() {
		return this.towns;
	}

	/**
	 * Setter for property 'towns'.
	 *
	 * @param towns Value to set for property 'towns'.
	 */
	public void setTowns(Set<Town> towns) {
		this.towns = towns;
	}

	/**
	 * Getter for property 'regionName'.
	 *
	 * @return Value for property 'regionName'.
	 */
	public RegionName getRegionName() {
		return regionName;
	}

	/**
	 * Setter for property 'regionName'.
	 *
	 * @param regionName Value to set for property 'regionName'.
	 */
	public void setRegionName(RegionName regionName) {
		this.regionName = regionName;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", id)
				.append("Status", status)
				.append("Names", namesTimeLine)
				.toString();
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 *
	 * @param obj the reference object with which to compare.
	 * @return <code>true</code> if this object is the same as the obj argument;
	 *         <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Region)) {
			return false;
		}

		Region region = (Region) obj;

		return new EqualsBuilder()
				.append(namesTimeLine, region.namesTimeLine)
				.isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(namesTimeLine)
				.toHashCode();
	}
}
