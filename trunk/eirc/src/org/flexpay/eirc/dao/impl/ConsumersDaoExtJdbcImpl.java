package org.flexpay.eirc.dao.impl;

import org.flexpay.eirc.dao.ConsumerDaoExt;
import org.flexpay.eirc.persistence.Consumer;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceException;
import java.util.List;

public class ConsumersDaoExtJdbcImpl extends JpaDaoSupport implements ConsumerDaoExt {

	/**
	 * Find Consumer by account number and service id
	 *
	 * @param accountNumber Service provider internal account number
	 * @param code		  Service id
	 * @return Consumer if found, or <code>null</code> otherwise
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public Consumer findConsumerByService(final String accountNumber, final Long code) {
		List<?> results = getJpaTemplate().execute(new JpaCallback<List<?>>() {
			@Override
			public List<?> doInJpa(EntityManager entityManager) throws PersistenceException {
				entityManager.setFlushMode(FlushModeType.COMMIT);
				return entityManager.createNamedQuery("Consumer.findConsumersByService").
						setParameter(1, accountNumber).
						setParameter(2, code).getResultList();
			}
		});
		return results.isEmpty() ? null : (Consumer) results.get(0);
	}

	/**
	 * Find Consumer by provider identifier, account number and external service code
	 *
	 * @param providerId	Provider key
	 * @param accountNumber Service provider internal account number
	 * @param code		  Service provider external code code
	 * @return Consumer if found, or <code>null</code> otherwise
	 */
	@Transactional (readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
	public Consumer findConsumerByProviderServiceCode(final Long providerId, final String accountNumber, final String code) {
		List<?> results = getJpaTemplate().execute(new JpaCallback<List<?>>() {
			@Override
			public List<?> doInJpa(EntityManager entityManager) throws PersistenceException {
				entityManager.setFlushMode(FlushModeType.COMMIT);
				return entityManager.createNamedQuery("Consumer.findConsumersByProviderServiceCode").
						setParameter(1, providerId).
						setParameter(2, accountNumber).
						setParameter(3, code).getResultList();
			}
		});
		return results.isEmpty() ? null : (Consumer) results.get(0);
	}
}