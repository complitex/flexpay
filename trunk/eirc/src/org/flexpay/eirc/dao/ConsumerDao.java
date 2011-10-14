package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.Consumer;

import java.util.Collection;
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
	 * Find consumer by external account number and service type key
	 *
	 * @param accountNumber Extrernal account number
	 * @param serviceId	 Service id
	 * @return List of found consumers
	 */
	List<Consumer> findConsumersByAccountAndServiceType(String accountNumber, Long serviceId);

    /**
     * Find consumer by external account number and service key
     *
     * @param accountNumber Extrernal account number
     * @param serviceId	 Service id
     * @return List of found consumers
     */
    List<Consumer> findConsumersByAccountAndService(String accountNumber, Long serviceId);

    /**
     * Find consumer by external account number
     *
     * @param accountNumber Extrernal account number
     * @return List of found consumers
     */
    List<Consumer> findConsumersByAccount(String accountNumber);

    /**
     * Find consumer by apartment ids
     *
     * @param apartmentIds apartment ids
     * @return List of found consumers
     */
    List<Consumer> findConsumersByApartments(Collection<Long> apartmentIds);

    /**
     * Find consumer by ERC account number and service type
     *
     * @param ercAccount ERC account
     * @param serviceTypeId Service type id
     * @return List of found consumers
     */
    List<Consumer> findConsumersByERCAccountAndServiceType(String ercAccount, Long serviceTypeId);

    /**
     * Find consumer by ERC account number and service
     *
     * @param ercAccount ERC account
     * @param serviceTypeId Service type id
     * @return List of found consumers
     */
    List<Consumer> findConsumersByERCAccountAndService(String ercAccount, Long serviceTypeId);

    /**
     * Find consumer by ERC account number
     *
     * @param ercAccount ERC account
     * @return List of found consumers
     */
    List<Consumer> findConsumersByERCAccount(String ercAccount);

    List<Consumer> findConsumersByEIRCAccount(Long eircAccountId);
}
