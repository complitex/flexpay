package org.flexpay.orgs.actions.paymentpoint;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.orgs.persistence.filters.PaymentsCollectorFilter;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PaymentPointsListPageAction extends FPActionSupport {

	protected PaymentsCollectorFilter paymentsCollectorFilter = new PaymentsCollectorFilter();

	protected PaymentsCollectorService collectorService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected String doExecute() throws Exception {

		collectorService.initFilter(paymentsCollectorFilter);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	public PaymentsCollectorFilter getPaymentsCollectorFilter() {
		return paymentsCollectorFilter;
	}

	@Required
	public void setCollectorService(PaymentsCollectorService collectorService) {
		this.collectorService = collectorService;
	}

}
