package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.ab.persistence.Apartment;

/**
 * Place where payments are taken
 */
public class PaymentPoint extends DomainObjectWithStatus {

	private Organization organization;
	private Apartment apartment;

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}
}
