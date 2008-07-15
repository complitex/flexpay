package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ObjectAlreadyExistException;
import org.flexpay.ab.service.ApartmentService;import static org.flexpay.common.persistence.Stub.stub;

public class ApartmentEditAction extends BuildingsFilterDependentAction {
	private ApartmentService apartmentService;

	private Apartment apartment;
	private String apartmentNumber;
	private String numberError;

	public String doExecute() throws Exception {
		if (isSubmit()) {
			if (apartmentNumber == null || apartmentNumber.equals("")) {
				//status = STATUS_BLANC_NUMBER;
			} else {
				try {
					apartmentService.setApartmentNumber(stub(apartment), apartmentNumber);
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
	 * @param apartmentService the apartmentService to set
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
	 * @param apartment the apartment to set
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

	/**
	 * @return the apartmentService
	 */
	public ApartmentService getApartmentService() {
		return apartmentService;
	}
}
