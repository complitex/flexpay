package org.flexpay.tc.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;

public class TariffTranslation extends Translation {

	public TariffTranslation() {
	}

	public TariffTranslation(String name, Language lang) {
		super(name, lang);
	}

	public boolean isBlank() {
		return StringUtils.isBlank(getName());
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof TariffTranslation && super.equals(o);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("TariffTranslation {").
				append("id", getId()).
				append("name", getName()).
				append("lang", getLang()).
				append("}").toString();
	}

}
