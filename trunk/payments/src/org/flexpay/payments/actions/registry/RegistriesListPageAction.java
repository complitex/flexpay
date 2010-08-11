package org.flexpay.payments.actions.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.common.service.RegistryTypeService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.filters.RecipientOrganizationFilter;
import org.flexpay.orgs.persistence.filters.SenderOrganizationFilter;
import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

import static org.apache.commons.lang.time.DateUtils.addDays;
import static org.flexpay.common.util.DateUtil.*;

public class RegistriesListPageAction extends AccountantAWPActionSupport {

	private OrganizationFilter senderOrganizationFilter = new SenderOrganizationFilter();
	private OrganizationFilter recipientOrganizationFilter = new RecipientOrganizationFilter();
	private RegistryTypeFilter registryTypeFilter = new RegistryTypeFilter();
    private ServiceProviderFilter serviceProviderFilter = new ServiceProviderFilter();
	private Date fromDate = truncateDay(addDays(now(), -2));
	private Date tillDate = getEndOfThisDay(now());

    private ServiceProviderService serviceProviderService;
	private OrganizationService organizationService;
	private RegistryTypeService registryTypeService;

	/**
	* Perform action execution.
	* <p/>
	* If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	*
	* @return execution result code
	* @throws Exception if failure occurs
	*/
	@NotNull
	@Override
	protected String doExecute() throws Exception {

        serviceProviderService.initServiceProvidersFilter(serviceProviderFilter);
        serviceProviderFilter.setNeedAutoChange(false);

		List<Organization> orgs = organizationService.listOrganizations();

		StopWatch watch = new StopWatch();
		if (log.isDebugEnabled()) {
			watch.start();
		}
		senderOrganizationFilter.setOrganizations(orgs);
		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Time spent initializing sender filter: {}", watch);
			watch.reset();
			watch.start();
		}
		recipientOrganizationFilter.setOrganizations(orgs);
		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Time spent initializing recipient filter: {}", watch);
		}

		registryTypeService.initFilter(registryTypeFilter);

		return SUCCESS;
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
		return SUCCESS;
	}

    public OrganizationFilter getSenderOrganizationFilter() {
        return senderOrganizationFilter;
    }

    public void setSenderOrganizationFilter(OrganizationFilter senderOrganizationFilter) {
        this.senderOrganizationFilter = senderOrganizationFilter;
    }

    public OrganizationFilter getRecipientOrganizationFilter() {
        return recipientOrganizationFilter;
    }

    public void setRecipientOrganizationFilter(OrganizationFilter recipientOrganizationFilter) {
        this.recipientOrganizationFilter = recipientOrganizationFilter;
    }

    public RegistryTypeFilter getRegistryTypeFilter() {
        return registryTypeFilter;
    }

    public void setRegistryTypeFilter(RegistryTypeFilter registryTypeFilter) {
        this.registryTypeFilter = registryTypeFilter;
    }

    public ServiceProviderFilter getServiceProviderFilter() {
        return serviceProviderFilter;
    }

    public void setServiceProviderFilter(ServiceProviderFilter serviceProviderFilter) {
        this.serviceProviderFilter = serviceProviderFilter;
    }

    public void setFromDate(String dt) {
		fromDate = truncateDay(parseDate(dt, currentMonth()));
	}

    public String getFromDate() {
		return format(fromDate);
	}

    public void setTillDate(String dt) {
        tillDate = getEndOfThisDay(parseDate(dt, now()));
    }

	public String getTillDate() {
		return format(tillDate);
	}

    public RegistryType getTypeByCode(Integer code) {
        return registryTypeService.findByCode(code);
    }

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setRegistryTypeService(RegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
	}

    @Required
    public void setServiceProviderService(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }
}
