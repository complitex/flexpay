package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

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

	public Stub<DomainObject> getStub() {
		return stub(object);
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
	@NotNull
	public Set<T> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'translations'.
	 */
	public void setTranslations(@NotNull Set<T> translations) {
		this.translations = translations;
	}

	public void addNameTranslation(T translation) {

		translations = TranslationUtil.setTranslation(translations, this, translation);
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

		return obj instanceof TemporaryName && super.equals(obj);
	}
}
