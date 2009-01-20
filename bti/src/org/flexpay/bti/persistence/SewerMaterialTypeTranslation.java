package org.flexpay.bti.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

public class SewerMaterialTypeTranslation extends Translation {
    private String description;

    public SewerMaterialTypeTranslation() {
    }

    public SewerMaterialTypeTranslation(@NotNull String name, @NotNull Language lang) {
        super(name, lang);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SewerMaterialTypeTranslation && super.equals(o);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("SewerMaterialTypeTranslation {").
                append("id", getId()).
                append("name", getName()).
                append("lang", getLang()).
                append("description", description).
                append("}").toString();
    }
}
