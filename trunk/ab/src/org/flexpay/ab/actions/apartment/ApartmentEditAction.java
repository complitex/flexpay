package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ObjectAlreadyExistException;
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
