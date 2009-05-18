package org.flexpay.payments.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.dao.ServiceProviderAttributeDao;
import org.flexpay.payments.persistence.process.ServiceProviderAttribute;
import org.flexpay.payments.service.ServiceProviderAttributeService;
import org.jetbrains.annotations.NotNull;
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
    public List<ServiceProviderAttribute> listServiceProviderAttributes(@NotNull Stub<ServiceProvider> stub) {
        return serviceProviderAttributeDao.listServiceProviderAttributes(stub.getId());
    }

    /**
	 * Save service provider attribute
	 *
	 * @param attribute New or persitent object to save
	 */
	@Transactional (readOnly = false)
	public void save(ServiceProviderAttribute attribute) {
		if (attribute.isNew()) {
			attribute.setId(null);
			serviceProviderAttributeDao.create(attribute);
		} else {
			serviceProviderAttributeDao.update(attribute);
		}
	}

    @Required
    public void setServiceProviderAttributeDao(ServiceProviderAttributeDao serviceProviderAttributeDao) {
        this.serviceProviderAttributeDao = serviceProviderAttributeDao;
    }
}
