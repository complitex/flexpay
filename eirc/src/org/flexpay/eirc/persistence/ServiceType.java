package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Set;

public class ServiceType extends DomainObject {

	private Set<ServiceTypeNameTranslation> typeNames;

	/**
	 * Constructs a new DomainObject.
	 */
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
}