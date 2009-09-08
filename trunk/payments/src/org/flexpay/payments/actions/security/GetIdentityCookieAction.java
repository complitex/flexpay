package org.flexpay.payments.actions.security;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class GetIdentityCookieAction extends FPActionSupport implements ServletResponseAware {

	private static final String CASHBOX_ID = "cashboxId";
	private static final int SIX_YEARS_IN_SECONDS = 6 * 31536000;

	private String cashboxId;

	private HttpServletResponse response;

	private CashboxService cashboxService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (isSubmit()) {
			if (validatePaymentPointId()) {
				Cookie cookie = new Cookie(CASHBOX_ID, cashboxId);
				cookie.setMaxAge(SIX_YEARS_IN_SECONDS);
				response.addCookie(cookie);
				addActionMessage(getText("payments.get_identity_cookie.cookies_successfully_set"));
			}
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public boolean validatePaymentPointId() {

		try {
			Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(Long.parseLong(cashboxId)));
			if (cashbox == null) {
				addActionError(getText("payments.errors.cashbox_id_is_bad", cashboxId));
			}
		} catch (NumberFormatException nfe) {
			addActionError(getText("payments.errors.cashbox_id_must_be_a_number"));
		}

		return !hasActionErrors();
	}

	@Override
	public void setServletResponse(HttpServletResponse httpServletResponse) {
		this.response = httpServletResponse;
	}

	public String getCashboxId() {
		return cashboxId;
	}

	public void setCashboxId(String cashboxId) {
		this.cashboxId = cashboxId;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

}
