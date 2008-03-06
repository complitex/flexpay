package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.NameTimeDependentChild;

import java.util.Collections;
import java.util.Set;

/**
 * Region
 */
public class Region extends NameTimeDependentChild<RegionName, RegionNameTemporal> {

	private Set<Town> towns = Collections.emptySet();

	/**
	 * Constructs a new Region.
	 */
	public Region() {
	}

	public Region(Long id) {
		super(id);
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
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", getId())
				.append("Status", getStatus())
				.append("Names", getNamesTimeLine())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof Region && super.equals(obj);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
