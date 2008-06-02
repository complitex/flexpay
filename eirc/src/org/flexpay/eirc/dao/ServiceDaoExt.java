package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.AccountRecordType;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface ServiceDaoExt {

	List<ServiceType> getServiceTypes();

	/**
	 * Find Service type by its code
	 *
	 * @param code service type code
	 * @return ServiceType instance
	 */
	ServiceType findByCode(int code);

	/**
	 * Find Service Provider by its unique number
	 *
	 * @param number service provider number
	 * @return ServiceProvider instance
	 */
	ServiceProvider findByNumber(Long number);

	/**
	 * Find Service by provider and type ids
	 *
	 * @param providerId ServiceProvider id
	 * @param typeId ServiceType id
	 * @return Service instance
	 */
	Service findService(Long providerId, Long typeId);

	/**
	 * Find record type by id
	 *
	 * @param typeId Record type enum id
	 * @return record type
	 */
	AccountRecordType findRecordType(int typeId);

	/**
	 * List active services using filters and pager
	 *
	 * @param filters Set of filters to apply
	 * @param pager   Page
	 * @return List of services
	 */
	List<Service> findServices(List<ObjectFilter> filters, Page<Service> pager);
}
