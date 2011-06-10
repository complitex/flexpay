package org.flexpay.eirc.dao.impl;

import org.flexpay.eirc.dao.ConsumerDaoExt;
import org.flexpay.eirc.persistence.Consumer;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import java.util.List;

public class ConsumersDaoExtJdbcImpl extends JpaDaoSupport implements ConsumerDaoExt {

	/**
	 * Find Consumer by account number and service id
	 *
	 * @param accountNumber Service provider internal account number
	 * @param code		  Service id
	 * @return Consumer if found, or <code>null</code> otherwise
	 */
	@Override
	public Consumer findConsumerByService(String accountNumber, Long code) {
		Object[] params = {accountNumber, code};
		List<?> results = getJpaTemplate().findByNamedQuery("Consumer.findConsumersByService", params);
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
    @Override
	public Consumer findConsumerByProviderServiceCode(Long providerId, String accountNumber, String code) {
		Object[] params = {providerId, accountNumber, code};
		List<?> results = getJpaTemplate().findByNamedQuery("Consumer.findConsumersByProviderServiceCode", params);
		return results.isEmpty() ? null : (Consumer) results.get(0);
	}
}