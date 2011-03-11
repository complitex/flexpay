package org.flexpay.eirc.actions.quittance;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.filters.ServiceOrganizationFilter;
import org.flexpay.orgs.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class QuittanceGeneratePDFAction extends FPActionSupport {

	private ServiceOrganizationFilter serviceOrganizationFilter = new ServiceOrganizationFilter();

	private BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtils.truncate(new Date(), Calendar.MONTH));
	private EndDateFilter endDateFilter = new EndDateFilter(DateUtil.now());

	private ProcessManager processManager;
	private ServiceOrganizationService serviceOrganizationService;

	@NotNull
    @Override
	public String doExecute() throws Exception {

		if (isSubmit()) {

			if (!serviceOrganizationFilter.needFilter()) {
				addActionError(getText("eirc.error.quittance.no_service_organization"));
			} else {
				Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

				contextVariables.put("serviceOrganizationId", serviceOrganizationFilter.getSelectedId());
				contextVariables.put("dateFrom", beginDateFilter.getDate());
				contextVariables.put("dateTill", endDateFilter.getDate());

				processManager.createProcess("GenerateQuittancePDF", contextVariables);

				addActionMessage(getText("eirc.quittance.printing_started"));
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

	@Required
	public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
		this.serviceOrganizationService = serviceOrganizationService;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
