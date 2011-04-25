package org.flexpay.orgs.action.paymentpoint;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class PaymentPointViewAction extends FPActionSupport {

	private PaymentPoint point = new PaymentPoint();

	private PaymentPointService paymentPointService;

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

		if (point.isNew()) {
			log.error(getText("common.error.invalid_id"));
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		point = paymentPointService.read(stub(point));
		if (point == null) {
			log.error(getText("common.error.object_not_selected"));
			addActionError(getText("common.error.object_not_selected"));
			return REDIRECT_ERROR;
		}

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
		return REDIRECT_ERROR;
	}

	public PaymentPoint getPoint() {
		return point;
	}

	public void setPoint(PaymentPoint point) {
		this.point = point;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

}
