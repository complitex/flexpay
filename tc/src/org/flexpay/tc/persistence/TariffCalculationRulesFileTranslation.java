package org.flexpay.tc.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;

public class TariffCalculationRulesFileTranslation extends Translation {

	private String description;

	public TariffCalculationRulesFileTranslation() {
	}

	public TariffCalculationRulesFileTranslation(@NotNull String name) {
		super(name, getDefaultLanguage());
	}

	public TariffCalculationRulesFileTranslation(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isBlank() {
		return StringUtils.isBlank(getName()) && StringUtils.isBlank(getDescription());
	}

	@Override
	public void copyName(Translation t) {
		super.copyName(t);
		if (t instanceof TariffCalculationRulesFileTranslation) {
			this.description = ((TariffCalculationRulesFileTranslation) t).getDescription();
		}
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof TariffCalculationRulesFileTranslation && super.equals(o);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("name", getName()).
				append("lang", getLang()).
				append("description", description).
				toString();
	}

}
