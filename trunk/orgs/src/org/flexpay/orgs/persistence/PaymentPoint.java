package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.util.TranslationUtil;

import java.util.Collections;
import java.util.Set;

/**
 * Place where payments are taken
 */
public class PaymentPoint extends DomainObjectWithStatus {

	private PaymentsCollector collector;
	private Set<PaymentPointName> names = Collections.emptySet();
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

	public Set<PaymentPointName> getNames() {
		return names;
	}

	public void setNames(Set<PaymentPointName> names) {
		this.names = names;
	}

	public void setName(PaymentPointName name) {
		names = TranslationUtil.setTranslation(names, this, name);
	}
}
