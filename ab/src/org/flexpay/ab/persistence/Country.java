package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * Country
 */
public class Country implements Serializable {

	public static final int STATUS_ACTIVE = 0;
	public static final int STATUS_DISABLED = 1;

	// Fields
	private Long id;
	private int status;
	private Set<CountryNameTranslation> countryNames = Collections.emptySet();
	private Set<Region> regions = Collections.emptySet();

	// Constructors

	/**
	 * default constructor
	 */
	public Country() {
	}

	/**
	 * Getter for property 'id'.
	 *
	 * @return Value for property 'id'.
	 */
	public Long getId() {
		return id;
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
	 * Getter for property 'regions'.
	 *
	 * @return Value for property 'regions'.
	 */
	public Set<Region> getRegions() {
		return regions;
	}

	/**
	 * Setter for property 'regions'.
	 *
	 * @param regions Value to set for property 'regions'.
	 */
	public void setRegions(Set<Region> regions) {
		this.regions = regions;
	}

	/**
	 * Getter for property 'countryNames'.
	 *
	 * @return Value for property 'countryNames'.
	 */
	public Set<CountryNameTranslation> getCountryNames() {
		return countryNames;
	}

	/**
	 * Setter for property 'countryNames'.
	 *
	 * @param countryNames Value to set for property 'countryNames'.
	 */
	public void setCountryNames(Set<CountryNameTranslation> countryNames) {
		this.countryNames = countryNames;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", id)
				.append("Status", status)
				.append("Names", countryNames.toArray())
				.append("Regions", regions.toArray())
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (! (obj instanceof Country)) {
			return false;
		}

		Country country = (Country) obj;
		return new EqualsBuilder()
				.append(status, country.status)
				.append(countryNames, country.countryNames)
				.isEquals();
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(status)
				.append(countryNames)
				.toHashCode();
	}
}
