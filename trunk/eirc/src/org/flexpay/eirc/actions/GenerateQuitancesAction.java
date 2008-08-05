package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.apache.commons.lang.time.DateUtils;

import java.util.*;
import java.io.Serializable;

public class GenerateQuitancesAction extends FPActionSupport {

	private ServiceOrganisationService serviceOrganisationService;

	private List<ServiceOrganisation> serviceOrganizationList;

    private ProcessManager processManager;

    private Integer year;
	private Integer month;

	public String doExecute() throws Exception {
		if (isSubmit()) {

            Calendar calendar = new GregorianCalendar(year, month, 1);
            Date dateFrom = calendar.getTime();
			Date dateTill = DateUtils.addMonths(dateFrom, 1);

            Map<Serializable,Serializable> contextVariables = CollectionUtils.map();

            contextVariables.put("dateFrom", dateFrom);
            contextVariables.put("dateTill", dateTill);

            processManager.createProcess("GenerateQuitances",contextVariables);
        }

		initDefaultDate();
		serviceOrganizationList = serviceOrganisationService.listServiceOrganisations();

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	protected String getErrorResult() {
		return SUCCESS;
	}

	private void initDefaultDate() {
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the month
	 */
	public Integer getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(Integer month) {
		this.month = month;
	}

	/**
	 * @return the serviceOrganizationList
	 */
	public List<ServiceOrganisation> getServiceOrganizationList() {
		return serviceOrganizationList;
	}

	/**
	 * @param serviceOrganisationService the serviceOrganisationService to set
	 */
	public void setServiceOrganisationService(
			ServiceOrganisationService serviceOrganisationService) {
		this.serviceOrganisationService = serviceOrganisationService;
	}

    /**
     *
     * @param processManager the process manager to set
     */
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }
}
