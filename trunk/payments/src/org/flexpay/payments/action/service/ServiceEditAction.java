package org.flexpay.payments.action.service;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.MeasureUnitFilter;
import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceDescription;
import org.flexpay.payments.persistence.filters.ServiceFilter;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.DateUtil.now;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

/**
 * Service simple editor
 */
public class ServiceEditAction extends FPActionSupport {

	private Service service = new Service();

	private ServiceProviderFilter serviceProviderFilter = new ServiceProviderFilter();
	private ServiceTypeFilter serviceTypeFilter = new ServiceTypeFilter();
	private MeasureUnitFilter measureUnitFilter = new MeasureUnitFilter();
	private ServiceFilter parentServiceFilter = new ServiceFilter();
	private BeginDateFilter beginDateFilter = new BeginDateFilter(now());
	private EndDateFilter endDateFilter = new EndDateFilter();

	private Map<Long, String> names = map();

	private String crumbCreateKey;
	private SPService spService;
	private ServiceProviderService providerService;
	private ServiceTypeService serviceTypeService;
	private MeasureUnitService measureUnitService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

        if (service == null || service.getId() == null) {
            log.warn("Incorrect service id");
            addActionError(getText("payments.error.service.incorrect_service_id"));
            return REDIRECT_ERROR;
        }

        Service oldService = service.isNew() ? service : spService.readFull(stub(service));
        if (oldService == null) {
            addActionError(getText("common.error.object_not_selected"));
            return REDIRECT_SUCCESS;
        }

        if (isNotSubmit()) {
            if (service.isNotNew()) {
                Stub<Service> stub = stub(service);
                service = spService.readFull(stub);

                if (service == null) {
                    log.warn("Can't get service with id {} from DB", stub.getId());
                    addActionError(getText("payments.error.service.cant_get_service"));
                    return REDIRECT_ERROR;
                } else if (service.isNotActive()) {
                    log.warn("Service with id {} is disabled", stub.getId());
                    addActionError(getText("payments.error.service.cant_get_service"));
                    return REDIRECT_ERROR;
                }

            }

            service = oldService;

            correctNames();
            initFilters();
            initData();

            return INPUT;
        }

        if (!doValidate()) {
            return INPUT;
        }

        for (Map.Entry<Long, String> name : names.entrySet()) {
            String value = name.getValue();
            Language lang = getLang(name.getKey());
            oldService.setDescription(new ServiceDescription(value, lang));
        }

        if (parentServiceFilter.needFilter()) {
            oldService.setParentService(spService.readFull(parentServiceFilter.getSelectedStub()));
        } else {
            oldService.setParentService(null);
        }
        oldService.setCanReturn(service.getCanReturn());
        oldService.setExternalCode(service.getExternalCode());
        oldService.setBeginDate(beginDateFilter.getDate());
        oldService.setEndDate(endDateFilter.getDate());
        oldService.setServiceProvider(providerService.read(serviceProviderFilter.getSelectedStub()));
        oldService.setServiceType(serviceTypeService.read(serviceTypeFilter.getSelectedStub()));
        MeasureUnit unit = measureUnitFilter.needFilter() ?
                           measureUnitService.readFull(measureUnitFilter.getSelectedStub()) : null;
        oldService.setMeasureUnit(unit);

        if (oldService.isNew()) {
            spService.create(oldService);
        } else {
            spService.update(oldService);
        }

        log.debug("service = {}", oldService);

        addActionMessage(getText("payments.service.saved"));

        return REDIRECT_SUCCESS;

	}

    private boolean doValidate() {

        if (parentServiceFilter == null) {
            log.warn("ParentServiceFilter is not correct");
            addActionError(getText("payments.error.service.parent_service_filter_is_not_correct"));
        }
        if (serviceProviderFilter == null || !serviceProviderFilter.needFilter()) {
            log.warn("ServiceProviderFilter is not correct");
            addActionError(getText("payments.error.service.service_provider_filter_is_not_correct"));
        }
        if (serviceTypeFilter == null || !serviceTypeFilter.needFilter()) {
            log.warn("ServiceProviderFilter is not correct");
            addActionError(getText("payments.error.service.service_type_filter_is_not_correct"));
        }
        if (beginDateFilter == null || !beginDateFilter.needFilter()) {
            log.warn("BeginDateFilter is not correct");
            addActionError(getText("payments.error.service.begin_date_filter_is_required"));
        }

        return !hasActionErrors();
    }

    private void correctNames() {
        if (names == null) {
            log.warn("Names parameter is null");
            names = treeMap();
        }
        Map<Long, String> newNames = treeMap();
        for (Language lang : getLanguages()) {
            newNames.put(lang.getId(), names.containsKey(lang.getId()) ? names.get(lang.getId()) : "");
        }
        names = newNames;
    }

    private void initFilters() throws Exception {

        if (beginDateFilter == null) {
            log.warn("BeginDateFilter parameter is null");
            beginDateFilter = new BeginDateFilter(now());
        }

        if (endDateFilter == null) {
            log.warn("EndDateFilter parameter is null");
            endDateFilter = new EndDateFilter();
        }

        providerService.initServiceProvidersFilter(serviceProviderFilter);
        serviceProviderFilter.setNeedAutoChange(false);
        serviceTypeService.initFilter(serviceTypeFilter);
        spService.initParentServicesFilter(parentServiceFilter);
        measureUnitService.initFilter(measureUnitFilter);
    }

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	private void initData() {

        for (ServiceDescription name : service.getDescriptions()) {
            names.put(name.getLang().getId(), name.getName());
        }

        for (Language lang : getLanguages()) {
            if (!names.containsKey(lang.getId())) {
                names.put(lang.getId(), "");
            }
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
		} else {
            beginDateFilter.setDate(now());
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

    public Map<Long, String> getNames() {
        return names;
    }

    public void setNames(Map<Long, String> names) {
        this.names = names;
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
