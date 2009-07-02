package org.flexpay.orgs.actions.organization;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.actions.cashbox.CashboxesListAction;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.PaymentPointsFilter;
import org.flexpay.orgs.persistence.filters.PaymentsCollectorFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PaymentPointDetailsAction extends CashboxesListAction {

	// filters
	private PaymentsCollectorFilter paymentsCollectorFilter = new PaymentsCollectorFilter();
	private PaymentPointsFilter paymentPointsFilter = new PaymentPointsFilter();

	// required services
	private PaymentsCollectorService paymentsCollectorService;
	private PaymentPointService paymentPointService;

	@NotNull
	protected String doExecute() throws Exception {

		initFilters();

		if (paymentPointsFilter.needFilter()) {
			cashboxes = cashboxService.findCashboxesForPaymentPoint(paymentPointsFilter.getSelectedId());
		} else {
			cashboxes = CollectionUtils.list();
		}

		return SUCCESS;
	}

	private void initFilters() {

		paymentsCollectorFilter.setAllowEmpty(false);

		paymentsCollectorFilter.initFilter(session);
		paymentPointsFilter.initFilter(session);

		paymentsCollectorService.initFilter(paymentsCollectorFilter);
		paymentPointService.initFilter(CollectionUtils.arrayStack(paymentsCollectorFilter), paymentPointsFilter);
	}

	// rendering utility methods
	public String getPaymentPointAddress() {

		PaymentPoint paymentPoint = paymentPointService.read(paymentPointsFilter.getSelectedStub());
		return paymentPoint.getAddress();
	}

	// filters
	public PaymentsCollectorFilter getPaymentsCollectorFilter() {
		return paymentsCollectorFilter;
	}

	public void setPaymentsCollectorFilter(PaymentsCollectorFilter paymentsCollectorFilter) {
		this.paymentsCollectorFilter = paymentsCollectorFilter;
	}

	public PaymentPointsFilter getPaymentPointsFilter() {
		return paymentPointsFilter;
	}

	public void setPaymentPointsFilter(PaymentPointsFilter paymentPointsFilter) {
		this.paymentPointsFilter = paymentPointsFilter;
	}

	// required services
	@Required
	public void setPaymentsCollectorService(PaymentsCollectorService paymentsCollectorService) {
		this.paymentsCollectorService = paymentsCollectorService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}
}
