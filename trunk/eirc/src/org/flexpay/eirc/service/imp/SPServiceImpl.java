package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.dao.ServiceDaoExt;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.AccountRecordType;
import org.flexpay.eirc.service.SPService;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

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
		if (code2TypeCache == null) {
			initializeServiceTypesCache();
		}
		if (code2TypeCache.containsKey(code)) {
			return code2TypeCache.get(code);
		}
		ServiceType type = serviceDaoExt.findByCode(code);
		if (type == null) {
			throw new IllegalArgumentException("Cannot find service type with code #" + code);
		}

		code2TypeCache.put(code, type);

		return type;
	}

	private Map<Integer, ServiceType> code2TypeCache;

	private void initializeServiceTypesCache() {
		code2TypeCache = new HashMap<Integer, ServiceType>();
		List<ServiceType> types = serviceDaoExt.getServiceTypes();
		for (ServiceType type : types) {
			code2TypeCache.put(type.getCode(), type);
		}
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
		Service service = serviceDaoExt.findService(provider.getId(), type.getId());
		if (service == null) {
			throw new IllegalArgumentException(
					"Cannot find service (code " + type.getCode() + ") for provider" + provider.getId());
		}

		return service;
	}

	/**
	 * Get record type by type id
	 *
	 * @param typeId Record type enum id
	 * @return record type
	 */
	public AccountRecordType getRecordType(int typeId) {
		AccountRecordType type = serviceDaoExt.findRecordType(typeId);
		if (type == null) {
			throw new IllegalArgumentException("Cannot find record type #" + typeId);
		}

		return type;
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
