package org.flexpay.orgs.dao.impl;

import org.flexpay.orgs.dao.ServiceProviderDaoExt;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import java.util.List;

public class ServiceProviderDaoExtImpl extends JpaDaoSupport implements ServiceProviderDaoExt {

	/**
	 * Find Service Provider by Organization id
	 *
	 * @param id Organization key
	 * @return ServiceProvider instance
	 */
	@Override
	public ServiceProvider findByNumber(Long id) {
		List<?> objects = getJpaTemplate()
				.findByNamedQuery("ServiceProvider.findByOrganizationId", id);
		return objects.isEmpty() ? null : (ServiceProvider) objects.get(0);
	}

}
