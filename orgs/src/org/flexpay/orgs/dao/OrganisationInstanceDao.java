package org.flexpay.orgs.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.OrganizationInstance;
import org.flexpay.orgs.persistence.OrganizationInstanceDescription;

import java.util.List;

public interface OrganisationInstanceDao<
		D extends OrganizationInstanceDescription,
		T extends OrganizationInstance<D, T>> extends GenericDao<T, Long> {

	/**
	 * Find instances
	 *
	 * @param pager Page
	 * @return list instances
	 */
	List<T> findInstances(Page<T> pager);

    /**
	 * Find instances
	 *
        * @param range Range
	 * @return list instances
	 */
	List<T> listInstancesWithIdentities(FetchRange range);

	/**
	 * Find instances for organization
	 *
	 * @param organizationId Organization key
	 * @return List of instances of this type for organization
	 */
	List<T> findOrganizationInstances(Long organizationId);

	/**
	 * Find organizations that are not instances of type <code>T</code> except of that has a instance with specified
	 * <code>includedId</code>
	 *
	 * @param includedId Allowed instance key, that organization will also be in a resulting list
	 * @return List of organizations that are not instances of type T
	 */
	List<Organization> findInstancelessOrganizations(Long includedId);

}
