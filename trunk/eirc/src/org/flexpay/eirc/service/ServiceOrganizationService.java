package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Set;

public interface ServiceOrganizationService extends org.flexpay.orgs.service.ServiceOrganizationService {

	/**
	 * Get served buildings for service organization on specific page
	 *
	 * @param stub  Service organization stub
	 * @param pager Page
	 * @return Set of served buildings
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_READ_SERVED_BUILDINGS)
	List<ServedBuilding> findServedBuildings(@NotNull Stub<? extends ServiceOrganization> stub, Page<ServedBuilding> pager);

	@Secured (Roles.SERVICE_ORGANIZATION_REMOVE_SERVED_BUILDINGS)
	void removeServedBuildings(@NotNull Set<Long> objectIds) throws FlexPayExceptionContainer;

	/**
	 * Save or update served building
	 *
	 * @param servedBuilding Served building to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_ADD_SERVED_BUILDINGS)
	void updateServedBuilding(@NotNull ServedBuilding servedBuilding) throws FlexPayExceptionContainer;
}
