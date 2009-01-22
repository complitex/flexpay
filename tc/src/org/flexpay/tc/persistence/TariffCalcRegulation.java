package org.flexpay.tc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.Collections;

public class TariffCalcRegulation extends DomainObjectWithStatus {

	private FPFile file;
	private Set<TariffCalcRegulationTranslation> translations = Collections.emptySet();

	public TariffCalcRegulation() {
	}

	public TariffCalcRegulation(@NotNull Long id) {
		super(id);
	}

	public TariffCalcRegulation(@NotNull Stub<TariffCalcRegulation> stub) {
		super(stub.getId());
	}

	public FPFile getFile() {
		return file;
	}

	public void setFile(FPFile file) {
		this.file = file;
	}

	public Set<TariffCalcRegulationTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<TariffCalcRegulationTranslation> translations) {
		this.translations = translations;
	}

	public void setTranslation(@NotNull TariffCalcRegulationTranslation translation) {
		translations = TranslationUtil.setTranslation(translations, this, translation);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("TariffCalcRegulation {").
				append("id", getId()).
				append("status", getStatus()).
				append("file", file).
				append("}").toString();
	}

}
