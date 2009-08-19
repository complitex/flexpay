package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.Language;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

public class CountryNameTranslation extends Translation {

	public static final Integer SHORT_NAME_LENGTH = 5;

	private String shortName;

	private transient LangNameTranslation langTranslation;

	public CountryNameTranslation() {
	}

	public CountryNameTranslation(@NotNull String name) {
		super(name, ApplicationConfig.getDefaultLanguage());
	}

	public CountryNameTranslation(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public LangNameTranslation getLangTranslation() {
		return langTranslation;
	}

	public void setLangTranslation(LangNameTranslation langTranslation) {
		this.langTranslation = langTranslation;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CountryNameTranslation)) {
			return false;
		}

		CountryNameTranslation that = (CountryNameTranslation) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(that))
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("id", getId())
				.append("language", getLang().getLangIsoCode())
				.append("name", getName())
				.append("shortName", shortName)
				.toString();
	}

}
