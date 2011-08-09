package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;

import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;

/**
 * Street name translation
 */
public class StreetNameTranslation extends Translation {

	public StreetNameTranslation() {
	}

	public StreetNameTranslation(String name) {
		super(name, getDefaultLanguage());
	}

	public StreetNameTranslation(String name, Language lang) {
		super(name, lang);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("id", getId())
                .append("language", getLang().getLangIsoCode())
				.append("name", getName())
				.toString();
	}

    @Override
    public int hashCode() {
        return super.hashCode();
    }

	@Override
	public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof StreetNameTranslation)) {
            return false;
        }
        return super.equals(o);
	}
}
