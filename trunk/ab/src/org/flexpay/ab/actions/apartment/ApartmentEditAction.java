package org.flexpay.ab.actions.apartment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ObjectAlreadyExistException;
import org.flexpay.ab.persistence.PersonRegistration;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.exception.FlexPayException;

public class ApartmentEditAction extends FiltersBaseAction {
	private ApartmentService apartmentService;

	private Apartment apartment;
	private String apartmentNumber;
	private String numberError;

	public String execute() throws FlexPayException {
		if (isSubmitted()) {
			if (apartmentNumber == null || apartmentNumber.equals("")) {
				//status = STATUS_BLANC_NUMBER;
			} else {
				try {
					apartmentService.setApartmentNumber(apartment,
							apartmentNumber);
					return "list";
				} catch (ObjectAlreadyExistException e) {
					//status = STATUS_NUMBER_ALREDY_EXIST;
				}
			}
		}
		
		getCountryFilter().setReadOnly(true);
		getRegionFilter().setReadOnly(true);
		getTownFilter().setReadOnly(true);
		getStreetFilter().setReadOnly(true);
		getBuildingsFilter().setReadOnly(true);
		initFilters();

		apartment = apartmentService.readWithPersons(apartment.getId());
		
		
		
		
		
		
		
		return "form";
	}
	
	public List<PersonRegistration> sortPersonRegistrations(Set<PersonRegistration> registrations) {
		List<PersonRegistration> result = new ArrayList<PersonRegistration>(registrations);
		
		Collections.sort(result, new Comparator () {
	        public int compare(Object o1, Object o2) {
	        	PersonRegistration pr1 = (PersonRegistration)o1;
	        	PersonRegistration pr2 = (PersonRegistration)o2;
	            return pr1.getBeginDate().compareTo(pr2.getBeginDate());
	        }
	    });
		
		return result;
	}

	/**
	 * @param apartmentService
	 *            the apartmentService to set
	 */
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	/**
	 * @return the apartment
	 */
	public Apartment getApartment() {
		return apartment;
	}

	/**
	 * @param apartment
	 *            the apartment to set
	 */
	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	/**
	 * @return the numberError
	 */
	public String getNumberError() {
		return numberError;
	}

	/**
	 * @param apartmentNumber the apartmentNumber to set
	 */
	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

}
