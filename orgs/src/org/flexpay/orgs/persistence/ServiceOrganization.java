package org.flexpay.orgs.persistence;

/**
 * Service organization
 */
public class ServiceOrganization extends OrganizationInstance<ServiceOrganizationDescription, ServiceOrganization> {

	/**
	 * Constructs a new DomainObject.
	 */
	protected ServiceOrganization() {
	}

	public ServiceOrganization(Long id) {
		super(id);
	}

	public static ServiceOrganization newInstance() {
		return new ServiceOrganization();
	}
}
