package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.CountryNameTranslation;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Collection;
import java.util.Collections;

/**
 * Country filter class
 */
public class CountryFilter {

	private Long selectedCountryId;
	private Collection<CountryNameTranslation> countryNames = Collections.emptyList();

	/**
	 * Constructs a new CountryFilter.
	 */
	public CountryFilter() {
	}

	/**
	 * Getter for property 'selectedCountryId'.
	 *
	 * @return Value for property 'selectedCountryId'.
	 */
	public Long getSelectedCountryId() {
		return selectedCountryId;
	}

	/**
	 * Setter for property 'selectedCountryId'.
	 *
	 * @param selectedCountryId Value to set for property 'selectedCountryId'.
	 */
	public void setSelectedCountryId(Long selectedCountryId) {
		this.selectedCountryId = selectedCountryId;
	}

	/**
	 * Getter for property 'countryNames'.
	 *
	 * @return Value for property 'countryNames'.
	 */
	public Collection<CountryNameTranslation> getCountryNames() {
		return countryNames;
	}

	/**
	 * Setter for property 'countryNames'.
	 *
	 * @param countryNames Value to set for property 'countryNames'.
	 */
	public void setCountryNames(Collection<CountryNameTranslation> countryNames) {
		this.countryNames = countryNames;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Selected Country id", selectedCountryId)
				.append("Countries", countryNames.toArray())
				.toString();
	}
}
