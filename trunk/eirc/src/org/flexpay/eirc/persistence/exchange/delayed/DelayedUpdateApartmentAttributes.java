package org.flexpay.eirc.persistence.exchange.delayed;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.exchange.DelayedUpdate;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.bti.service.BtiApartmentService;

public class DelayedUpdateApartmentAttributes implements DelayedUpdate {

	private BtiApartment apartment;
	private BtiApartmentService service;

	public DelayedUpdateApartmentAttributes(BtiApartment apartment, BtiApartmentService service) {
		this.apartment = apartment;
		this.service = service;
	}

	/**
	 * Perform storage update
	 */
	@Override
	public void doUpdate() throws FlexPayExceptionContainer {
		service.mergeAttributes(apartment);
	}

	@Override
	public int hashCode() {
		return apartment.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof DelayedUpdateApartmentAttributes
			   && apartment.equals(((DelayedUpdateApartmentAttributes) obj).apartment);
	}
}
