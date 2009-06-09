package org.flexpay.orgs.service.history;

import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.PaymentsCollectorDescription;

public class PaymentsCollectorHistoryBuilder
	extends OrganizationInstanceHistoryBuilder<PaymentsCollectorDescription, PaymentsCollector> {

	protected PaymentsCollector newInstance() {
		return new PaymentsCollector();
	}

	protected PaymentsCollectorDescription newDescriptionInstance() {
		return new PaymentsCollectorDescription();
	}
}
