package org.flexpay.payments.action.paymentpoint;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class PaymentPointDeleteAction extends AccountantAWPActionSupport {

	private Set<Long> objectIds = CollectionUtils.set();

	private PaymentPointService paymentPointService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		paymentPointService.disable(objectIds);

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

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

}