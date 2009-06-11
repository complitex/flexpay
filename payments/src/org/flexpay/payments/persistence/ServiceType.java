package org.flexpay.payments.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ServiceType extends DomainObjectWithStatus {

	private int code;
	private Set<ServiceTypeNameTranslation> typeNames = Collections.emptySet();

	public ServiceType() {
	}

	public ServiceType(Long id) {
		super(id);
	}

	public Set<ServiceTypeNameTranslation> getTypeNames() {
		return typeNames;
	}

	public void setTypeNames(Set<ServiceTypeNameTranslation> typeNames) {
		this.typeNames = typeNames;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setTypeName(ServiceTypeNameTranslation nameTranslation) {
		if (Collections.emptySet().equals(typeNames)) {
			typeNames = new HashSet<ServiceTypeNameTranslation>();
		}

		ServiceTypeNameTranslation candidate = null;
		for (ServiceTypeNameTranslation name : typeNames) {
			if (name.getLang().getId().equals(nameTranslation.getLang().getId())) {
				candidate = name;
				break;
			}
		}

		if (candidate != null) {
			if (StringUtils.isBlank(nameTranslation.getName()) && StringUtils.isBlank(nameTranslation.getDescription())) {
				typeNames.remove(candidate);
				return;
			}
			candidate.setName(nameTranslation.getName());
			candidate.setDescription(nameTranslation.getDescription());
			return;
		}

		if (StringUtils.isBlank(nameTranslation.getName()) && StringUtils.isBlank(nameTranslation.getDescription())) {
			return;
		}

		nameTranslation.setTranslatable(this);
		typeNames.add(nameTranslation);
	}

	public String getName(@NotNull Locale locale) {
		return TranslationUtil.getTranslation(typeNames, locale).getName();
	}

	public String getName() {
		return getName(ApplicationConfig.getDefaultLocale());
	}

	/**
	 * Get name translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public ServiceTypeNameTranslation getTranslation(@NotNull Language lang) {

		for (ServiceTypeNameTranslation translation : getTypeNames()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}


	@Override
	public boolean equals(Object obj) {
		return obj instanceof ServiceType && super.equals(obj);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("code", code).
				toString();
	}

}
