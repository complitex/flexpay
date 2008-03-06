package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.ServiceType;

/**
 * Service providers helper service
 */
public interface SPService {

	/**
	 * Find service provider by its number
	 *
	 * @param providerNumber Service provider unique number
	 * @return ServiceProvider instance
	 * @throws IllegalArgumentException if provider cannot be found
	 */
	ServiceProvider getProvider(Long providerNumber) throws IllegalArgumentException;

	/**
	 * Find service type by its code
	 *
	 * @param code Service type code
	 * @return Service type if found
	 * @throws IllegalArgumentException if the <code>code</code> is invalid
	 */
	ServiceType getServiceType(int code) throws IllegalArgumentException;

	/**
	 * Find service of specified <code>type</code> for provider
	 *
	 * @param provider ServiceProvider
	 * @param type	 ServiceType to find
	 * @return Service if found, or <code>null</code> if the requested service is not
	 *         available from <code>provider</code>
	 */
	Service getService(ServiceProvider provider, ServiceType type);
}
