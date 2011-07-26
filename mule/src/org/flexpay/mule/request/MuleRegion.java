package org.flexpay.mule.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;

import java.util.Set;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.common.util.DateUtil.parseBeginDate;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;

public class MuleRegion extends MuleIdObject {

    private Long parentId;
    private String nameDate;
    private Set<MuleTranslation> translations = set();

    public Region convert(Region region) throws FlexPayException {

        RegionName regionName = new RegionName();
        for (Language lang : getLanguages()) {
            for (MuleTranslation muleTrans : translations) {
                if (lang.getId().equals(muleTrans.getLanguageId())) {
                    regionName.setTranslation(new RegionNameTranslation(muleTrans.getName(), lang));
                }
            }
        }

        region.setNameForDate(regionName, isEmpty(nameDate) ? getPastInfinite() : parseBeginDate(nameDate));

        if (region.isNew()) {
            region.setParent(new Country(parentId));
        }

        return region;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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
                append("nameDate", nameDate).
                append("translations", translations).
                toString();
    }
}
