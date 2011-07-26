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

public class MuleTown extends MuleIdObject {

    private Long parentId;
    private Long typeId;
    private String nameDate;
    private Set<MuleTranslation> translations = set();

    public Town convert(Town town) throws FlexPayException {

        TownName townName = new TownName();
        for (Language lang : getLanguages()) {
            for (MuleTranslation muleTrans : translations) {
                if (lang.getId().equals(muleTrans.getLanguageId())) {
                    townName.setTranslation(new TownNameTranslation(muleTrans.getName(), lang));
                }
            }
        }

        Date beginDate = isEmpty(nameDate) ? getPastInfinite() : parseBeginDate(nameDate);

        town.setNameForDate(townName, beginDate);
        town.setTypeForDate(new TownType(typeId), beginDate);

        if (town.isNew()) {
            town.setParent(new Region(parentId));
        }

        return town;
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
                append("translations", translations).
                toString();
    }
}
