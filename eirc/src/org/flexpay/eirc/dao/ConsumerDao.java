package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.Consumer;

import java.util.List;

/**
 * Persistence layer implementation for Consumers
 */
public interface ConsumerDao extends GenericDao<Consumer, Long> {

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
	List<Consumer> findConsumers(Page pager, Long personId, Long serviceId, String accountNumber, Long apartmentId);

	/**
	 * Find consumer by external account number and service key
	 *
	 * @param accountNumber Extrernal account number
	 * @param serviceId	 Service id
	 * @return List of found consumers
	 */
	List<Consumer> findConsumersByAccountAndService(String accountNumber, Long serviceId);

    /**
     * Find consumer by ERC account number and service
     *
     * @param ercAccount ERC account
     * @param serviceId Service id
     * @return List of found consumers
     */
    List<Consumer> findConsumersByERCAccountAndService(String ercAccount, Long serviceId);

    /**
     * Find consumer by ERC account number
     *
     * @param ercAccount ERC account
     * @return List of found consumers
     */
    List<Consumer> findConsumersByERCAccount(String ercAccount);

}
