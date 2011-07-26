package org.flexpay.mule.request;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class MuleTranslation implements Serializable {

    private String name;
    private String shortName;
    private Long languageId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("name", name).
                append("shortName", shortName).
                append("languageId", languageId).
                toString();
    }
}
