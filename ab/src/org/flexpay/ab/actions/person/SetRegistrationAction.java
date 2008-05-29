package org.flexpay.ab.actions.person;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.flexpay.ab.actions.apartment.ApartmentEditAction;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.DateIntervalUtil;

public class SetRegistrationAction extends ApartmentEditAction {
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	PersonService personService;
	BuildingService buildingService;
	
	private List<Apartment> apartments = new ArrayList<Apartment>();
	private Person person;
	private Date beginDate;
	private Date endDate;
	
	private String action;
	private String apartmentError;
	private String beginAfterEndError;
	private String beginIntervalError;
	
	public String execute() throws FlexPayException {
		
		if("save".equals(action)) {
			if(getApartment() == null) {
				apartmentError = "ab.person.apartment_absent";
			}
			
			
			if(apartmentError == null) {
				person = personService.read(person.getId());
				try {
					person.setPersonRegistration(getApartment(), beginDate, endDate);
					personService.update(person);
					return "person_view";
				} catch (FlexPayException e) {
					if("ab.person.registration.error.begin_after_end".equals(e.getErrorKey())) {
						beginAfterEndError = e.getErrorKey();
					} else if("ab.person.registration.error.begin_date_interval_error".equals(e.getErrorKey())) {
						beginIntervalError = e.getErrorKey();
					} else {
						throw e;
					}
				}
			}
		}
		
		if(beginDate == null) {
			beginDate = DateIntervalUtil.now();
		}
		if(endDate == null) {
			endDate = ApplicationConfig.getInstance().getFutureInfinite();
		}
		
		
		if(getCountryFilter().getSelectedId() == null) {
			person = personService.read(person.getId());
			getApartmentService().fillFilterIds(person.getApartment(), getCountryFilter(), getRegionFilter(), getTownFilter(), getStreetFilter(), getBuildingsFilter());
		}
		
		initFilters();
		if(getFiltersError() == null) {
			Page pager = new Page();
			pager.setPageSize(10000);
			apartments = getApartmentService().getApartments(getFilters(), pager);
		}
		
		
		return "form";
	}
	
	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}



	/**
	 * @return the beginDate
	 */
	public String getBeginDate() {
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		return format.format(beginDate);
	}



	/**
	 * @param beginDate the beginDate to set
	 * @throws ParseException 
	 */
	public void setBeginDate(String beginDate) throws ParseException {
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		this.beginDate = format.parse(beginDate);
	}



	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		return format.format(endDate);
	}



	/**
	 * @param endDate the endDate to set
	 * @throws ParseException 
	 */
	public void setEndDate(String endDate) throws ParseException {
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		this.endDate = format.parse(endDate);
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * @param buildingService the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	/**
	 * @return the apartments
	 */
	public List<Apartment> getApartments() {
		return apartments;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the apartmentError
	 */
	public String getApartmentError() {
		return apartmentError;
	}

	/**
	 * @return the beginAfterEndError
	 */
	public String getBeginAfterEndError() {
		return beginAfterEndError;
	}

	/**
	 * @return the beginIntervalError
	 */
	public String getBeginIntervalError() {
		return beginIntervalError;
	}

}
