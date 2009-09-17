package org.flexpay.orgs.actions.paymentpoint;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.actions.cashbox.CashboxesListAction;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.PaymentPointsFilter;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PaymentPointDetailsAction extends CashboxesListAction {

	// filters
	private PaymentCollectorFilter paymentCollectorFilter = new PaymentCollectorFilter();
	private PaymentPointsFilter paymentPointsFilter = new PaymentPointsFilter();

	// required services
	private PaymentCollectorService paymentCollectorService;
	private PaymentPointService paymentPointService;

	@NotNull
	@Override
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

		paymentCollectorFilter.setAllowEmpty(false);

		paymentCollectorFilter.initFilter(session);
		paymentPointsFilter.initFilter(session);

		paymentCollectorService.initFilter(paymentCollectorFilter);
		paymentPointService.initFilter(CollectionUtils.arrayStack(paymentCollectorFilter), paymentPointsFilter);
	}

	// rendering utility methods
	public String getPaymentPointAddress() {

		PaymentPoint paymentPoint = paymentPointService.read(paymentPointsFilter.getSelectedStub());
		return paymentPoint.getAddress();
	}

	// filters
	public PaymentCollectorFilter getPaymentCollectorFilter() {
		return paymentCollectorFilter;
	}

	public void setPaymentCollectorFilter(PaymentCollectorFilter paymentCollectorFilter) {
		this.paymentCollectorFilter = paymentCollectorFilter;
	}

	public PaymentPointsFilter getPaymentPointsFilter() {
		return paymentPointsFilter;
	}

	public void setPaymentPointsFilter(PaymentPointsFilter paymentPointsFilter) {
		this.paymentPointsFilter = paymentPointsFilter;
	}

	// required services
	@Required
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}
}
