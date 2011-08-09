package org.flexpay.tc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class Tariff extends DomainObjectWithStatus {

	private String subServiceCode;
	private Set<TariffTranslation> translations = set();

	public Tariff() {
	}

	public Tariff(@NotNull Long id) {
		super(id);
	}

	public Tariff(@NotNull Stub<Tariff> stub) {
		super(stub.getId());
	}

	public String getSubServiceCode() {
		return subServiceCode;
	}

	public void setSubServiceCode(String subServiceCode) {
		this.subServiceCode = subServiceCode;
	}

	public Set<TariffTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<TariffTranslation> translations) {
		this.translations = translations;
	}

	public void setTranslation(@NotNull TariffTranslation translation) {
		translations = TranslationUtil.setTranslation(translations, this, translation);
	}

	public String getDefultTranslation() {
		return TranslationUtil.getTranslation(translations).getName();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("subServiceCode", subServiceCode).
				toString();
	}

}
