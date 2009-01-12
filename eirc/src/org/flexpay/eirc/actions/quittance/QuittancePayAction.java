package org.flexpay.eirc.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.persistence.QuittancePayment;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.QuittancePaymentService;
import org.jetbrains.annotations.NotNull;

public class QuittancePayAction extends FPActionSupport {

	private QuittancePaymentService quittancePaymentService;

	private QuittancePacket packet = new QuittancePacket();
	private Quittance quittance = new Quittance();
	private QuittancePayment payment = new QuittancePayment();

	@NotNull
	public String doExecute() {

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

}
