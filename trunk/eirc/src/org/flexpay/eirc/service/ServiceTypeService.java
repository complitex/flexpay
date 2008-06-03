package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.filters.ServiceTypeFilter;

import java.util.List;
import java.util.Set;

public interface ServiceTypeService {

	/**
	 * Disable service types
	 *
	 * @param objectIds Identifiers of service type to disable
	 */
	void disable(Set<Long> objectIds);

	/**
	 * List service types
	 *
	 * @param pager Page
	 * @return list of Service types for current page
	 */
	List<ServiceType> listServiceTypes(Page<ServiceType> pager);

	/**
	 * Read full service type info
	 *
	 * @param serviceType ServiceType stub
	 * @return ServiceType
	 */
	ServiceType read(ServiceType serviceType);

	/**
	 * Save ServiceType object
	 *
	 * @param type ServiceType
	 * @throws FlexPayExceptionContainer if validation error occurs
	 */
	void save(ServiceType type) throws FlexPayExceptionContainer;

	/**
	 * Find service type by its code
	 *
	 * @param code Service type code
	 * @return Service type if found
	 * @throws IllegalArgumentException if the <code>code</code> is invalid
	 */
	ServiceType getServiceType(int code) throws IllegalArgumentException;

	/**
	 * Read service type details
	 *
	 * @param typeStub Service type stub
	 * @return Service type
	 */
	ServiceType getServiceType(ServiceType typeStub);

	/**
	 * Initialize filter
	 *
	 * @param serviceTypeFilter Filter to initialize
	 * @return Filter back
	 */
	ServiceTypeFilter initFilter(ServiceTypeFilter serviceTypeFilter);
}
