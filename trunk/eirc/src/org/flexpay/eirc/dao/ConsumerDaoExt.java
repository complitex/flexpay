package org.flexpay.eirc.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.Consumer;

import java.util.List;

/**
 * Extended persistence layer functionality for Consumers
 */
public interface ConsumerDaoExt {

	/**
	 * Find consumer by example data
	 *
	 * @param pager		 Page
	 * @param personId	  Responsible person id
	 * @param serviceId	 Consumer service id
	 * @param accountNumber Consumer external account number
	 * @param apartmentId   Apartment id
	 * @return list of consumers
	 */
	List<Consumer> findConsumers(Page<?> pager, Long personId, Long serviceId, String accountNumber, Long apartmentId);

	/**
	 * Find Consumer by provider identifier, account number and service type code
	 *
	 * @param accountNumber Service provider internal account number
	 * @param code		  Service type code
	 * @return Consumer if found, or <code>null</code> otherwise
	 */
	Consumer findConsumerByService(String accountNumber, Long code);

	/**
	 * Find Consumer by provider identifier, account number and external service code
	 *
	 * @param providerId	Provider key
	 * @param accountNumber Service provider internal account number
	 * @param code		  Service provider external code code
	 * @return Consumer if found, or <code>null</code> otherwise
	 */
	Consumer findConsumerByProviderServiceCode(Long providerId, String accountNumber, String code);
}