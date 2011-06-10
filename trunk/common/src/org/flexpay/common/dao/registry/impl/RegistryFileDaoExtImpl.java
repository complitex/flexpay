package org.flexpay.common.dao.registry.impl;

import org.flexpay.common.dao.registry.RegistryFileDaoExt;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

public class RegistryFileDaoExtImpl extends JpaDaoSupport implements RegistryFileDaoExt {

	/**
	 * Check if registry file is loaded to database
	 *
	 * @param fileId Registry file id
	 * @return <code>true</code> if file already loaded, or <code>false</code> otherwise
	 */
	@Override
	public boolean isLoaded(@NotNull final Long fileId) {
		return getJpaTemplate().execute(new JpaCallback<Boolean>() {
			@Override
			public Boolean doInJpa(EntityManager entityManager) throws PersistenceException {
				javax.persistence.Query qCount = entityManager.createNamedQuery("Registry.listRegistries.count");
				qCount.setParameter(1, fileId);
				Number count = (Number) qCount.getSingleResult();
				return count.longValue() > 0;
			}
		});
	}

}
