package org.flexpay.mule.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.ab.persistence.*;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;

import java.util.Date;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.common.util.DateUtil.parseBeginDate;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;

public class MuleStreet extends MuleIdObject {

    private Long parentId;
    private Long typeId;
    private String nameDate;
    private Set<Long> districts = set();
    private Set<MuleTranslation> translations = set();

    public Street convert(Street street) throws FlexPayException {

        StreetName streetName = new StreetName();
        for (Language lang : getLanguages()) {
            for (MuleTranslation muleTrans : translations) {
                if (lang.getId().equals(muleTrans.getLanguageId())) {
                    streetName.setTranslation(new StreetNameTranslation(muleTrans.getName(), lang));
                }
            }
        }

        Date beginDate = isEmpty(nameDate) ? getPastInfinite() : parseBeginDate(nameDate);

        street.setNameForDate(streetName, beginDate);
        street.setTypeForDate(new StreetType(typeId), beginDate);

        if (street.isNew()) {
            street.setParent(new Town(parentId));
        }

        return street;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getNameDate() {
        return nameDate;
    }

    public void setNameDate(String nameDate) {
        this.nameDate = nameDate;
    }

    public Set<Long> getDistricts() {
        return districts;
    }

    public void setDistricts(Set<Long> districts) {
        this.districts = districts;
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
                append("parentId", parentId).
                append("typeId", typeId).
                append("nameDate", nameDate).
                append("districts", districts).
                append("translations", translations).
                toString();
    }
}
