package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Set;

public class ServiceType extends DomainObjectWithStatus {

	private int code;
	private Set<ServiceTypeNameTranslation> typeNames;

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
}
