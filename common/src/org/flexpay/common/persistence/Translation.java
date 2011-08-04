package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;

public class Translation extends DomainObject {

	protected String name;
	protected Language lang;
	protected DomainObject translatable;

	public Translation() {
	}

	public Translation(@NotNull String name, @NotNull Language lang) {
		this.name = name;
		this.lang = lang;
	}

	@NotNull
	public String getName() {
		return name != null ? name : "";
	}

	@NotNull
	public Language getLang() {
		return lang;
	}

	public void setName(@NotNull String name) {
		this.name = name;
	}

	public void copyName(Translation t) {
		this.name = t.getName();
	}

	@NotNull
	public DomainObject getTranslatable() {
		return translatable;
	}

	public void setTranslatable(@NotNull DomainObject translatable) {
		this.translatable = translatable;
	}

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

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(lang)
				.append(name)
				.toHashCode();
	}

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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("name", name).
				append("lang", lang).
				toString();
	}

}
