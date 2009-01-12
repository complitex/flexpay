package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.Set;

/**
 * Place where payments are taken
 */
public class PaymentPoint extends DomainObjectWithStatus {

	private PaymentsCollector collector;
	private String address;
	private Set<QuittancePacket> quittancePackets = Collections.emptySet();

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

	public Set<QuittancePacket> getQuittancePackets() {
		return quittancePackets;
	}

	public void setQuittancePackets(Set<QuittancePacket> quittancePackets) {
		this.quittancePackets = quittancePackets;
	}
}
