package org.flexpay.eirc.actions.quittance;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.persistence.filters.ServiceOrganisationFilter;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class GenerateQuitancePDFAction extends FPActionSupport {

	private ServiceOrganisationService serviceOrganisationService;

	private ServiceOrganisationFilter serviceOrganisationFilter =
			new ServiceOrganisationFilter();

	private BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtils.truncate(new Date(), Calendar.MONTH));
	private EndDateFilter endDateFilter = new EndDateFilter(DateUtil.now());

	private ProcessManager processManager;

	@NotNull
	public String doExecute() throws Exception {

		if (isSubmit()) {

			if (!serviceOrganisationFilter.needFilter()) {
				addActionError(getText("eirc.error.quittance.no_service_organisation"));
			} else {
				Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

				contextVariables.put("serviceOrganisationId", serviceOrganisationFilter.getSelectedId());
				contextVariables.put("dateFrom", beginDateFilter.getDate());
				contextVariables.put("dateTill", endDateFilter.getDate());

				processManager.createProcess("GenerateQuitancePDF", contextVariables);

				addActionError(getText("eirc.quittance.printing_started"));
			}
		}

		serviceOrganisationService.initServiceOrganisationsFilter(serviceOrganisationFilter);

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

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in
	 * a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	/**
	 * @param serviceOrganisationService the serviceOrganisationService to set
	 */
	public void setServiceOrganisationService(
			ServiceOrganisationService serviceOrganisationService) {
		this.serviceOrganisationService = serviceOrganisationService;
	}

	/**
	 * @param processManager process manager setter
	 */
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
