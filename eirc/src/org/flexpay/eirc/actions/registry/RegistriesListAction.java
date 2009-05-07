package org.flexpay.eirc.actions.registry;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryProperties;
import org.flexpay.common.service.RegistryTypeService;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.persistence.EircRegistryProperties;
import org.flexpay.eirc.service.EircRegistryService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.service.OrganizationService;
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

	private List<Registry> registries;

	private OrganizationService organizationService;
	private EircRegistryService eircRegistryService;
	private RegistryTypeService registryTypeService;

	@NotNull
	public String doExecute() throws Exception {

		organizationService.initFilter(senderOrganizationFilter);
		organizationService.initFilter(recipientOrganizationFilter);

		registryTypeService.initFilter(registryTypeFilter);

		registries = eircRegistryService.findObjects(senderOrganizationFilter, recipientOrganizationFilter,
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

	public List<Registry> getRegistries() {
		return registries;
	}

	public void setRegistries(List<Registry> registries) {
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
		log.debug("dt = {}, fromDate = {}", dt, fromDate);
	}

	public String getTillDate() {
		return format(tillDate);
	}

	public void setTillDate(String dt) {
		tillDate = DateUtil.parseDate(dt, DateUtil.now());
		log.debug("dt = {}, fromDate = {}", dt, tillDate);
	}

	public Organization getSenderOrg(RegistryProperties properties) {
		EircRegistryProperties props = (EircRegistryProperties) properties;
		return organizationService.readFull(props.getSenderStub());
	}

	public Organization getRecipientOrg(RegistryProperties properties) {
		EircRegistryProperties props = (EircRegistryProperties) properties;
		return organizationService.readFull(props.getRecipientStub());
	}

	@Required
	public void setEircRegistryService(EircRegistryService eircRegistryService) {
		this.eircRegistryService = eircRegistryService;
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
