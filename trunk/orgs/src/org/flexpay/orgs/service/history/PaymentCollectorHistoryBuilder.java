package org.flexpay.orgs.service.history;

import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentCollectorDescription;

public class PaymentCollectorHistoryBuilder
	extends OrganizationInstanceHistoryBuilder<PaymentCollectorDescription, PaymentCollector> {

    @Override
	protected PaymentCollector newInstance() {
		return new PaymentCollector();
	}

    @Override
	protected PaymentCollectorDescription newDescriptionInstance() {
		return new PaymentCollectorDescription();
	}
}
