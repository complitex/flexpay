package org.flexpay.payments.actions.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.flexpay.payments.actions.PaymentPointAwareAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PaymentPointIdentificationInterceptor extends AbstractInterceptor {

	private static final Logger log = LoggerFactory.getLogger(PaymentPointIdentificationInterceptor.class);

	private static final String POINT_AUTHENTICATION_REQUIRED = "pointAuthenticationRequired";

	public String intercept(ActionInvocation actionInvocation) throws Exception {

		Object action = actionInvocation.getAction();
		if (action instanceof PaymentPointAwareAction) {
			PaymentPointAwareAction paymentPointAwareAction = (PaymentPointAwareAction) action;

			String paymentPointId = paymentPointAwareAction.getPaymentPointId();
			String organizationId = paymentPointAwareAction.getOrganizationId();
			if (paymentPointId != null || organizationId != null) {
				log.info("Payment point identified as {} and organization as {}", new Object[] {paymentPointId, organizationId});
			} else {
				return POINT_AUTHENTICATION_REQUIRED;
			}
		}

		return actionInvocation.invoke();
	}
}
