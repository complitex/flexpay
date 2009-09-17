package org.flexpay.orgs.service.history;

import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentCollectorDescription;

public class PaymentCollectorHistoryBuilder
	extends OrganizationInstanceHistoryBuilder<PaymentCollectorDescription, PaymentCollector> {

	protected PaymentCollector newInstance() {
		return new PaymentCollector();
	}

	protected PaymentCollectorDescription newDescriptionInstance() {
		return new PaymentCollectorDescription();
	}
}
