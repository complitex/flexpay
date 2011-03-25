package org.flexpay.common.dao.registry.impl;

import org.flexpay.common.dao.registry.RegistryDaoExt;
import org.flexpay.common.persistence.registry.Registry;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RegistryDaoExtImpl extends HibernateDaoSupport implements RegistryDaoExt {

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	@SuppressWarnings ({"unchecked"})
    @Override
	public Collection<Registry> findRegistries(@NotNull final Set<Long> objectIds) {
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
            @Override
			public Object doInHibernate(Session session) throws HibernateException {
				return session.createQuery("select distinct r from Registry r " +
										   "inner join fetch r.registryType " +
										   "inner join fetch r.registryStatus " +
										   "left join fetch r.containers " +
										   "where r.id in (:ids)")
						.setParameterList("ids", objectIds).list();
			}
		});
	}

	/**
	 * Check if registry has more records to process
	 *
	 * @param registryId Registry id
	 * @return <code>true</code> if registry has records for processing, or <code>false</code> otherwise
	 */
    @Override
	public boolean hasMoreRecordsToProcess(Long registryId) {
		List<?> value = getHibernateTemplate().findByNamedQuery("RegistryType.haveNotProcessedRecords", registryId);

		return !value.isEmpty();
	}
}
