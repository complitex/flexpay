package org.flexpay.orgs.actions.organization;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.filters.PaymentsCollectorFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class PaymentsCollectorDetailsAction extends FPActionWithPagerSupport<PaymentPoint> {

	protected PaymentsCollectorFilter paymentsCollectorFilter = new PaymentsCollectorFilter();
	protected List<PaymentPoint> points = Collections.emptyList();

	private PaymentsCollector paymentsCollector;

	protected OrganizationHelper organizationHelper;
	protected PaymentPointService paymentPointService;
	protected PaymentsCollectorService collectorService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		loadPaymentsCollector();

		collectorService.initFilter(paymentsCollectorFilter);
		ArrayStack filters = CollectionUtils.arrayStack(paymentsCollectorFilter);
		points = paymentPointService.listPoints(filters, getPager());

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
		return organizationHelper.getName(paymentsCollector.getOrganization(), getUserPreferences().getLocale());
	}

	public PaymentsCollector getPaymentsCollector() {
		return paymentsCollector;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setOrganizationHelper(OrganizationHelper organizationHelper) {
		this.organizationHelper = organizationHelper;
	}

	@Required
	public void setCollectorService(PaymentsCollectorService collectorService) {
		this.collectorService = collectorService;
	}

}
