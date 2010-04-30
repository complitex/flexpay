package org.flexpay.payments.service;

import org.flexpay.payments.persistence.quittance.InfoRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.jetbrains.annotations.NotNull;

public interface QuittanceDetailsFinder {

	/**
	 * Find quittance details
	 *
	 * @param request Request for quittance details
	 * @return Details response
	 */
	@NotNull
	QuittanceDetailsResponse findQuittance(InfoRequest request);
}
