package org.flexpay.eirc.actions.quittance;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.filters.ServiceOrganizationFilter;
import org.flexpay.eirc.process.quittance.GenerateQuittanceJob;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.service.ServiceOrganizationService;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class QuittanceGenerateAction extends FPActionSupport {

	private ServiceOrganizationFilter serviceOrganizationFilter = new ServiceOrganizationFilter();

	private BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtils.truncate(new Date(), Calendar.MONTH));
	private EndDateFilter endDateFilter = new EndDateFilter(DateUtil.now());
	private TownFilter townFilter = new TownFilter(ApplicationConfig.getDefaultTownStub());

	private ServiceOrganizationService serviceOrganizationService;
	private ProcessManager processManager;

	@NotNull
	public String doExecute() throws Exception {

		if (isSubmit()) {

			boolean validated = true;

			if (!serviceOrganizationFilter.needFilter()) {
				addActionError(getText("eirc.error.quittance.no_service_organization"));
				validated = false;
			}
			if (!townFilter.needFilter()) {
				addActionError(getText("eirc.error.quittance.no_town"));
				validated = false;
			}
			if (!DateUtil.truncateMonth(beginDateFilter.getDate()).equals(DateUtil.truncateMonth(endDateFilter.getDate()))) {
				addActionError(getText("eirc.error.quittance.job.month_only_allowed"));
				validated = false;
			}

			if (validated) {

				Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

				contextVariables.put(GenerateQuittanceJob.PARAM_DATE_FROM, beginDateFilter.getDate());
				contextVariables.put(GenerateQuittanceJob.PARAM_DATE_TILL, endDateFilter.getDate());
				contextVariables.put(GenerateQuittanceJob.PARAM_SERVICE_ORGANIZATION_ID, serviceOrganizationFilter.getSelectedId());
				contextVariables.put(GenerateQuittanceJob.PARAM_TOWN_ID, townFilter.getSelectedId());

				processManager.createProcess("GenerateQuittances", contextVariables);

				addActionError(getText("eirc.quittance.generation_started"));
			}
		}

		serviceOrganizationService.initServiceOrganizationsFilter(serviceOrganizationFilter);

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

	public TownFilter getTownFilter() {
		return townFilter;
	}

	public void setTownFilter(TownFilter townFilter) {
		this.townFilter = townFilter;
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
