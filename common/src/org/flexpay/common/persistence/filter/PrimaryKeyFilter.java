package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Translation;

import java.util.Collection;
import java.util.Collections;

public class PrimaryKeyFilter<T extends Translation> {

	private Long selectedId;
	private Collection<T> translations = Collections.emptyList();

	/**
	 * Getter for property 'selectedCountryId'.
	 *
	 * @return Value for property 'selectedCountryId'.
	 */
	public Long getSelectedId() {
		return selectedId;
	}

	/**
	 * Setter for property 'selectedCountryId'.
	 *
	 * @param selectedId Value to set for property 'selectedCountryId'.
	 */
	public void setSelectedId(Long selectedId) {
		this.selectedId = selectedId;
	}

	/**
	 * Getter for property 'countryNames'.
	 *
	 * @return Value for property 'countryNames'.
	 */
	public Collection<T> getNames() {
		return translations;
	}

	/**
	 * Setter for property 'countryNames'.
	 *
	 * @param names Value to set for property 'countryNames'.
	 */
	public void setNames(Collection<T> names) {
		this.translations = names;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Selected id", selectedId)
				.toString();
	}
}
