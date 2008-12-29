package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.ab.persistence.Apartment;

/**
 * Place where payments are taken
 */
public class PaymentPoint extends DomainObjectWithStatus {

	private PaymentsCollector collector;
	private String address;

	public PaymentsCollector getCollector() {
		return collector;
	}

	public void setCollector(PaymentsCollector collector) {
		this.collector = collector;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
