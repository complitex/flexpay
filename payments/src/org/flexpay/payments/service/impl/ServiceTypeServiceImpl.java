package org.flexpay.payments.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.payments.dao.ServiceDaoExt;
import org.flexpay.payments.dao.ServiceTypeDao;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceTypeNameTranslation;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class ServiceTypeServiceImpl implements ServiceTypeService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ServiceTypeDao serviceTypeDao;
	private ServiceDaoExt serviceDaoExt;

	private SessionUtils sessionUtils;
	private ModificationListener<ServiceType> modificationListener;

	/**
	 * Disable service types
	 *
	 * @param objectIds Identifiers of service type to disable
	 */
	@Transactional (readOnly = false)
    @Override
	public void disable(Set<Long> objectIds) {
		log.debug("Disabling service types");
		for (Long id : objectIds) {
			ServiceType serviceType = serviceTypeDao.read(id);
			if (serviceType != null) {
				serviceType.disable();
				serviceTypeDao.update(serviceType);

				modificationListener.onDelete(serviceType);
				log.debug("Disabled service type: {}", serviceType);
			}
		}
	}

	/**
	 * List service types
	 *
	 * @param pager Page
	 * @return list of Service types for current page
	 */
    @Override
	public List<ServiceType> listServiceTypes(Page<ServiceType> pager) {
		return serviceTypeDao.findServiceTypes(pager);
	}

    @Override
	public List<ServiceType> getAllServiceTypes() {
		return serviceTypeDao.findAllServiceTypes();
	}

	/**
	 * Read full service type info
	 *
	 * @param stub Service type stub
	 * @return ServiceType
	 */
    @Override
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
    @Override
	public ServiceType create(@NotNull ServiceType type) throws FlexPayExceptionContainer {

		validate(type);
		type.setId(null);
		serviceTypeDao.create(type);

		modificationListener.onCreate(type);

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
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
    @Override
	public ServiceType update(@NotNull ServiceType type) throws FlexPayExceptionContainer {

		validate(type);

		if (type.isNew()) {
			throw new FlexPayExceptionContainer(new FlexPayException("New", "common.error.update_new"));
		}

		ServiceType old = read(Stub.stub(type));
		if (old == null) {
			throw new FlexPayExceptionContainer(new FlexPayException("No object found to update " + type));
		}

		sessionUtils.evict(old);
		modificationListener.onUpdate(old, type);

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
				container.addException(new FlexPayException(
						"Need name and desc, was: '" + translation.getName() + "' and '" + translation.getDescription() + "'",
						"eirc.error.service_type.need_both_name_and_description"));
			}
		}
		if (!defaultNameFound) {
			container.addException(new FlexPayException(
					"No default lang name", "eirc.error.service_type.no_default_lang_name"));
		}

		if (type.getCode() == 0) {
			container.addException(new FlexPayException(
					"No code specified", "eirc.error.service_type.no_code"));
		} else {
			// check if code was not changed and is a unique one
			try {
				ServiceType typeByCode = getServiceType(type.getCode());
				if (typeByCode != null && (type.isNew() || !typeByCode.equals(type))) {
					container.addException(new FlexPayException("Not unique code: " + type.getCode(),
							"eirc.error.service_type.not_unique_code"));
				}
				sessionUtils.evict(typeByCode);
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
	@NotNull
    @Override
	public ServiceType getServiceType(int code) throws IllegalArgumentException {

		log.debug("Getting service type by code {}", code);
		ServiceType type = serviceDaoExt.findByCode(code);
		if (type == null) {
			throw new IllegalArgumentException("Cannot find service type with code #" + code);
		}

		return type;
	}

    @NotNull
    @Override
    public List<ServiceType> getByCodes(Collection<Integer> codes) {
        return serviceTypeDao.findByCodes(codes);
    }

    /**
	 * Initialize filter
	 *
	 * @param serviceTypeFilter Filter to initialize
	 * @return Filter back
	 */
    @Override
	public ServiceTypeFilter initFilter(ServiceTypeFilter serviceTypeFilter) {

		serviceTypeFilter.setServiceTypes(getAllServiceTypes());

		return serviceTypeFilter;
	}

	@Transactional (readOnly = false)
	@Override
	public void delete(@NotNull ServiceType type) {
		serviceTypeDao.delete(type);
	}

	@Required
	public void setServiceDaoExt(ServiceDaoExt serviceDaoExt) {
		this.serviceDaoExt = serviceDaoExt;
	}

	@Required
	public void setServiceTypeDao(ServiceTypeDao serviceTypeDao) {
		this.serviceTypeDao = serviceTypeDao;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<ServiceType> modificationListener) {
		this.modificationListener = modificationListener;
	}
}
