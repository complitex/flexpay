package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.Stub;

import java.util.Set;
import java.util.Collections;

public class District extends NameTimeDependentChild<DistrictName, DistrictNameTemporal> {

	private Set<Street> streets = Collections.emptySet();

	/**
	 * Constructs a new District.
	 */
	public District() {
	}

	public District(Long id) {
		super(id);
	}

	public District(Stub<District> district) {
		super(district.getId());
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
	 * Getter for property 'streets'.
	 *
	 * @return Value for property 'streets'.
	 */
	public Set<Street> getStreets() {
		return streets;
	}

	/**
	 * Setter for property 'streets'.
	 *
	 * @param streets Value to set for property 'streets'.
	 */
	public void setStreets(Set<Street> streets) {
		this.streets = streets;
	}
}
