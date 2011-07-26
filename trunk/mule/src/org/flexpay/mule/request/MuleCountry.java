package org.flexpay.mule.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.CountryTranslation;
import org.flexpay.common.persistence.Language;

import java.io.Serializable;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

public class MuleCountry implements Serializable {

    private Set<MuleTranslation> translations = set();

    public Country convert(Country country) {

        for (Language lang : getLanguages()) {
            for (MuleTranslation muleTrans : translations) {
                if (lang.getId().equals(muleTrans.getLanguageId())) {
                    CountryTranslation translation = new CountryTranslation(muleTrans.getName(), lang);
                    translation.setShortName(muleTrans.getShortName());
                    country.setTranslation(translation);
                }
            }
        }

        return country;
    }

    public Set<MuleTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(Set<MuleTranslation> translations) {
        this.translations = translations;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("translations", translations).
                toString();
    }
}
