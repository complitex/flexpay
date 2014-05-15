package org.flexpay.payments.service;

import org.flexpay.payments.action.outerrequest.request.SearchRequest;
import org.flexpay.payments.action.outerrequest.request.response.SearchResponse;
import org.jetbrains.annotations.NotNull;

public interface QuittanceDetailsFinder {

	/**
	 * Find quittance details
	 *
	 * @param request Request for quittance details
	 * @return Details response
	 */
	@NotNull
    <E extends SearchResponse, T extends SearchRequest<E>> E findQuittance(T request);
}
