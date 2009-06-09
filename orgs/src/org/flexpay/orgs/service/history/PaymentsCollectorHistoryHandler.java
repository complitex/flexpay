package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.PaymentsCollectorDescription;
import org.jetbrains.annotations.NotNull;

public class PaymentsCollectorHistoryHandler
		extends OrganizationInstanceHistoryHandler<PaymentsCollectorDescription, PaymentsCollector> {

	protected PaymentsCollector newInstance() {
		return new PaymentsCollector();
	}

	protected Class<PaymentsCollector> getType() {
		return PaymentsCollector.class;
	}

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(PaymentsCollector.class) == diff.getObjectType();
	}
}
