package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
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
	 * @param pager Page
	 * @param personId Responsible person id
	 * @param serviceId Consumer service id
	 * @param accountNumber Consumer external account number
	 * @param apartmentId Apartment id
	 * @return list of consumers
	 */
	List<Consumer> findConsumers(Page pager, Long personId, Long serviceId, String accountNumber, Long apartmentId);
}