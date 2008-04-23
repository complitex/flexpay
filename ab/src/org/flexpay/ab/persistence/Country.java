package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.Set;

/**
 * Country
 */
public class Country extends DomainObjectWithStatus {

	private Set<CountryNameTranslation> countryNames = Collections.emptySet();
	private Set<Region> regions = Collections.emptySet();

	// Constructors

	/**
	 * default constructor
	 */
	public Country() {
	}

	public Country(Long id) {
		super(id);
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
				.append("id", getId())
				.append("Status", getStatus())
				.append("Names", countryNames.toArray())
				.append("Regions", regions.toArray())
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Country)) {
			return false;
		}

		Country country = (Country) obj;
		return new EqualsBuilder()
				.append(countryNames, country.countryNames)
				.isEquals();
	}
}
