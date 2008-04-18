package org.flexpay.eirc.actions.registry;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.OrganisationService;
import org.flexpay.eirc.service.SpRegistryTypeService;

import java.util.*;
import java.text.ParseException;

public class ListRegistriesAction extends FPActionSupport {

	private OrganisationFilter senderOrganisationFilter = new OrganisationFilter();
	private OrganisationFilter recipientOrganisationFilter = new OrganisationFilter();
	private RegistryTypeFilter registryTypeFilter = new RegistryTypeFilter();
	private Date fromDate = DateUtils.truncate(new Date(), Calendar.MONTH);
	private Date tillDate = new Date();
	private Page pager = new Page();

	private List<SpRegistry> registries;

	private OrganisationService organisationService;
	private SpRegistryService registryService;
	private SpRegistryTypeService registryTypeService;

	/**
	 *
	 */
	public String execute() throws Exception {

		try {
			organisationService.initFilter(senderOrganisationFilter);
			organisationService.initFilter(recipientOrganisationFilter);

			registryTypeService.initFilter(registryTypeFilter);

			registries = registryService.findObjects(senderOrganisationFilter, recipientOrganisationFilter,
					registryTypeFilter, fromDate, tillDate, pager);
		} catch (Exception e) {
			log.error("unexpected error", e);
		}

		return SUCCESS;
	}

	public List<SpRegistry> getRegistries() {
		return registries;
	}

	public void setRegistries(List<SpRegistry> registries) {
		this.registries = registries;
	}

	public OrganisationFilter getSenderOrganisationFilter() {
		return senderOrganisationFilter;
	}

	public void setSenderOrganisationFilter(OrganisationFilter senderOrganisationFilter) {
		this.senderOrganisationFilter = senderOrganisationFilter;
	}

	public OrganisationFilter getRecipientOrganisationFilter() {
		return recipientOrganisationFilter;
	}

	public void setRecipientOrganisationFilter(OrganisationFilter recipientOrganisationFilter) {
		this.recipientOrganisationFilter = recipientOrganisationFilter;
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

	public void setFromDate(String fromDate) {
		try {
			this.fromDate = DateIntervalUtil.parse(fromDate);
		} catch (ParseException e) {
			this.fromDate = DateUtils.truncate(new Date(), Calendar.MONTH);
		}
	}

	public String getTillDate() {
		return format(tillDate);
	}

	public void setTillDate(String tillDate) {
		try {
			this.tillDate = DateIntervalUtil.parse(tillDate);
		} catch (ParseException e) {
			this.tillDate = new Date();
		}
	}

	public void setRegistryService(SpRegistryService registryService) {
		this.registryService = registryService;
	}

	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}

	public void setRegistryTypeService(SpRegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
	}

	public Page getPager() {
		return pager;
	}

	public void setPager(Page pager) {
		this.pager = pager;
	}
}
