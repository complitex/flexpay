package org.flexpay.payments.actions.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.flexpay.common.service.RegistryTypeService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.filters.RecipientOrganizationFilter;
import org.flexpay.orgs.persistence.filters.SenderOrganizationFilter;
import org.flexpay.orgs.service.OrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

import static org.apache.commons.lang.time.DateUtils.addDays;
import static org.flexpay.common.util.DateUtil.getEndOfThisDay;
import static org.flexpay.common.util.DateUtil.now;
import static org.flexpay.common.util.DateUtil.truncateDay;

public class RegistriesListPageAction extends FPActionSupport {

	private OrganizationFilter senderOrganizationFilter = new SenderOrganizationFilter();
	private OrganizationFilter recipientOrganizationFilter = new RecipientOrganizationFilter();
	private RegistryTypeFilter registryTypeFilter = new RegistryTypeFilter();
	private Date fromDate = truncateDay(addDays(now(), -2));
	private Date tillDate = getEndOfThisDay(now());

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

	public OrganizationFilter getRecipientOrganizationFilter() {
		return recipientOrganizationFilter;
	}

	public RegistryTypeFilter getRegistryTypeFilter() {
		return registryTypeFilter;
	}

	public String getFromDate() {
		return format(fromDate);
	}

	public String getTillDate() {
		return format(tillDate);
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setRegistryTypeService(RegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
	}

}
