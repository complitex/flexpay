package org.flexpay.bti.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class SewerType extends DomainObjectWithStatus {

	private Set<SewerTypeTranslation> translations = Collections.emptySet();

	public SewerType() {
	}

	public SewerType(@NotNull Long id) {
		super(id);
	}

	public SewerType(@NotNull Stub<SewerType> stub) {
		super(stub.getId());
	}


	public Set<SewerTypeTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<SewerTypeTranslation> translations) {
		this.translations = translations;
	}

	public void setTranslation(@NotNull SewerTypeTranslation translation) {
		translations = TranslationUtil.setTranslation(translations, this, translation);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("SewerType {").
				append("id", getId()).
				append("status", getStatus()).
				append("}").toString();
	}

}
