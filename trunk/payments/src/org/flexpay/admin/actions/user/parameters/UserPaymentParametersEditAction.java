package org.flexpay.admin.actions.user.parameters;

import org.flexpay.admin.persistence.UserPaymentPropertiesModel;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class UserPaymentParametersEditAction extends FPActionSupport {

	private UserPaymentPropertiesModel preference = UserPaymentPropertiesModel.getInstance();
	private UserPreferencesService userPreferencesService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		PaymentsUserPreferences paymentsUserPreferences = null;
		try {
			UserPreferences userPreference = userPreferencesService.loadUserByUsername(preference.getUsername());
			if (userPreference instanceof PaymentsUserPreferences) {
				paymentsUserPreferences = (PaymentsUserPreferences)userPreference;
			} else {
				addActionError(getText("admin.error.payment.not_edited"));
				return INPUT;
			}
		} catch (Throwable t) {
			log.warn("Search user {}", preference.getUsername(), t);
		}
		if (paymentsUserPreferences == null) {
			addActionError(getText("admin.error.certificate.user_not_found"));
			return INPUT;
		}

		if (isSubmit() && getText("common.save").equals(submitted)) {
			paymentsUserPreferences.setPaymentCollectorId(preference.getPaymentCollectorId());
			paymentsUserPreferences.setPaymentPointId(preference.getPaymentPointId());
			paymentsUserPreferences.setCashboxId(preference.getCashboxId());

			userPreferencesService.saveAdvancedData(paymentsUserPreferences);
			return REDIRECT_SUCCESS;
		}

		preference.setCashboxId(paymentsUserPreferences.getCashboxId());
		preference.setPaymentPointId(paymentsUserPreferences.getPaymentPointId());
		preference.setPaymentCollectorId(paymentsUserPreferences.getPaymentCollectorId());

		return INPUT;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public UserPaymentPropertiesModel getPreference() {
		return preference;
	}

	public void setPreference(UserPaymentPropertiesModel preference) {
		this.preference = preference;
	}

	@Required
	public void setUserPreferencesService(UserPreferencesService userPreferencesService) {
		this.userPreferencesService = userPreferencesService;
	}
}
