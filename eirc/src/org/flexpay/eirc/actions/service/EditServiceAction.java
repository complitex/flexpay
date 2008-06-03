package org.flexpay.eirc.actions.service;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceDescription;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.filters.ServiceFilter;
import org.flexpay.eirc.persistence.filters.ServiceProviderFilter;
import org.flexpay.eirc.persistence.filters.ServiceTypeFilter;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.service.ServiceTypeService;

import java.util.HashMap;
import java.util.Map;

public class EditServiceAction extends FPActionSupport {

	private SPService spService;
	private ServiceTypeService serviceTypeService;

	private Service service = new Service();

	private ServiceProviderFilter serviceProviderFilter = new ServiceProviderFilter();
	private ServiceTypeFilter serviceTypeFilter = new ServiceTypeFilter();
	private ServiceFilter parentServiceFilter = new ServiceFilter();
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();

	private Map<Long, String> descriptions = new HashMap<Long, String>();

	public String execute() throws Exception {

		if (service.getId() == null) {
			// todo: notify that no object was selected
			addActionError("No object was selected");
			log.warn("No id specified");
			return SUCCESS;
		}

		Service srvc = spService.read(service);

		serviceProviderFilter = spService.initServiceProvidersFilter(serviceProviderFilter);
		serviceTypeFilter = serviceTypeService.initFilter(serviceTypeFilter);
		parentServiceFilter = spService.initParentServicesFilter(parentServiceFilter);

		if (!isPost()) {
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

		try {
			spService.save(srvc);
		} catch (FlexPayExceptionContainer container) {
			addActionErrors(container);
			return INPUT;
		}

		log.warn("Saved!");
		return SUCCESS;
	}

	private void init() {
		for (ServiceDescription description : service.getDescriptions()) {
			descriptions.put(description.getLang().getId(), description.getName());
		}

		for (Language lang : ApplicationConfig.getInstance().getLanguages()) {
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
}
