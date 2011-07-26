package org.flexpay.mule.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

public class MuleTownType extends MuleIdObject {

    private Set<MuleTranslation> translations = set();

    public TownType convert(TownType townType) throws FlexPayException {

        for (Language lang : getLanguages()) {
            for (MuleTranslation muleTrans : translations) {
                if (lang.getId().equals(muleTrans.getLanguageId())) {
                    TownTypeTranslation translation = new TownTypeTranslation(muleTrans.getName(), lang);
                    translation.setShortName(muleTrans.getShortName());
                    townType.setTranslation(translation);
                }
            }
        }

        return townType;
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
