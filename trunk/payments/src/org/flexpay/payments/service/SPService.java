package org.flexpay.payments.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.filters.ServiceFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.List;

/**
 * Service providers services helper service
 */
public interface SPService {

	/**
	 * List active services using filters and pager
	 *
	 * @param filters Set of filters to apply
	 * @param pager   Page
	 * @return List of services
	 */
	@Secured (Roles.SERVICE_READ)
	List<Service> listServices(List<ObjectFilter> filters, Page<Service> pager);

	/**
	 * Read full service information
	 *
	 * @param stub Service stub
	 * @return Service description
	 */
	@Secured (Roles.SERVICE_READ)
	@Nullable
	Service read(@NotNull Stub<Service> stub);

	/**
	 * Create or update service
	 *
	 * @param service Service to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured ({Roles.SERVICE_ADD, Roles.SERVICE_CHANGE})
	void save(Service service) throws FlexPayExceptionContainer;

	/**
	 * Initalize service filter with a list of parent services
	 *
	 * @param filter Filter to initialize
	 * @return Filter back
	 */
	@Secured (Roles.SERVICE_READ)
	ServiceFilter initParentServicesFilter(ServiceFilter filter);
}
