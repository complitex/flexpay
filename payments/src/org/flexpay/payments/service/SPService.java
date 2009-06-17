package org.flexpay.payments.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.service.DomainObjectService;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.filters.ServiceFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Collection;

/**
 * Service providers services helper service
 */
public interface SPService extends DomainObjectService<Service> {

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
	@Override
	@Nullable
	Service readFull(@NotNull Stub<? extends Service> stub);

	/**
	 * Create service
	 *
	 * @param service Service to save
	 * @return Persisted object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.SERVICE_ADD)
	@Override
	@NotNull
	Service create(@NotNull Service service) throws FlexPayExceptionContainer;

	/**
	 * Update service
	 *
	 * @param service Service to save
	 * @return updated service back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.SERVICE_CHANGE)
	@Override
	@NotNull
	Service update(@NotNull Service service) throws FlexPayExceptionContainer;

	@Override
	void disable(@NotNull Collection<Long> ids);

	/**
	 * Initalize service filter with a list of parent services
	 *
	 * @param filter Filter to initialize
	 * @return Filter back
	 */
	@Secured (Roles.SERVICE_READ)
	ServiceFilter initParentServicesFilter(ServiceFilter filter);

	void delete(@NotNull Service service);
}
