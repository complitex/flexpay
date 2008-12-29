package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.Organization;

public interface OrganizationDaoExt {

	/**
	 * Get organization stub by uniqueId
	 *
	 * @param uniqueId Organization Eirc number
	 * @return Organization stub if found, or <code>null</code> if not found
	 */
	Organization getOrganizationStub(String uniqueId);

}
