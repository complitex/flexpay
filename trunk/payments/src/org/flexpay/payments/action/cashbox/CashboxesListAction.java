package org.flexpay.payments.action.cashbox;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.action.AccountantAWPWithPagerActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.SecurityUtil.isAuthenticationGranted;
import static org.flexpay.orgs.service.Roles.CASHBOX_READ_ALIAS_PAYMENT_COLLECTORS;

public class CashboxesListAction extends AccountantAWPWithPagerActionSupport<Cashbox> {

	protected List<Cashbox> cashboxes = list();
	protected PaymentPointFilter paymentPointFilter = new PaymentPointFilter();

	protected CashboxService cashboxService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		PaymentCollector collector = getPaymentCollector();
		if (paymentPointFilter != null && paymentPointFilter.needFilter()) {
			if (!isAuthenticationGranted(CASHBOX_READ_ALIAS_PAYMENT_COLLECTORS) && collector != null &&
					paymentPointFilter.getSelected() != null && !collector.getPaymentPoints().contains(paymentPointFilter.getSelected())) {
				cashboxes = Collections.emptyList();
				return SUCCESS;
			}
			cashboxes = cashboxService.listCashboxes(arrayStack(paymentPointFilter), getPager());
			return SUCCESS;
		}

		if (!isAuthenticationGranted(CASHBOX_READ_ALIAS_PAYMENT_COLLECTORS) && collector != null) {
			cashboxes = cashboxService.findCashboxesForPaymentCollector(Stub.stub(collector), getPager());
			return SUCCESS;
		}
		cashboxes = cashboxService.findObjects(getPager());

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public List<Cashbox> getCashboxes() {
		return cashboxes;
	}

	public PaymentPointFilter getPaymentPointFilter() {
		return paymentPointFilter;
	}

	public void setPaymentPointFilter(PaymentPointFilter paymentPointFilter) {
		this.paymentPointFilter = paymentPointFilter;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

}
