package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ObjectAlreadyExistException;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.exception.FlexPayException;

public class ApartmentEditAction extends CommonAction {
	public static final int STATUS_BLANC_NUMBER = 0;
	public static final int STATUS_NUMBER_ALREDY_EXIST = 1;

	private ApartmentService apartmentService;

	private Apartment apartment;
	private String apartmentNumber;
	private int status = -1;

	public String execute() throws FlexPayException {
		if (isSubmitted()) {
			if (apartmentNumber == null || apartmentNumber.equals("")) {
				status = STATUS_BLANC_NUMBER;
			} else {
				try {
					apartmentService.setApartmentNumber(apartment,
							apartmentNumber);
					return "list";
				} catch (ObjectAlreadyExistException e) {
					status = STATUS_NUMBER_ALREDY_EXIST;
				}
			}
		}

		apartment = apartmentService.readFull(apartment.getId());
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
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param apartmentNumber
	 *            the apartmentNumber to set
	 */
	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

}
