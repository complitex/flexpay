package org.flexpay.eirc.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.eirc.dao.ServiceDao;
import org.flexpay.eirc.dao.ServiceDaoExt;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceDescription;
import org.flexpay.eirc.persistence.filters.ParentServiceFilterMarker;
import org.flexpay.eirc.persistence.filters.ServiceFilter;
import org.flexpay.eirc.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional (readOnly = true)
public class SPServiceImpl implements SPService {

	private ServiceDaoExt serviceDaoExt;
	private ServiceDao serviceDao;

	private SessionUtils sessionUtils;

	private MeasureUnitService measureUnitService;

	/**
	 * List active services using filters and pager
	 *
	 * @param filters Set of filters to apply
	 * @param pager   Page
	 * @return List of services
	 */
	public List<Service> listServices(List<ObjectFilter> filters, Page<Service> pager) {
		return serviceDaoExt.findServices(filters, pager);
	}

	/**
	 * Create or update service
	 *
	 * @param service Service to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	public void save(Service service) throws FlexPayExceptionContainer {
		validate(service);
		if (service.isNew()) {
			service.setId(null);
			serviceDao.create(service);
		} else {
			serviceDao.update(service);
		}
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(Service service) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (service.getServiceProvider().isNew()) {
			container.addException(new FlexPayException("Connot set empty service provider",
					"eirc.error.service.need_setup_service_provider"));
		}

		if (service.getServiceType().isNew()) {
			container.addException(new FlexPayException("Connot set empty service type",
					"eirc.error.service.need_setup_service_type"));
		}

		if (service.getParentService() != null && service.getParentService().isNew()) {
			container.addException(new FlexPayException("Connot set new parent service",
					"eirc.error.service.invalid_parent_service"));
		}

		if (!service.getChildServices().isEmpty() && service.isSubService()) {
			container.addException(new FlexPayException("Connot set subservices of subservice",
					"eirc.error.service.subservice_cannot_have_subservices"));
		}

		if (service.isSubService() && service.getServiceProvider().isNotNew() && service.getParentService().isNotNew()) {
			Service parentStub = service.getParentService();
			@SuppressWarnings ({"ConstantConditions"})
			Service parent = serviceDao.read(parentStub.getId());
			Long parentProviderId = parent.getServiceProviderStub().getId();
			if (!parentProviderId.equals(service.getServiceProvider().getId())) {
				container.addException(new FlexPayException("Subservice wrong provider",
						"eirc.error.service.invalid_parent_service_provider"));
			}
		}

		boolean defaultDescFound = false;
		for (ServiceDescription description : service.getDescriptions()) {
			if (description.getLang().isDefault() && StringUtils.isNotBlank(description.getName())) {
				defaultDescFound = true;
			}
		}
		if (!defaultDescFound) {
			container.addException(new FlexPayException(
					"No default lang desc", "eirc.error.service.no_default_lang_description"));
		}

		if (StringUtils.isNotBlank(service.getExternalCode())) {
			List<Service> services = serviceDao.findServicesByProviderCode(
					service.getServiceProvider().getId(), service.getExternalCode());
			boolean hasDuplicateCode = services.size() == 1 && !services.get(0).equals(service);
			hasDuplicateCode = hasDuplicateCode || services.size() >= 2;
			if (hasDuplicateCode) {
				container.addException(new FlexPayException(
						"Duplicate code", "eirc.error.service.duplicate_code"));
			}
			sessionUtils.evict(services);
		}

		List<Service> sameTypeSrvcs = serviceDaoExt.findIntersectingServices(
				service.getServiceProvider().getId(), service.getServiceType().getId(),
				service.getBeginDate(), service.getEndDate());
		boolean intersects = sameTypeSrvcs.size() == 1 && !sameTypeSrvcs.get(0).equals(service);
		intersects = intersects || sameTypeSrvcs.size() >= 2;
		if (intersects) {
			container.addException(new FlexPayException(
					"Duplicate service type", "eirc.error.service.duplicate_service_type"));
		}
		sessionUtils.evict(sameTypeSrvcs);

		if (!container.isEmpty()) {
			throw container;
		}
	}

	/**
	 * Read full service information
	 *
	 * @param stub Service stub
	 * @return Service description
	 */
	@Nullable
	public Service read(@NotNull Stub<Service> stub) {
		Service service = serviceDao.readFull(stub.getId());
		if (service != null && service.getMeasureUnit() != null) {
			Stub<MeasureUnit> unitStub = Stub.stub(service.getMeasureUnit());
			service.setMeasureUnit(measureUnitService.read(unitStub));
		}

		return service;
	}

	/**
	 * Initalize service filter with a list of parent services
	 *
	 * @param filter Filter to initialize
	 * @return Filter back
	 */
	public ServiceFilter initParentServicesFilter(ServiceFilter filter) {
		List<ObjectFilter> filters = new ArrayList<ObjectFilter>();
		filters.add(new ParentServiceFilterMarker());
		List<Service> services = serviceDaoExt.findServices(filters, new Page<Service>(10000, 1));
		filter.setServices(services);

		return filter;
	}

	@Required
	public void setServiceDaoExt(ServiceDaoExt serviceDaoExt) {
		this.serviceDaoExt = serviceDaoExt;
	}

	@Required
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setMeasureUnitService(MeasureUnitService measureUnitService) {
		this.measureUnitService = measureUnitService;
	}
}
