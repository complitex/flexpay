package org.flexpay.common.persistence;

import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	public Set<T> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<T> translations) {
		this.translations = translations;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
	}

	public void disable() {
		status = STATUS_DISABLED;
	}

	public void activate() {
		status = STATUS_ACTIVE;
	}

	/**
	 * Check if object is active
	 *
	 * @return <code>true</code> if object status is active, or <code>false</code> otherwise
	 */
	public boolean isActive() {
		return status == STATUS_ACTIVE;
	}

	public boolean isNotActive() {
		return status == STATUS_DISABLED;
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
	@Override
	public boolean isEmpty() {
		return isNew();
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

	/**
	 * Get type translation in a default language
	 *
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public T getDefaultTranslation() {
		return getTranslation(ApplicationConfig.getDefaultLanguage());
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TemporaryType && super.equals(obj);
	}

}
