package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentCollectorDescription;
import org.jetbrains.annotations.NotNull;

public class PaymentCollectorHistoryHandler
		extends OrganizationInstanceHistoryHandler<PaymentCollectorDescription, PaymentCollector> {

	protected PaymentCollector newInstance() {
		return new PaymentCollector();
	}

	protected Class<PaymentCollector> getType() {
		return PaymentCollector.class;
	}

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(PaymentCollector.class) == diff.getObjectType();
	}
}
