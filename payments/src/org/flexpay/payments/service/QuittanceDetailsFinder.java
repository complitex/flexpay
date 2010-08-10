package org.flexpay.payments.service;

import org.flexpay.payments.actions.outerrequest.request.SearchRequest;
import org.flexpay.payments.actions.outerrequest.request.response.SearchResponse;
import org.jetbrains.annotations.NotNull;

public interface QuittanceDetailsFinder {

	/**
	 * Find quittance details
	 *
	 * @param request Request for quittance details
	 * @return Details response
	 */
	@NotNull
    SearchResponse findQuittance(SearchRequest<?> request);
}
