package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class Translation extends DomainObject {

	private String name;
	private Language lang;
	private DomainObject translatable;

	/**
	 * Constructs a new Translation.
	 */
	public Translation() {
	}

	public Translation(@NotNull String name, @NotNull Language lang) {
		this.name = name;
		this.lang = lang;
	}

	/**
	 * Getter for property 'name'.
	 *
	 * @return Value for property 'name'.
	 */
	@NotNull
	public String getName() {
		return name != null ? name : "";
	}

	/**
	 * Getter for property 'lang'.
	 *
	 * @return Value for property 'lang'.
	 */
	@NotNull
	public Language getLang() {
		return lang;
	}

	/**
	 * Setter for property 'name'.
	 *
	 * @param name Value to set for property 'name'.
	 */
	public void setName(@NotNull @NonNls String name) {
		this.name = name;
	}

	public void copyName(Translation t) {
		this.name = t.getName();
	}

	/**
	 * Getter for property 'translatable'.
	 *
	 * @return Value for property 'translatable'.
	 */
	@NotNull 
	public DomainObject getTranslatable() {
		return translatable;
	}

	/**
	 * Setter for property 'translatable'.
	 *
	 * @param translatable Value to set for property 'translatable'.
	 */
	public void setTranslatable(@NotNull DomainObject translatable) {
		this.translatable = translatable;
	}

	/**
	 * Setter for property 'lang'.
	 *
	 * @param lang Value to set for property 'lang'.
	 */
	public void setLang(@NotNull Language lang) {
		this.lang = lang;
	}

	/**
	 * Check if another translation has the same language
	 *
	 * @param translation Translation to check
	 * @return <code>true</code> if translation uses the same language, or <code>false</code> otherwise
	 */
	public boolean isSameLanguage(@NotNull Translation translation) {
		return getLang().equals(translation.getLang());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("Translation {").
				append("id", getId()).
				append("name", name).
				append("lang", lang).
				append("}").toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(lang)
				.append(name)
				.toHashCode();
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Translation)) {
			return false;
		}
		final Translation that = (Translation) o;

		return new EqualsBuilder()
				.append(lang, that.getLang())
				.append(name, that.getName())
				.isEquals();
	}

}
