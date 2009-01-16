package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * Temporary object name having reference to object and a collection of translations
 */
public abstract class TemporaryType<TV extends TemporaryValue<TV>, T extends Translation>
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
	 * Check if object is active
	 *
	 * @return <code>true</code> if object status is active, or <code>false</code> otherwise
	 */
	public boolean isActive() {
		return status == ObjectWithStatus.STATUS_ACTIVE;
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

		return obj instanceof TemporaryType && super.equals(obj);
	}

	/**
	 * Set translation for object
	 *
	 * @param translation Translation to set
	 */
	public void setTranslation(T translation) {
		translations = TranslationUtil.setTranslation(translations, this, translation);
	}

	/**
	 * Check if this value is empty
	 *
	 * @return <code>true</code> if this value is empty, or <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return translations == Collections.EMPTY_SET;
	}


	/**
	 * Get type translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public T getTranslation(@NotNull Language lang) {
		for (T translation : getTranslations()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}
}
