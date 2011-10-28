package org.flexpay.payments.action.paymentpoint;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.util.SecurityUtil.isAuthenticationGranted;
import static org.flexpay.payments.service.Roles.PAYMENTS_DEVELOPER;

public class PaymentPointsListPageAction extends AccountantAWPActionSupport {

	protected PaymentCollectorFilter paymentCollectorFilter = new PaymentCollectorFilter();

	protected PaymentCollectorService collectorService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	@Override
	protected String doExecute() throws Exception {

		UserPreferences userPreferences = getUserPreferences();
		if (isAuthenticationGranted(PAYMENTS_DEVELOPER)) {
			collectorService.initFilter(paymentCollectorFilter);
		} else if (userPreferences != null && userPreferences instanceof PaymentsUserPreferences &&
					((PaymentsUserPreferences)userPreferences).getPaymentCollectorId() != null) {
			PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>((((PaymentsUserPreferences) userPreferences)).getPaymentCollectorId()));
			if (paymentCollector != null) {
				paymentCollectorFilter.setInstances(CollectionUtils.list(paymentCollector));
				paymentCollectorFilter.setSelectedId(paymentCollector.getId());
				paymentCollectorFilter.setReadOnly(true);
			} else {
				log.warn("Can not find payment collector '{}' (see user preferences {})",
						(((PaymentsUserPreferences) userPreferences)).getPaymentCollectorId(), userPreferences);
			}
		}

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public PaymentCollectorFilter getPaymentCollectorFilter() {
		return paymentCollectorFilter;
	}

	@Required
	public void setCollectorService(PaymentCollectorService collectorService) {
		this.collectorService = collectorService;
	}

}