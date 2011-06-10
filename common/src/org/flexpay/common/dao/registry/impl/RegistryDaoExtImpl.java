package org.flexpay.common.dao.registry.impl;

import org.flexpay.common.dao.registry.RegistryDaoExt;
import org.flexpay.common.persistence.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RegistryDaoExtImpl extends JpaDaoSupport implements RegistryDaoExt {

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	@SuppressWarnings ({"unchecked"})
    @Override
	public Collection<Registry> findRegistries(@NotNull final Set<Long> objectIds) {
		return getJpaTemplate().executeFind(new JpaCallback() {
			@Override
			public Object doInJpa(EntityManager entityManager) throws PersistenceException {
				return entityManager.createQuery("select distinct r from Registry r " +
						"inner join fetch r.registryType " +
						"inner join fetch r.registryStatus " +
						"left join fetch r.containers " +
						"where r.id in (:ids)")
						.setParameter("ids", objectIds).getResultList();
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
		List<?> value = getJpaTemplate().findByNamedQuery("RegistryType.haveNotProcessedRecords", registryId);

		return !value.isEmpty();
	}
}
