package org.flexpay.orgs.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.dao.ServiceProviderAttributeDao;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderAttribute;
import org.flexpay.orgs.service.ServiceProviderAttributeService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class ServiceProviderAttributeServiceImpl implements ServiceProviderAttributeService {
    private ServiceProviderAttributeDao serviceProviderAttributeDao;

    /**
	 * Find all service provider attributes
	 *
	 * @param stub Service provider
	 * @return Service providers list, empty if no service providers found
	 */
    @NotNull
    @Override
    public List<ServiceProviderAttribute> listServiceProviderAttributes(@NotNull Stub<ServiceProvider> stub) {
        return serviceProviderAttributeDao.listServiceProviderAttributes(stub.getId());
    }

    /**
        * Find service provider attribute by name for service provider
        *
        * @param stub Service provider
        * @param attributeName Attribute name
	 * @return Service providers list, empty if no service providers found
        */
    @Nullable
    @Override
    public ServiceProviderAttribute getServiceProviderAttribute(@NotNull Stub<ServiceProvider> stub, @NotNull String attributeName) {
        List<ServiceProviderAttribute> attributes = serviceProviderAttributeDao.findServiceProviderAttribute(stub.getId(), attributeName);
        return attributes.isEmpty() ? null : attributes.get(0);
    }

    /**
	 * Save service provider attribute
	 *
	 * @param attribute New or persistent object to save
	 */
	@Transactional (readOnly = false)
    @Override
	public void save(@NotNull ServiceProviderAttribute attribute) {
		if (attribute.isNew()) {
			attribute.setId(null);
			serviceProviderAttributeDao.create(attribute);
		} else {
			serviceProviderAttributeDao.update(attribute);
		}
	}

    /**
     * {@inheritDoc}
     */
    @Transactional (readOnly = false)
    @Override
    public void delete(@NotNull Stub<ServiceProvider> stub) {
        serviceProviderAttributeDao.deleteByServiceProvider(stub.getId());
    }

    @Required
    public void setServiceProviderAttributeDao(ServiceProviderAttributeDao serviceProviderAttributeDao) {
        this.serviceProviderAttributeDao = serviceProviderAttributeDao;
    }
}
