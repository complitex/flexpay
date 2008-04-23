package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Collections;
import java.util.Set;

/**
 * Temporary object name having reference to object and a collection of translations
 */
public abstract class TemporaryType<TV extends TemporaryValue, T extends Translation>
		extends TemporaryValue<TV> implements ObjectWithStatus {

	private Set<T> translations = Collections.emptySet();
	private int status;

	protected TemporaryType() {
	}

	protected TemporaryType(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'translations'.
	 *
	 * @return Value for property 'translations'.
	 */
	public Set<T> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'translations'.
	 */
	public void setTranslations(Set<T> translations) {
		this.translations = translations;
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
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("id", getId())
				.append("Translations", translations.toArray())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof TemporaryName)) {
			return false;
		}

		TemporaryName that = (TemporaryName) obj;
		return new EqualsBuilder()
				.append(translations, that.getTranslations()).isEquals();
	}
}
