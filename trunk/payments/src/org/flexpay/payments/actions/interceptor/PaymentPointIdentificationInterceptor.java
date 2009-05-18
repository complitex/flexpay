package org.flexpay.payments.actions.interceptor;

import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.ActionInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flexpay.payments.actions.PaymentPointAwareAction;


public class PaymentPointIdentificationInterceptor extends AbstractInterceptor {

	private static final Logger log = LoggerFactory.getLogger(PaymentPointIdentificationInterceptor.class);

	private static final String POINT_AUTHENTICATION_REQUIRED = "pointAuthenticationRequired";

	public String intercept(ActionInvocation actionInvocation) throws Exception {

		Object action = actionInvocation.getAction();
		if (action instanceof PaymentPointAwareAction) {
			PaymentPointAwareAction paymentPointAwareAction = (PaymentPointAwareAction) action;

			String paymentPointId = paymentPointAwareAction.getPaymentPointId();
			if (paymentPointId != null) {
				log.info("Payment point identified as {}", paymentPointId);
			} else {
				return POINT_AUTHENTICATION_REQUIRED;
			}
		}

		return actionInvocation.invoke();
	}
}
