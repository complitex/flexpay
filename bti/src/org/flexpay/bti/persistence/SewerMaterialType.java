package org.flexpay.bti.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class SewerMaterialType extends DomainObjectWithStatus {
    private Set<SewerMaterialTypeTranslation> translations = Collections.emptySet();

    public SewerMaterialType() {
    }

    public SewerMaterialType(@NotNull Long id) {
        super(id);
    }

    public SewerMaterialType(@NotNull Stub<SewerMaterialType> stub) {
        super(stub.getId());
    }

    public Set<SewerMaterialTypeTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(Set<SewerMaterialTypeTranslation> translations) {
        this.translations = translations;
    }

    public void setTranslation(@NotNull SewerMaterialTypeTranslation translation) {
        translations = TranslationUtil.setTranslation(translations, this, translation);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("SewerMaterialType {").
                append("id", getId()).
                append("status", getStatus()).
                append("}").toString();
    }
}
