package org.flexpay.eirc.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.dao.ServiceTypeDao;
import org.flexpay.eirc.dao.ServiceDaoExt;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.ServiceTypeNameTranslation;
import org.flexpay.eirc.service.ServiceTypeService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

@Transactional(readOnly = true)
public class ServiceTypeServiceImpl implements ServiceTypeService {

	private Logger log = Logger.getLogger(getClass());

	private ServiceTypeDao serviceTypeDao;
	private ServiceDaoExt serviceDaoExt;

	/**
	 * Disable service types
	 *
	 * @param objectIds Identifiers of service type to disable
	 */
	@Transactional(readOnly = false)
	public void disable(Set<Long> objectIds) {
		log.info("Disabling service types");
		for (Long id : objectIds) {
			ServiceType serviceType = serviceTypeDao.read(id);
			if (serviceType != null) {
				serviceType.disable();
				serviceTypeDao.update(serviceType);
			}
		}
	}

	/**
	 * List service types
	 *
	 * @param pager Page
	 * @return list of Service types for current page
	 */
	public List<ServiceType> listServiceTypes(Page<ServiceType> pager) {
		log.info("Listing service types by paging");

		List<ServiceType> types = serviceTypeDao.findServiceTypes(pager);
		updateCaches(types);

		return types;
	}

	/**
	 * Read full service type info
	 *
	 * @param serviceType ServiceType stub
	 * @return ServiceType
	 */
	public ServiceType read(ServiceType serviceType) {
		log.info("Reading service type");
		if (serviceType.isNotNew()) {
			return serviceTypeDao.readFull(serviceType.getId());
		}

		return new ServiceType(0L);
	}

	/**
	 * Save ServiceType object
	 *
	 * @param type ServiceType
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation error occurs
	 */
	@Transactional(readOnly = false)
	public void save(ServiceType type) throws FlexPayExceptionContainer {
		log.info("Saving service types");
		validate(type);
		if (type.isNew()) {
			type.setId(null);
			serviceTypeDao.create(type);
		} else {
			serviceTypeDao.update(type);
		}
	}

	private void validate(ServiceType type) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultNameFound = false;
		for (ServiceTypeNameTranslation translation : type.getTypeNames()) {
			if (log.isDebugEnabled()) {
				log.debug("Validating translation: " + translation);
			}
			boolean nameBlank = StringUtils.isBlank(translation.getName());
			if (translation.getLang().isDefault() && !nameBlank) {
				defaultNameFound = true;
			}
			boolean descBlank = StringUtils.isBlank(translation.getDescription());
			if (nameBlank || descBlank) {
				container.addException(new FlexPayException("Need both name and desc for lang",
						"eirc.error.service_type.need_both_name_and_description"));
			}
		}
		if (!defaultNameFound) {
			container.addException(new FlexPayException(
					"No default lang desc", "eirc.error.service_type.no_default_lang_name"));
		}

		if (type.getCode() == 0) {
			container.addException(new FlexPayException(
					"No code specified", "eirc.error.service_type.no_code"));
		} else {
			// check if code was not changed and is a unique one
			try {
				ServiceType typeByCode = code2TypeCache.get(type.getCode());
				if (typeByCode != null && (type.isNew() || !typeByCode.getId().equals(type.getId()))) {
					container.addException(new FlexPayException("Not unique code",
							"eirc.error.service_type.not_unique_code"));
				}
			} catch (IllegalArgumentException e) {
				// nothing to do
			}

		}

		if (!container.isEmpty()) {
			throw container;
		}
	}

	/**
	 * Find service type by its code
	 *
	 * @param code Service type code
	 * @return Service type if found
	 * @throws IllegalArgumentException if the <code>code</code> is invalid
	 */
	public ServiceType getServiceType(int code) throws IllegalArgumentException {
		log.info("Getting service type by code");
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

	/**
	 * Read service type details
	 *
	 * @param typeStub Service type stub
	 * @return Service type
	 */
	public ServiceType getServiceType(ServiceType typeStub) {
		log.info("Getting service type by stub");
		if (id2TypeCache == null) {
			initializeServiceTypesCache();
		}
		if (id2TypeCache.containsKey(typeStub.getId())) {
			return id2TypeCache.get(typeStub.getId());
		}

		throw new IllegalArgumentException("Cannot find service type with id " + typeStub.getId());
	}

	private Map<Integer, ServiceType> code2TypeCache;
	private Map<Long, ServiceType> id2TypeCache;

	private void initializeServiceTypesCache() {
		log.info("Initialising caches");
		List<ServiceType> types = serviceTypeDao.findServiceTypes(new Page<ServiceType>(10000, 1));
		updateCaches(types);
	}

	private void updateCaches(List<ServiceType> types) {
		log.info("Updating caches");
		if (code2TypeCache == null) {
			code2TypeCache = new HashMap<Integer, ServiceType>();
			id2TypeCache = new HashMap<Long, ServiceType>();
		}

		for (ServiceType type : types) {
			code2TypeCache.put(type.getCode(), type);
			id2TypeCache.put(type.getId(), type);
		}
	}

	public void setServiceDaoExt(ServiceDaoExt serviceDaoExt) {
		this.serviceDaoExt = serviceDaoExt;
	}

	public void setServiceTypeDao(ServiceTypeDao serviceTypeDao) {
		this.serviceTypeDao = serviceTypeDao;
	}
}
