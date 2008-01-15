package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Set;
import java.util.Collections;

/**
 * Temporary object name having reference to object and a collection of translations
 */
public abstract class TemporaryName<TV extends TemporaryValue, T extends Translation>
		extends TemporaryValue<TV> {

	private DomainObject object;
	private Set<T> translations = Collections.emptySet();

	protected TemporaryName() {
	}

	/**
	 * Getter for property 'object'.
	 *
	 * @return Value for property 'object'.
	 */
	public DomainObject getObject() {
		return object;
	}

	/**
	 * Setter for property 'object'.
	 *
	 * @param object Value to set for property 'object'.
	 */
	public void setObject(DomainObject object) {
		this.object = object;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(translations).toHashCode();
	}
}
