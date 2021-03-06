package org.flexpay.payments.action.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.action.security.SetCashboxIdAction;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CashboxInterceptor extends AbstractInterceptor {

	private Logger log = LoggerFactory.getLogger(getClass());

	private static final String CASHBOX_AUTHENTICATION_REQUIRED = "cashboxAuthenticationRequired";

	private CashboxService cashboxService;

    public static final int MOUNTH_IN_SECONDS = 2628000;

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {

		try {
			Object action = actionInvocation.getAction();
			if (action instanceof CashboxAware) {
				Long cashboxId = ((CashboxAware) action).getCashboxId();
				if (cashboxId == null || cashboxId <= 0) {
					log.info("No cashbox identifier information found. Access denied.");
					return CASHBOX_AUTHENTICATION_REQUIRED;
				}

				Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));
				if (cashbox == null) {
					log.info("Bad cashbox identifier: no cashbox exists with id {}. Access denied.", cashboxId);
					return CASHBOX_AUTHENTICATION_REQUIRED;
				}

				log.info("Cashbox identified as {}", cashboxId);

				Long cashboxPaymentPointId = cashbox.getPaymentPoint().getId();

				if (action instanceof FPActionSupport) {
					FPActionSupport fpAction = (FPActionSupport) action;
					PaymentsUserPreferences userPreferences = (PaymentsUserPreferences) fpAction.getUserPreferences();
					Long userPaymentPointId = userPreferences.getPaymentPointId();

					if (!cashboxPaymentPointId.equals(userPaymentPointId)) {
						log.info("Payment point identifier from user preferences ({}) does not correspond cashbox payment point identifier ({}). Access denied.",
								new Object[] {userPaymentPointId, cashboxPaymentPointId});
						return CASHBOX_AUTHENTICATION_REQUIRED;
					}
                    HttpServletRequest request = ServletActionContext.getRequest();
                    log.debug("Extend life cookies");
                    Cookie[] cookies = request.getCookies();
                    for (Cookie cookie : cookies) {
                        log.debug("Cookie: {}, maxAge: {}", cookie.getName(), cookie.getMaxAge());
                        if (StringUtils.equals(cookie.getName(), SetCashboxIdAction.CASHBOX_ID) && cookie.getMaxAge() < MOUNTH_IN_SECONDS) {
                            log.debug("Update cookie`s maxAge");
                            HttpServletResponse response = ServletActionContext.getResponse();
                            Cookie updateCookie = new Cookie(SetCashboxIdAction.CASHBOX_ID, cookie.getValue());
                            updateCookie.setMaxAge(SetCashboxIdAction.SIX_MOUNTH_IN_SECONDS);
                            response.addCookie(updateCookie);
                            break;
                        }
                    }
					log.info("Cashbox authentication ok.");
				} else {
					log.info("Action not instance of FPActionSupport");
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

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

}
