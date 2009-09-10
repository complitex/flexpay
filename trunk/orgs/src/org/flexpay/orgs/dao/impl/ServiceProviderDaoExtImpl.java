package org.flexpay.orgs.dao.impl;

import org.flexpay.orgs.dao.ServiceProviderDaoExt;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class ServiceProviderDaoExtImpl extends HibernateDaoSupport implements ServiceProviderDaoExt {

	/**
	 * Find Service Provider by Organization id
	 *
	 * @param id Organization key
	 * @return ServiceProvider instance
	 */
	@Override
	public ServiceProvider findByNumber(Long id) {
		try {
			getHibernateTemplate().setMaxResults(1);
			List<?> objects = getHibernateTemplate()
					.findByNamedQuery("ServiceProvider.findByOrganizationId", id);
			return objects.isEmpty() ? null : (ServiceProvider) objects.get(0);
		} finally {
			getHibernateTemplate().setMaxResults(0);
		}
	}

}
