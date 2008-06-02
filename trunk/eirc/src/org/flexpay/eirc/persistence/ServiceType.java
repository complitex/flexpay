package org.flexpay.eirc.persistence;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ServiceType extends DomainObjectWithStatus {

	private int code;
	private Set<ServiceTypeNameTranslation> typeNames = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public ServiceType() {
	}

	public ServiceType(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'typeNames'.
	 *
	 * @return Value for property 'typeNames'.
	 */
	public Set<ServiceTypeNameTranslation> getTypeNames() {
		return typeNames;
	}

	/**
	 * Setter for property 'typeNames'.
	 *
	 * @param typeNames Value to set for property 'typeNames'.
	 */
	public void setTypeNames(Set<ServiceTypeNameTranslation> typeNames) {
		this.typeNames = typeNames;
	}

	/**
	 * Getter for property 'code'.
	 *
	 * @return Value for property 'code'.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Setter for property 'code'.
	 *
	 * @param code Value to set for property 'code'.
	 */
	public void setCode(int code) {
		this.code = code;
	}

	public void setTypeName(ServiceTypeNameTranslation nameTranslation) {
		if (typeNames == Collections.EMPTY_SET) {
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
}
