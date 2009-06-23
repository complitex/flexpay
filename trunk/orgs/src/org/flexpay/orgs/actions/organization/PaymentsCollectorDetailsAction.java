package org.flexpay.orgs.actions.organization;

import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

public class PaymentsCollectorDetailsAction extends PaymentPointsListAction {

	private PaymentsCollector paymentsCollector;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		loadPaymentsCollector();
		loadPaymentPoints();

		return SUCCESS;
	}

	private void loadPaymentsCollector() {
		if (paymentsCollectorFilter.needFilter()) {
			paymentsCollector = collectorService.read(paymentsCollectorFilter.getSelectedStub());
		} else {
			paymentsCollector = null;
		}
	}

	// rendering utility methods
	public boolean isPaymentCollectorLoaded() {
		return paymentsCollector != null;
	}

	public String getPaymentsCollectorDescription() {

		return getTranslation(paymentsCollector.getDescriptions()).getName();
	}

	public String getOrganizationName() {

		return organizationHelper.getName(paymentsCollector.getOrganization(), userPreferences.getLocale());
	}

	public PaymentsCollector getPaymentsCollector() {
		return paymentsCollector;
	}
}
