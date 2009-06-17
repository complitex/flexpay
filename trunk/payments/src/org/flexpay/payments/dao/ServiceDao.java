package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.payments.persistence.Service;

import java.util.List;

public interface ServiceDao extends GenericDao<Service, Long> {

	/**
	 * Find service provider services by type
	 *
	 * @param providerId Service provider id
	 * @param serviceCode Service type code
	 * @return list of services
	 */
	List<Service> findServicesByTypeCode(Long providerId, Long serviceCode);

	/**
	 * Find service provider services by provider code
	 *
	 * @param providerId Service provider id
	 * @param serviceCode Service type code
	 * @return list of services
	 */
	List<Service> findServicesByProviderCode(Long providerId, String serviceCode);

	/**
	 * List all services
	 * 
	 * @return all services
	 */
	List<Service> findAllServices();
}
