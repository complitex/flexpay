package org.flexpay.payments.service;

import org.flexpay.payments.actions.request.data.request.InfoRequest;
import org.flexpay.payments.actions.request.data.response.QuittanceDetailsResponse;
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
