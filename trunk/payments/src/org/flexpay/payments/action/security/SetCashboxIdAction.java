package org.flexpay.payments.action.security;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.service.Roles;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static org.flexpay.common.util.SecurityUtil.isAuthenticationGranted;

public class SetCashboxIdAction extends FPActionSupport implements ServletResponseAware {

	private static final String CASHBOX_ID = "cashboxId";
	private static final int SIX_YEARS_IN_SECONDS = 6 * 31536000;

	private String cashboxId;

	private HttpServletResponse response;

	private CashboxService cashboxService;

    @NotNull
	@Override
	protected String doExecute() throws Exception {

		if (isAuthenticationGranted(Roles.ROLE_MENU_PAYMENTS_WORKPLACE) && validateCashboxId()) {
			Cookie cookie = new Cookie(CASHBOX_ID, cashboxId);
			cookie.setMaxAge(SIX_YEARS_IN_SECONDS);
			response.addCookie(cookie);
			addActionMessage(getText("payments.get_identity_cookie.cookies_successfully_set"));
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public boolean validateCashboxId() {

		try {
			Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(Long.parseLong(cashboxId)));
			if (cashbox == null) {
				addActionError(getText("payments.error.cashbox_id_is_bad"));
				return false;
			}

			// validating payment point correspondance
			Long userPaymentPointId = ((PaymentsUserPreferences) getUserPreferences()).getPaymentPointId();
			if (userPaymentPointId == null) {
				addActionError(getText("payments.error.user_payment_point_is_not_set"));
				return false;
			}

			Long cashboxPaymentPointId = cashbox.getPaymentPoint().getId();
			if (!cashboxPaymentPointId.equals(userPaymentPointId)) {
				addActionError(getText("payments.error.casbox_id_is_bad_payment_point"));
				return false;
			}

		} catch (NumberFormatException nfe) {
			addActionError(getText("payments.error.cashbox_id_must_be_a_number"));
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
