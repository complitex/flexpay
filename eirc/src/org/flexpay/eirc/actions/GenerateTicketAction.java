package org.flexpay.eirc.actions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ApartmentNumber;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.persistence.Street;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.flexpay.eirc.service.TicketService;

public class GenerateTicketAction extends CommonAction {

	private ServiceOrganisationService serviceOrganisationService;
	private TicketService tickerService;

	private List<ServiceOrganisation> serviceOrganizationList;
	
	private Integer year;
	private Integer month;
	private Long serviceOrganisationId;

	public String execute() {
		if(isSubmitted()) {
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
			
			tickerService.generateForServiceOrganisation(serviceOrganisationId, dateFrom, dateTill);
			
		}
		
		initDefaultDate();
		serviceOrganizationList = serviceOrganisationService.listServiceOrganisation();

		return "success";
	}
	
	private void processBuilding(ServedBuilding building) {
		Set<Buildings> buildingsSet = building.getBuildingses();
		if(buildingsSet.isEmpty()) {
			return;
		}
		String buildingsNumber = null;
		Street street = null;
		for(Buildings buildings : buildingsSet) {
			buildingsNumber = buildings.getNumber();
			street = buildings.getStreet();
			if(buildingsNumber != null && street != null) {
				break;
			}
		}
		
		Set<Apartment> apartmentSet = building.getApartments();
		for(Apartment apartament : apartmentSet) {
			Set<ApartmentNumber> apartmentNumberSet = apartament.getApartmentNumbers();
			Set<Person>personSet = apartament.getPersons();
			for(Person person : personSet) {
				PersonIdentity personIdentity = getTargetPersonIdentity(person.getPersonIdentities());
				String firstName = personIdentity.getFirstName();
				String lastName = personIdentity.getLastName();
				String middleName = personIdentity.getMiddleName();
			}
		}
		
		
	}
	
	private PersonIdentity getTargetPersonIdentity(Set<PersonIdentity> personIdentitySet) {
		// TODO what person identity we need &
		return personIdentitySet.isEmpty() ? null : personIdentitySet.iterator().next();
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
	 * @param tickerService the tickerService to set
	 */
	public void setTickerService(TicketService tickerService) {
		this.tickerService = tickerService;
	}

}
