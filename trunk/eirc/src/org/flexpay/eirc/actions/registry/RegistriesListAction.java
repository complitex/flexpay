package org.flexpay.eirc.actions.registry;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;
import org.flexpay.eirc.service.OrganizationService;
import org.flexpay.eirc.service.RegistryService;
import org.flexpay.eirc.service.SpRegistryTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

public class RegistriesListAction extends FPActionWithPagerSupport {

	private OrganizationFilter senderOrganizationFilter = new OrganizationFilter();
	private OrganizationFilter recipientOrganizationFilter = new OrganizationFilter();
	private RegistryTypeFilter registryTypeFilter = new RegistryTypeFilter();
	private Date fromDate = DateUtils.addMonths(DateUtil.currentMonth(), -2);
	private Date tillDate = new Date();

	private List<SpRegistry> registries;

	private OrganizationService organizationService;
	private RegistryService registryService;
	private SpRegistryTypeService registryTypeService;

	@NotNull
	public String doExecute() throws Exception {

		organizationService.initFilter(senderOrganizationFilter);
		organizationService.initFilter(recipientOrganizationFilter);

		registryTypeService.initFilter(registryTypeFilter);

		registries = registryService.findObjects(senderOrganizationFilter, recipientOrganizationFilter,
				registryTypeFilter, fromDate, tillDate, getPager());

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

	public List<SpRegistry> getRegistries() {
		return registries;
	}

	public void setRegistries(List<SpRegistry> registries) {
		this.registries = registries;
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

	public String getFromDate() {
		return format(fromDate);
	}

	public void setFromDate(String dt) {
		fromDate = DateUtil.parseDate(dt, DateUtil.currentMonth());
	}

	public String getTillDate() {
		return format(tillDate);
	}

	public void setTillDate(String dt) {
		tillDate = DateUtil.parseDate(dt, DateUtil.now());
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setRegistryTypeService(SpRegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
	}

}
