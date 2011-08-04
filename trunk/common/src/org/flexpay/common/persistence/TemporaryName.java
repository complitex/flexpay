package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;

/**
 * Temporary object name having reference to object and a collection of translations
 */
public abstract class TemporaryName<TV extends TemporaryValue<TV>, T extends Translation>
		extends TemporaryValue<TV> {

	private DomainObject object;
	private Set<T> translations = Collections.emptySet();

	public DomainObject getObject() {
		return object;
	}

    @Override
    public String getXmlString() {
        return "";
    }

	public Stub<DomainObject> getStub() {
		return stub(object);
	}

	public void setObject(DomainObject object) {
		this.object = object;
	}

	@NotNull
	public Set<T> getTranslations() {
		return translations;
	}

	public void setTranslations(@NotNull Set<T> translations) {
		this.translations = translations;
	}

	public void addNameTranslation(T translation) {
		translations = TranslationUtil.setTranslation(translations, this, translation);
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

	@Nullable
	public String getDefaultNameTranslation() {
		T t = TranslationUtil.getTranslation(translations);
		return t == null ? null : t.getName();
	}
	
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
		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		return isNew() && translations == Collections.EMPTY_SET;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				toString();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TemporaryName && super.equals(obj);
	}

}
