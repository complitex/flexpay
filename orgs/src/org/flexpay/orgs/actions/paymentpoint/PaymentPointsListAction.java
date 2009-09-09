package org.flexpay.orgs.actions.paymentpoint;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.PaymentsCollectorFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class PaymentPointsListAction extends FPActionWithPagerSupport<PaymentPoint> {

	protected Long paymentsCollectorFilter;
	protected List<PaymentPoint> points = CollectionUtils.list();

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

		ArrayStack filters = CollectionUtils.arrayStack(new PaymentsCollectorFilter(paymentsCollectorFilter));
		points = paymentPointService.listPoints(filters, getPager());

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

	public void setPaymentsCollectorFilter(Long paymentsCollectorFilter) {
		this.paymentsCollectorFilter = paymentsCollectorFilter;
	}

	public List<PaymentPoint> getPoints() {
		return points;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

}
