package org.flexpay.eirc.actions.quittance;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.persistence.filters.ServiceOrganizationFilter;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class QuittanceGeneratePDFAction extends FPActionSupport {

	private ServiceOrganizationService serviceOrganizationService;

	private ServiceOrganizationFilter serviceOrganizationFilter =
			new ServiceOrganizationFilter();

	private BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtils.truncate(new Date(), Calendar.MONTH));
	private EndDateFilter endDateFilter = new EndDateFilter(DateUtil.now());

	private ProcessManager processManager;

	@NotNull
	public String doExecute() throws Exception {

		if (isSubmit()) {

			if (!serviceOrganizationFilter.needFilter()) {
				addActionError(getText("eirc.error.quittance.no_service_organization"));
			} else {
				Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

				contextVariables.put("serviceOrganizationId", serviceOrganizationFilter.getSelectedId());
				contextVariables.put("dateFrom", beginDateFilter.getDate());
				contextVariables.put("dateTill", endDateFilter.getDate());

				processManager.createProcess("GenerateQuitancePDF", contextVariables);

				addActionError(getText("eirc.quittance.printing_started"));
			}
		}

		serviceOrganizationService.initServiceOrganizationsFilter(serviceOrganizationFilter);

		return SUCCESS;
	}

	public ServiceOrganizationFilter getServiceOrganizationFilter() {
		return serviceOrganizationFilter;
	}

	public void setServiceOrganizationFilter(ServiceOrganizationFilter serviceOrganizationFilter) {
		this.serviceOrganizationFilter = serviceOrganizationFilter;
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
	 * @param serviceOrganizationService the serviceOrganizationService to set
	 */
	public void setServiceOrganizationService(
			ServiceOrganizationService serviceOrganizationService) {
		this.serviceOrganizationService = serviceOrganizationService;
	}

	/**
	 * @param processManager process manager setter
	 */
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
