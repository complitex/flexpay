package org.flexpay.payments.actions.cashbox;

import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;

public class CashboxesListAction extends AccountantAWPWithPagerActionSupport<Cashbox> {

	protected List<Cashbox> cashboxes = list();
	protected PaymentPointFilter paymentPointFilter = new PaymentPointFilter();

	protected CashboxService cashboxService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (paymentPointFilter != null && paymentPointFilter.needFilter()) {
			cashboxes = cashboxService.listCashboxes(arrayStack(paymentPointFilter), getPager());
		} else {
			cashboxes = cashboxService.findObjects(getPager());
		}

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
