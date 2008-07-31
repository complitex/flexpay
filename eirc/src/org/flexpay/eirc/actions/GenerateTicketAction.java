package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.GregorianCalendar;

public class GenerateTicketAction extends FPActionSupport {

	private ServiceOrganisationService serviceOrganisationService;
	//private TicketService tickerService;
	private QuittanceService quittanceService;

	private List<ServiceOrganisation> serviceOrganizationList;

	private Integer year;
	private Integer month;
	private Long serviceOrganisationId;

	public String doExecute() {
		if (isSubmit()) {

            Calendar calendar = new GregorianCalendar(year, month, 1);
            Date dateFrom = calendar.getTime();
			Date dateTill = DateUtils.addMonths(dateFrom, 1);

			quittanceService.generateForServiceOrganisation(serviceOrganisationId, dateFrom, dateTill);
		}

		initDefaultDate();
		serviceOrganizationList = serviceOrganisationService.listServiceOrganisation();

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
	 * @param serviceOrganisationId the serviceOrganisationId to set
	 */
	public void setServiceOrganisationId(Long serviceOrganisationId) {
		this.serviceOrganisationId = serviceOrganisationId;
	}


	/**
	 * @param quittanceService the quittanceService to set
	 */
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

}
