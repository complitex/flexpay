package org.flexpay.payments.actions.service;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.MeasureUnitFilter;
import org.flexpay.common.service.MeasureUnitService;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceDescription;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.filters.ServiceFilter;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.actions.interceptor.CashboxAware;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class ServiceEditAction extends FPActionSupport implements CashboxAware {

	private Service service = new Service(0L);

	private ServiceProviderFilter serviceProviderFilter = new ServiceProviderFilter();
	private ServiceTypeFilter serviceTypeFilter = new ServiceTypeFilter();
	private MeasureUnitFilter measureUnitFilter = new MeasureUnitFilter();
	private ServiceFilter parentServiceFilter = new ServiceFilter();
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();

	private Map<Long, String> descriptions = map();

	private Long cashboxId;

	private String crumbCreateKey;
	private SPService spService;
	private ServiceProviderService providerService;
	private ServiceTypeService serviceTypeService;
	private MeasureUnitService measureUnitService;

	@NotNull
	public String doExecute() throws Exception {

		if (service.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		Service srvc = service.isNew() ? service : spService.read(stub(service));
		if (srvc == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		serviceProviderFilter = providerService.initServiceProvidersFilter(serviceProviderFilter);
		serviceTypeFilter = serviceTypeService.initFilter(serviceTypeFilter);
		parentServiceFilter = spService.initParentServicesFilter(parentServiceFilter);
		measureUnitFilter = measureUnitService.initFilter(measureUnitFilter);

		if (!isSubmit()) {
			service = srvc;
			init();
			return INPUT;
		}

		Service parentService = new Service(parentServiceFilter.getSelectedId());
		if (parentService.isNotNew()) {
			srvc.setParentService(parentService);
		}
		srvc.setBeginDate(beginDateFilter.getDate());
		srvc.setEndDate(endDateFilter.getDate());
		srvc.setServiceProvider(new ServiceProvider(serviceProviderFilter.getSelectedId()));
		srvc.setServiceType(new ServiceType(serviceTypeFilter.getSelectedId()));
		MeasureUnit unit = measureUnitFilter.needFilter() ?
						   new MeasureUnit(measureUnitFilter.getSelectedStub().getId()) : null;
		srvc.setMeasureUnit(unit);
		srvc.setExternalCode(service.getExternalCode());

		for (Map.Entry<Long, String> name : descriptions.entrySet()) {
			ServiceDescription description = new ServiceDescription();
			description.setLang(getLang(name.getKey()));
			description.setName(name.getValue());

			log.debug("Setting service description: {}", description);

			srvc.setDescription(description);
		}

		spService.save(srvc);

		log.debug("Service saved!");
		return REDIRECT_SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return INPUT;
	}

	private void init() {
		for (ServiceDescription description : service.getDescriptions()) {
			descriptions.put(description.getLang().getId(), description.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (descriptions.containsKey(lang.getId())) {
				continue;
			}
			descriptions.put(lang.getId(), "");
		}

		if (service.isNotNew()) {
			serviceProviderFilter.setSelectedId(service.getServiceProvider().getId());
			serviceTypeFilter.setSelectedId(service.getServiceType().getId());

			beginDateFilter.setDate(service.getBeginDate());
			endDateFilter.setDate(service.getEndDate());

			if (service.isSubService()) {
				parentServiceFilter.setSelectedId(service.getParentService().getId());
			}

			if (service.getMeasureUnit() != null) {
				measureUnitFilter.setSelectedId(service.getMeasureUnit().getId());
			}
		}
	}

	@Override
	protected void setBreadCrumbs() {
		if (service.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public ServiceProviderFilter getServiceProviderFilter() {
		return serviceProviderFilter;
	}

	public void setServiceProviderFilter(ServiceProviderFilter serviceProviderFilter) {
		this.serviceProviderFilter = serviceProviderFilter;
	}

	public ServiceTypeFilter getServiceTypeFilter() {
		return serviceTypeFilter;
	}

	public void setServiceTypeFilter(ServiceTypeFilter serviceTypeFilter) {
		this.serviceTypeFilter = serviceTypeFilter;
	}

	public ServiceFilter getParentServiceFilter() {
		return parentServiceFilter;
	}

	public void setParentServiceFilter(ServiceFilter parentServiceFilter) {
		this.parentServiceFilter = parentServiceFilter;
	}

	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public EndDateFilter getEndDateFilter() {
		return endDateFilter;
	}

	public void setEndDateFilter(EndDateFilter endDateFilter) {
		this.endDateFilter = endDateFilter;
	}

	public MeasureUnitFilter getMeasureUnitFilter() {
		return measureUnitFilter;
	}

	public void setMeasureUnitFilter(MeasureUnitFilter measureUnitFilter) {
		this.measureUnitFilter = measureUnitFilter;
	}

	public Map<Long, String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Map<Long, String> descriptions) {
		this.descriptions = descriptions;
	}

	public Long getCashboxId() {
		return cashboxId;
	}

	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

	@Required
	public void setMeasureUnitService(MeasureUnitService measureUnitService) {
		this.measureUnitService = measureUnitService;
	}

	@Required
	public void setProviderService(ServiceProviderService providerService) {
		this.providerService = providerService;
	}

}
