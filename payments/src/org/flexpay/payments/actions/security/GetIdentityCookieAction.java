package org.flexpay.payments.actions.security;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

public class GetIdentityCookieAction extends FPActionSupport implements ServletResponseAware {

	private static final String PAYMENT_POINT_ID = "paymentPointId";
	private static final int ONE_YEAR_IN_SECONDS = 31536000;

	private String paymentPointId;

	private HttpServletResponse response;

	private PaymentPointService paymentPointService;

	@NotNull
	protected String doExecute() throws Exception {

		if (isSubmit()) {
			if (validatePaymentPointId()) {
				Cookie cookie = new Cookie(PAYMENT_POINT_ID, paymentPointId);
				cookie.setMaxAge(ONE_YEAR_IN_SECONDS);
				response.addCookie(cookie);
				addActionMessage(getText("payments.get_identity_cookie.cookies_successfully_set"));
			}
		}

		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	public boolean validatePaymentPointId() {

		try {
			Long id = Long.parseLong(paymentPointId);
			PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(id));
			if (paymentPoint == null) {
				addActionError(getText("payments.errors.payment_point_id_is_bad", new String[] { paymentPointId }));
			}
		} catch (NumberFormatException nfe) {
			addActionError(getText("payments.errors.payment_point_id_must_be_a_number"));
		}

		return !hasActionErrors();
	}

	public void setServletResponse(HttpServletResponse httpServletResponse) {
		this.response = httpServletResponse;
	}

	public void setPaymentPointId(String paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public String getPaymentPointId() {
		return paymentPointId;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}
}
