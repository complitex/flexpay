package org.flexpay.payments.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

public class ServiceTypeNameTranslation extends Translation {

	private String description;

	public ServiceTypeNameTranslation() {
	}

	public ServiceTypeNameTranslation(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

	public ServiceTypeNameTranslation(@NotNull String name, @NotNull String description, @NotNull Language lang) {
		super(name, lang);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
