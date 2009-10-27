package org.flexpay.orgs.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.orgs.persistence.ServiceProviderAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ServiceProviderAttributeDao extends GenericDao<ServiceProviderAttribute, Long> {

    /**
	 * Find all service provider attributes
	 *
	 * @param serviceProviderId Service provider identifier
	 * @return List of registrations
	 */
    List<ServiceProviderAttribute> listServiceProviderAttributes(@NotNull Long serviceProviderId);

    /**
        * Get Service provider attributes by service provider Id and attribute name
        *
        * @param serviceProviderId Service provider Id
        * @param attributeName Attribute name
        * @return Service provider attributes
        */
    List<ServiceProviderAttribute> findServiceProviderAttribute(@NotNull Long serviceProviderId, @NotNull String attributeName);

    /**
     * Delete all service provider attributes
     *
     * @param serviceProviderId Service provider identifier
     */
    void deleteByServiceProvider(@NotNull Long serviceProviderId);
}
