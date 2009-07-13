package org.flexpay.payments.util;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.service.Security;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Mapper maps Megabank service type codes to internal {@link org.flexpay.payments.persistence.ServiceType}
 */
public class ServiceTypesMapper {

	private BidiMap mapping = new DualHashBidiMap();
	private ServiceTypeService serviceTypeService;

	/**
	 * Map megabank service type code to internal ServiceType object reference
	 *
	 * @param mbCode Megabank service type code
	 * @return Service type stub
	 */
	public Stub<ServiceType> getInternalType(String mbCode) {
		//noinspection unchecked
		return (Stub<ServiceType>) mapping.get(mbCode);
	}

	/**
	 * Map internal service type to Megabank code
	 *
	 * @param stub Internal service type stub
	 * @return Megabank service type code
	 */
	public String getMegabankCode(Stub<ServiceType> stub) {
		return (String) mapping.getKey(stub);
	}

	public void validate() {

		Authentication auth = SecurityUtil.getAuthentication();
		Security.authenticateSyncer();
		List<Long> invalidTypes = CollectionUtils.list();
		for (Object mbCode : mapping.keySet()) {
			@SuppressWarnings ({"unchecked"})
			Stub<ServiceType> stub = (Stub<ServiceType>) mapping.get(mbCode);
			if (serviceTypeService.read(stub) == null) {
				invalidTypes.add(stub.getId());
			}
		}
		SecurityUtil.setAuthentication(auth);

		if (!invalidTypes.isEmpty()) {
			throw new IllegalArgumentException("Unknown service types: " + invalidTypes);
		}
	}

	@Required
	public void setMapping(Map<String, Long> mapping) {
		Set<Long> knownTypes = CollectionUtils.set();
		Set<Long> duplicateTypes = CollectionUtils.set();
		for (Map.Entry<String, Long> entry : mapping.entrySet()) {
			Stub<ServiceType> stub = new Stub<ServiceType>(entry.getValue());
			this.mapping.put(entry.getKey(), stub);
			if (knownTypes.contains(stub.getId())) {
				duplicateTypes.add(stub.getId());
			}
			knownTypes.add(stub.getId());
		}
		if (!duplicateTypes.isEmpty()) {
			throw new IllegalArgumentException("Duplicate service types: " + duplicateTypes);
		}
	}

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}
}
