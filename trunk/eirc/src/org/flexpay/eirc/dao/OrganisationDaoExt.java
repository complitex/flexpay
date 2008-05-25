package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.Organisation;

public interface OrganisationDaoExt {

	/**
	 * Get organisation stub by uniqueId
	 *
	 * @param uniqueId Organisation Eirc number
	 * @return Organisation stub if found, or <code>null</code> if not found
	 */
	Organisation getOrganisationStub(String uniqueId);
}
