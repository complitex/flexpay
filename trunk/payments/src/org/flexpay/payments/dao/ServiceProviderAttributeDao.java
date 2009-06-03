package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.payments.persistence.process.ServiceProviderAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ServiceProviderAttributeDao extends GenericDao<ServiceProviderAttribute, Long> {

    /**
	 * List all person registrations
	 *
	 * @param serviceProviderId Service provider identifier
	 * @return List of registrations
	 */
    List<ServiceProviderAttribute> listServiceProviderAttributes(@NotNull Long serviceProviderId);

}
