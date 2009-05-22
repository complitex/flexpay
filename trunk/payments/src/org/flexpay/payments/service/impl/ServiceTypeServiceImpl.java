package org.flexpay.payments.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.dao.ServiceDaoExt;
import org.flexpay.payments.dao.ServiceTypeDao;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceTypeNameTranslation;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class ServiceTypeServiceImpl implements ServiceTypeService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ServiceTypeDao serviceTypeDao;
	private ServiceDaoExt serviceDaoExt;

	/**
	 * Disable service types
	 *
	 * @param objectIds Identifiers of service type to disable
	 */
	@Transactional (readOnly = false)
	public void disable(Set<Long> objectIds) {
		log.debug("Disabling service types");
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

		return serviceTypeDao.findServiceTypes(pager);
	}

	public List<ServiceType> listAllServiceTypes() {

		return serviceTypeDao.findAllServiceTypes();
	}

	/**
	 * Read full service type info
	 *
	 * @param stub Service type stub
	 * @return ServiceType
	 */
	public ServiceType read(@NotNull Stub<ServiceType> stub) {
		log.debug("Reading service type {}", stub);
		return serviceTypeDao.readFull(stub.getId());
	}

	/**
	 * Save ServiceType object
	 *
	 * @param type ServiceType
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation error occurs
	 */
	@Transactional (readOnly = false)
	public ServiceType create(@NotNull ServiceType type) throws FlexPayExceptionContainer {

		validate(type);
		type.setId(null);
		serviceTypeDao.create(type);

		return type;
	}

	/**
	 * Save ServiceType object
	 *
	 * @param type ServiceType
	 * @return Updated object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation error occurs
	 */
	public ServiceType update(@NotNull ServiceType type) throws FlexPayExceptionContainer {

		validate(type);
		serviceTypeDao.update(type);

		return type;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(ServiceType type) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultNameFound = false;
		for (ServiceTypeNameTranslation translation : type.getTypeNames()) {
			log.debug("Validating translation: {}", translation);
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
				ServiceType typeByCode = getServiceType(type.getCode());
				if (typeByCode != null && (type.isNew() || !typeByCode.equals(type))) {
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

		ServiceType type = serviceDaoExt.findByCode(code);
		if (type == null) {
			throw new IllegalArgumentException("Cannot find service type with code #" + code);
		}

		return type;
	}

	/**
	 * Initialize filter
	 *
	 * @param serviceTypeFilter Filter to initialize
	 * @return Filter back
	 */
	public ServiceTypeFilter initFilter(ServiceTypeFilter serviceTypeFilter) {

		serviceTypeFilter.setServiceTypes(listAllServiceTypes());

		return serviceTypeFilter;
	}

	public void setServiceDaoExt(ServiceDaoExt serviceDaoExt) {
		this.serviceDaoExt = serviceDaoExt;
	}

	public void setServiceTypeDao(ServiceTypeDao serviceTypeDao) {
		this.serviceTypeDao = serviceTypeDao;
	}
}
