package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.PaymentPoint;
import org.flexpay.eirc.persistence.filters.PaymentsCollectorFilter;
import org.flexpay.eirc.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;

public class PaymentPointEditAction extends FPActionSupport {

	private PaymentPointService paymentPointService;


	private PaymentsCollectorFilter paymentsCollectorFilter = new PaymentsCollectorFilter();
	private PaymentPoint point = new PaymentPoint();

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

		if (point.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}
		PaymentPoint pnt = point.isNew() ? point : paymentPointService.read(stub(point));
		if (pnt == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		pnt.setAddress(point.getAddress());

		if (isSubmit()) {
			return REDIRECT_SUCCESS;
		}

		return INPUT;
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
		return INPUT;
	}
}
