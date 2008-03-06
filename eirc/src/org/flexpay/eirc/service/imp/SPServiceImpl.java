package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.dao.ServiceDaoExt;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.service.SPService;

public class SPServiceImpl implements SPService {

	private ServiceDaoExt serviceDaoExt;

	/**
	 * Find service type by its code
	 *
	 * @param code Service type code
	 * @return Service type if found
	 * @throws IllegalArgumentException if the <code>code</code> is invalid
	 */
	public ServiceType getServiceType(int code) throws IllegalArgumentException {
		ServiceType type = serviceDaoExt.findByCode(code);
		if (type == null) {
			throw new IllegalArgumentException("Cannot find service type with code #" + code);
		}

		return type;
	}

	/**
	 * Find service provider by its number
	 *
	 * @param providerNumber Service provider unique number
	 * @return ServiceProvider instance
	 * @throws IllegalArgumentException if provider cannot be found
	 */
	public ServiceProvider getProvider(Long providerNumber) throws IllegalArgumentException {
		ServiceProvider serviceProvider = serviceDaoExt.findByNumber(providerNumber);
		if (serviceProvider == null) {
			throw new IllegalArgumentException("Cannot find service provider with number #" + providerNumber);
		}

		return serviceProvider;
	}

	/**
	 * Find service of specified <code>type</code> for provider
	 *
	 * @param provider ServiceProvider
	 * @param type	 ServiceType to find
	 * @return Service if found, or <code>null</code> if the requested service is not
	 *         available from <code>provider</code>
	 */
	public Service getService(ServiceProvider provider, ServiceType type) {
		return null;
	}

	/**
	 * Setter for property 'serviceTypeDaoExt'.
	 *
	 * @param serviceDaoExt Value to set for property 'serviceTypeDaoExt'.
	 */
	public void setServiceDaoExt(ServiceDaoExt serviceDaoExt) {
		this.serviceDaoExt = serviceDaoExt;
	}
}
