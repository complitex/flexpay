package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.payments.persistence.Service;

import java.util.List;
import java.util.Date;

public interface ServiceDao extends GenericDao<Service, Long> {

	/**
	 * Find service provider services by type
	 *
	 * @param providerId Service provider id
	 * @param serviceCode Service type code
	 * @return list of services
	 */
	List<Service> findServicesByCode(Long providerId, Long serviceCode);

	/**
	 * Find service provider services by type and date
	 *
	 * @param providerId Service provider id
	 * @param serviceCode Service type code
	 * @param date Date services are valid on
	 * @return list of services
	 */
	List<Service> findServicesByTypeCodeAndDate(Long providerId, Long serviceCode, Date date);

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
