package org.flexpay.eirc.actions;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.eirc.persistence.filters.ServiceOrganisationFilter;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class GenerateQuitancesAction extends FPActionSupport {

	private ServiceOrganisationService serviceOrganisationService;
	private ProcessManager processManager;

	private ServiceOrganisationFilter serviceOrganisationFilter =
			new ServiceOrganisationFilter();

	private BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtils.truncate(new Date(), Calendar.MONTH));
	private EndDateFilter endDateFilter = new EndDateFilter(DateUtil.now());

	@NotNull
	public String doExecute() throws Exception {

		if (isSubmit()) {

			if (!serviceOrganisationFilter.needFilter()) {
				addActionError(getText("eirc.error.quittance.no_service_organisation"));
			} else {

				Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

				contextVariables.put("dateFrom", beginDateFilter.getDate());
				contextVariables.put("dateTill", endDateFilter.getDate());
				contextVariables.put("serviceOrganisationId", serviceOrganisationFilter.getSelectedId());

				processManager.createProcess("GenerateQuitances", contextVariables);

				addActionError(getText("eirc.quittance.generation_started"));
			}
		}

		serviceOrganisationService.initServiceOrganisationsFilter(serviceOrganisationFilter);

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
	protected String getErrorResult() {
		return SUCCESS;
	}

	public ServiceOrganisationFilter getServiceOrganisationFilter() {
		return serviceOrganisationFilter;
	}

	public void setServiceOrganisationFilter(ServiceOrganisationFilter serviceOrganisationFilter) {
		this.serviceOrganisationFilter = serviceOrganisationFilter;
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

	public void setServiceOrganisationService(
			ServiceOrganisationService serviceOrganisationService) {
		this.serviceOrganisationService = serviceOrganisationService;
	}

	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
