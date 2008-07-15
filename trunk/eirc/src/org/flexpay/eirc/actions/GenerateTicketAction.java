package org.flexpay.eirc.actions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.flexpay.common.actions.FPActionSupport;

public class GenerateTicketAction extends FPActionSupport {

	private ServiceOrganisationService serviceOrganisationService;
	//private TicketService tickerService;
	private QuittanceService quittanceService;

	private List<ServiceOrganisation> serviceOrganizationList;
	
	private Integer year;
	private Integer month;
	private Long serviceOrganisationId;

	public String execute() {
		if(isSubmit()) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date dateFrom = cal.getTime();
			cal.add(Calendar.MONTH, 1);
			Date dateTill = cal.getTime();
			
			quittanceService.generateForServiceOrganisation(serviceOrganisationId, dateFrom, dateTill);
		}
		
		initDefaultDate();
		serviceOrganizationList = serviceOrganisationService.listServiceOrganisation();

		return "success";
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
