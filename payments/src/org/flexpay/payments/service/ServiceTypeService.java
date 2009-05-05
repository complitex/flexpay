package org.flexpay.payments.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Set;

public interface ServiceTypeService {

	/**
	 * Disable service types
	 *
	 * @param objectIds Identifiers of service type to disable
	 */
	@Secured (Roles.SERVICE_TYPE_DELETE)
	void disable(Set<Long> objectIds);

	/**
	 * List service types
	 *
	 * @param pager Page
	 * @return list of Service types for current page
	 */
	@Secured (Roles.SERVICE_TYPE_READ)
	List<ServiceType> listServiceTypes(Page<ServiceType> pager);

	/**
	 * List all service types
	 * @return list of service types
	 */
	@Secured (Roles.SERVICE_TYPE_READ)
	List<ServiceType> listAllServiceTypes();

	/**
	 * Read full service type info
	 *
	 * @param serviceType ServiceType stub
	 * @return ServiceType
	 */
	@Secured (Roles.SERVICE_TYPE_READ)
	ServiceType read(ServiceType serviceType);

	/**
	 * Save ServiceType object
	 *
	 * @param type ServiceType
	 * @throws FlexPayExceptionContainer if validation error occurs
	 */
	@Secured ({Roles.SERVICE_TYPE_ADD, Roles.SERVICE_TYPE_CHANGE})
	void save(ServiceType type) throws FlexPayExceptionContainer;

	/**
	 * Find service type by its code
	 *
	 * @param code Service type code
	 * @return Service type if found
	 * @throws IllegalArgumentException if the <code>code</code> is invalid
	 */
	@Secured (Roles.SERVICE_TYPE_READ)
	ServiceType getServiceType(int code) throws IllegalArgumentException;

	/**
	 * Read service type details
	 *
	 * @param typeStub Service type stub
	 * @return Service type
	 */
	@Secured (Roles.SERVICE_TYPE_READ)
	ServiceType getServiceType(ServiceType typeStub);

	/**
	 * Initialize filter
	 *
	 * @param serviceTypeFilter Filter to initialize
	 * @return Filter back
	 */
	@Secured (Roles.SERVICE_TYPE_READ)
	ServiceTypeFilter initFilter(ServiceTypeFilter serviceTypeFilter);
}
