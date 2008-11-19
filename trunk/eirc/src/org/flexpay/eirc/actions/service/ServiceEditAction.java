package org.flexpay.eirc.actions.service;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.MeasureUnitFilter;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceDescription;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.filters.ServiceFilter;
import org.flexpay.eirc.persistence.filters.ServiceProviderFilter;
import org.flexpay.eirc.persistence.filters.ServiceTypeFilter;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ServiceEditAction extends FPActionSupport {

	private SPService spService;
	private ServiceTypeService serviceTypeService;
	private MeasureUnitService measureUnitService;

	private Service service = new Service(0L);

	private ServiceProviderFilter serviceProviderFilter = new ServiceProviderFilter();
	private ServiceTypeFilter serviceTypeFilter = new ServiceTypeFilter();
	private MeasureUnitFilter measureUnitFilter = new MeasureUnitFilter();
	private ServiceFilter parentServiceFilter = new ServiceFilter();
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();

	private Map<Long, String> descriptions = map();

	@NotNull
	public String doExecute() throws Exception {

		if (service.getId() == null) {
			addActionError("No object was selected");
			return REDIRECT_SUCCESS;
		}

		Service srvc = service.isNew() ? service : spService.read(stub(service));

		serviceProviderFilter = spService.initServiceProvidersFilter(serviceProviderFilter);
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

			if (log.isDebugEnabled()) {
				log.debug("Setting service description: " + description);
			}

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

	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

	public void setMeasureUnitService(MeasureUnitService measureUnitService) {
		this.measureUnitService = measureUnitService;
	}
}
