package org.flexpay.payments.action.paymentpoint;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.action.AccountantAWPWithPagerActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class PaymentPointsListAction extends AccountantAWPWithPagerActionSupport<PaymentPoint> {

	protected Long paymentCollectorFilter;
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

		ArrayStack filters = CollectionUtils.arrayStack(new PaymentCollectorFilter(paymentCollectorFilter));
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

	public void setPaymentCollectorFilter(Long paymentCollectorFilter) {
		this.paymentCollectorFilter = paymentCollectorFilter;
	}

	public List<PaymentPoint> getPoints() {
		return points;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

}