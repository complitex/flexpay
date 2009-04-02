package org.flexpay.orgs.persistence;

/**
 * Service organization
 */
public class ServiceOrganization extends OrganizationInstance<ServiceOrganizationDescription, ServiceOrganization> {

	/**
	 * Constructs a new DomainObject.
	 */
	public ServiceOrganization() {
	}

	public ServiceOrganization(Long id) {
		super(id);
	}
}
