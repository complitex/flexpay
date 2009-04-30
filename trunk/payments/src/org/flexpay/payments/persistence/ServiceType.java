package org.flexpay.payments.persistence;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Locale;

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

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ServiceType && super.equals(obj);
	}

}
