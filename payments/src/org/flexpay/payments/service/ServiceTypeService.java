package org.flexpay.payments.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.Collection;
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
	 *
	 * @return list of service types
	 */
	@Secured (Roles.SERVICE_TYPE_READ)
	List<ServiceType> getAllServiceTypes();

	/**
	 * Read full service type info
	 *
	 * @param stub Service type stub
	 * @return ServiceType
	 */
	@Secured (Roles.SERVICE_TYPE_READ)
	@Nullable
	ServiceType read(@NotNull Stub<ServiceType> stub);

	/**
	 * Create a new ServiceType object
	 *
	 * @param type ServiceType
	 * @return Persisted type back
	 * @throws FlexPayExceptionContainer if validation error occurs
	 */
	@Secured (Roles.SERVICE_TYPE_ADD)
	ServiceType create(@NotNull ServiceType type) throws FlexPayExceptionContainer;

	/**
	 * Save ServiceType object
	 *
	 * @param type ServiceType
	 * @return Updated object back
	 * @throws FlexPayExceptionContainer if validation error occurs
	 */
	@Secured (Roles.SERVICE_TYPE_CHANGE)
	ServiceType update(@NotNull ServiceType type) throws FlexPayExceptionContainer;

	/**
	 * Find service type by its code
	 *
	 * @param code Service type code
	 * @return Service type if found
	 * @throws IllegalArgumentException if the <code>code</code> is invalid
	 */
	@Secured (Roles.SERVICE_TYPE_READ)
	@NotNull
	ServiceType getServiceType(int code) throws IllegalArgumentException;

    /**
     * Find service types by its codes
     *
     * @param codes Service type codes
     * @return found service types
     */
    @Secured (Roles.SERVICE_TYPE_READ)
    @NotNull
    List<ServiceType> getByCodes(Collection<Integer> codes);

	/**
	 * Initialize filter
	 *
	 * @param serviceTypeFilter Filter to initialize
	 * @return Filter back
	 */
	@Secured (Roles.SERVICE_TYPE_READ)
	ServiceTypeFilter initFilter(ServiceTypeFilter serviceTypeFilter);

	void delete(@NotNull ServiceType type);
}
