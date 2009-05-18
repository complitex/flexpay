package org.flexpay.payments.actions.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.payments.actions.PaymentPointAwareAction;
import org.jetbrains.annotations.NotNull;

public class GenerateRegistryAction extends FPActionSupport implements PaymentPointAwareAction {

	@NotNull
	protected String doExecute() throws Exception {

		// TODO implement
		
		if (isSubmit()) {
			addActionMessage(getText("payments.registry.generate.generation_started"));
		} else {
			clearErrorsAndMessages();
		}

		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		
		return SUCCESS;
	}

	private String paymentPointId;

	public void setPaymentPointId(String paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public String getPaymentPointId() {
		return paymentPointId;
	}
}
