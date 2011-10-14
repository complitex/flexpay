package org.flexpay.payments.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;

import java.util.Date;
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
     * Find Service by its code
     *
     * @param code service code
     * @return Service instance
     */
    Service findServiceByCode(int code);

	/**
	 * List active services using filters and pager
	 *
	 * @param filters Set of filters to apply
	 * @param pager   Page
	 * @return List of services
	 */
	List<Service> findServices(List<ObjectFilter> filters, Page<Service> pager);


	/**
	 * Find provider services of the specified type for date interval
	 *
	 * @param providerId Service provider identifier
	 * @param typeId	 Service type identifier
	 * @param beginDate  Interval begin date
	 * @param endDate	interval end date
	 * @return List of services
	 */
	List<Service> findIntersectingServices(Long providerId, Long typeId, Date beginDate, Date endDate);

}
