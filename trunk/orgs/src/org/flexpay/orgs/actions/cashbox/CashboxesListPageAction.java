package org.flexpay.orgs.actions.cashbox;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class CashboxesListPageAction extends FPActionSupport {

	protected PaymentPointFilter paymentPointFilter = new PaymentPointFilter();

	protected PaymentPointService paymentPointService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	@Override
	protected String doExecute() throws Exception {

		paymentPointService.initFilter(paymentPointFilter);

		paymentPointFilter.setReadOnly(true);

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
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public PaymentPointFilter getPaymentPointFilter() {
		return paymentPointFilter;
	}

	public void setPaymentPointFilter(PaymentPointFilter paymentPointFilter) {
		this.paymentPointFilter = paymentPointFilter;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

}
