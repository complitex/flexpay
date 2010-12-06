package org.flexpay.admin.actions.user.parameters;

import org.flexpay.admin.persistence.UserPaymentPropertiesModel;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.CashboxFilter;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class UserPaymentParametersEditAction extends FPActionSupport {

    private UserPaymentPropertiesModel preference = UserPaymentPropertiesModel.getInstance();
    private PaymentCollectorFilter paymentCollectorFilter = new PaymentCollectorFilter();
    private PaymentPointFilter paymentPointFilter = new PaymentPointFilter();
    private CashboxFilter cashboxFilter = new CashboxFilter();

    private CashboxService cashboxService;
    private PaymentPointService paymentPointService;
    private PaymentCollectorService paymentCollectorService;
	private UserPreferencesService userPreferencesService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		PaymentsUserPreferences paymentsUserPreferences = null;

		try {
			UserPreferences userPreference = userPreferencesService.loadUserByUsername(preference.getUsername());
			if (userPreference instanceof PaymentsUserPreferences) {
				paymentsUserPreferences = (PaymentsUserPreferences) userPreference;
			} else {
				addActionError(getText("admin.error.payment.not_edited"));
                log.debug("This user preferences not instance of PaymentsUserPreferences. Can't edit");
				return INPUT;
			}
		} catch (Throwable t) {
			log.warn("Search user {}", preference.getUsername(), t);
		}
		if (paymentsUserPreferences == null) {
			addActionError(getText("admin.error.certificate.user_not_found"));
            log.debug("User not found");
			return INPUT;
		}

		if (isSubmit()) {

            if (cashboxFilter != null && cashboxFilter.getSelectedId() != null && cashboxFilter.getSelectedId() > 0) {
                Cashbox cashbox = cashboxService.read(cashboxFilter.getSelectedStub());
                if (cashbox == null || cashbox.isNotActive()) {
                    addActionError(getText("admin.error.payment.incorrect_cashbox_id"));
                    log.warn("Incorrect cashbox value ({})", cashboxFilter.getSelectedId());
                    return INPUT;
                }
                if (paymentPointFilter == null || !cashbox.getPaymentPoint().getId().equals(paymentPointFilter.getSelectedId())) {
                    addActionError(getText("admin.error.payment.incorrect_payment_point_id"));
                    log.warn("Incorrect payment point value ({})", paymentPointFilter);
                }
                if (paymentCollectorFilter == null || !cashbox.getPaymentPoint().getCollector().getId().equals(paymentCollectorFilter.getSelectedId())) {
                    addActionError(getText("admin.error.payment.incorrect_payment_collector_id"));
                    log.warn("Incorrect payment collector value ({})", paymentCollectorFilter);
                }
            } else if (paymentPointFilter != null && paymentPointFilter.getSelectedId() != null && paymentPointFilter.getSelectedId() > 0) {
                PaymentPoint paymentPoint = paymentPointService.read(paymentPointFilter.getSelectedStub());
                if (paymentPoint == null || paymentPoint.isNotActive()) {
                    addActionError(getText("admin.error.payment.incorrect_payment_point_id"));
                    log.warn("Incorrect payment point value ({})", paymentPointFilter);
                    return INPUT;
                }
                if (paymentCollectorFilter == null || !paymentPoint.getCollector().getId().equals(paymentCollectorFilter.getSelectedId())) {
                    addActionError(getText("admin.error.payment.incorrect_payment_collector_id"));
                    log.warn("Incorrect payment collector value ({})", paymentCollectorFilter);
                }
            } else if (paymentCollectorFilter == null && paymentCollectorFilter.getSelectedId() != null && paymentCollectorFilter.getSelectedId() > 0) {
                PaymentCollector paymentCollector = paymentCollectorService.read(paymentCollectorFilter.getSelectedStub());
                if (paymentCollector == null || paymentCollector.isNotActive()) {
                    addActionError(getText("admin.error.payment.incorrect_payment_collector_id"));
                    log.warn("Incorrect payment collector value ({})", paymentCollectorFilter);
                    return INPUT;
                }
            }

            if (hasErrors()) {
                return INPUT;
            }

			paymentsUserPreferences.setPaymentCollectorId(paymentCollectorFilter.getSelectedId());
			paymentsUserPreferences.setPaymentPointId(paymentPointFilter.getSelectedId());
			paymentsUserPreferences.setCashboxId(cashboxFilter.getSelectedId());

			userPreferencesService.saveGeneralData(paymentsUserPreferences);
			return REDIRECT_SUCCESS;
		}

        paymentCollectorService.initFilter(paymentCollectorFilter);
        paymentCollectorFilter.setSelectedId(paymentsUserPreferences.getPaymentCollectorId());
        paymentCollectorFilter.setNeedAutoChange(false);

        paymentPointFilter.setSelectedId(paymentsUserPreferences.getPaymentPointId());
        paymentPointFilter.setNeedAutoChange(false);

        cashboxFilter.setSelectedId(paymentsUserPreferences.getCashboxId());
        cashboxFilter.setNeedAutoChange(false);

		return INPUT;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

    public UserPaymentPropertiesModel getPreference() {
        return preference;
    }

    public PaymentCollectorFilter getPaymentCollectorFilter() {
        return paymentCollectorFilter;
    }

    public void setPaymentCollectorFilter(PaymentCollectorFilter paymentCollectorFilter) {
        this.paymentCollectorFilter = paymentCollectorFilter;
    }

    public PaymentPointFilter getPaymentPointFilter() {
        return paymentPointFilter;
    }

    public void setPaymentPointFilter(PaymentPointFilter paymentPointFilter) {
        this.paymentPointFilter = paymentPointFilter;
    }

    public CashboxFilter getCashboxFilter() {
        return cashboxFilter;
    }

    public void setCashboxFilter(CashboxFilter cashboxFilter) {
        this.cashboxFilter = cashboxFilter;
    }

    @Required
    public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
        this.paymentCollectorService = paymentCollectorService;
    }

    @Required
    public void setCashboxService(CashboxService cashboxService) {
        this.cashboxService = cashboxService;
    }

    @Required
    public void setPaymentPointService(PaymentPointService paymentPointService) {
        this.paymentPointService = paymentPointService;
    }

    @Required
	public void setUserPreferencesService(UserPreferencesService userPreferencesService) {
		this.userPreferencesService = userPreferencesService;
	}
}
