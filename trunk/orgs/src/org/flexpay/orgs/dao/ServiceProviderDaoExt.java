package org.flexpay.orgs.dao;

import org.flexpay.orgs.persistence.ServiceProvider;

public interface ServiceProviderDaoExt {

	/**
	 * Find Service Provider by Organization id
	 *
	 * @param organizationId Organization key
	 * @return ServiceProvider instance
	 */
	ServiceProvider findByNumber(Long organizationId);
}
