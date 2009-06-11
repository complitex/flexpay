package org.flexpay.payments.actions.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CashboxInterceptor extends AbstractInterceptor {

	private Logger log = LoggerFactory.getLogger(getClass());

	private static final String CASHBOX_AUTHENTICATION_REQUIRED = "cashboxAuthenticationRequired";

	public String intercept(ActionInvocation actionInvocation) throws Exception {

		try {

			Object action = actionInvocation.getAction();
			if (action instanceof CashboxAware) {
				Long cashboxId = ((CashboxAware) action).getCashboxId();
				if (cashboxId != null) {
					log.info("Cashbox identified as {}", cashboxId);
				} else {
					return CASHBOX_AUTHENTICATION_REQUIRED;
				}
			}

			return actionInvocation.invoke();

		} catch (Exception e) {
			log.error("Failure", e);
			throw e;
		} catch (Throwable e) {
			log.error("Failure", e);
			throw new RuntimeException(e);
		}

	}

}
