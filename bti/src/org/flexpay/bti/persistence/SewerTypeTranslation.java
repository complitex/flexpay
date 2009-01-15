package org.flexpay.bti.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;

public class SewerTypeTranslation extends Translation {

	private String description;

	public SewerTypeTranslation() {
	}

	public SewerTypeTranslation(String name, Language lang) {
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
	public boolean equals(Object o) {
		return o instanceof SewerTypeTranslation && super.equals(o);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("SewerTypeTranslation {").
				append("id", getId()).
				append("name", getName()).
				append("lang", getLang()).
				append("description", description).
				append("}").toString();
	}

}
