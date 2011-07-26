package org.flexpay.mule.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

public class MuleStreetType extends MuleIdObject {

    private Set<MuleTranslation> translations = set();

    public StreetType convert(StreetType streetType) throws FlexPayException {

        for (Language lang : getLanguages()) {
            for (MuleTranslation muleTrans : translations) {
                if (lang.getId().equals(muleTrans.getLanguageId())) {
                    StreetTypeTranslation translation = new StreetTypeTranslation(muleTrans.getName(), lang);
                    translation.setShortName(muleTrans.getShortName());
                    streetType.setTranslation(translation);
                }
            }
        }

        return streetType;
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
                append("id", id).
                append("ids", ids).
                append("translations", translations).
                toString();
    }
}
