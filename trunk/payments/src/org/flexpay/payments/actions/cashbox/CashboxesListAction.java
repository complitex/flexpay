package org.flexpay.payments.actions.cashbox;

import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.filters.PaymentPointsFilter;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;

public class CashboxesListAction extends AccountantAWPWithPagerActionSupport<Cashbox> {

	protected List<Cashbox> cashboxes = list();
	protected PaymentPointsFilter paymentPointsFilter = new PaymentPointsFilter();

	protected CashboxService cashboxService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (paymentPointsFilter != null && paymentPointsFilter.needFilter()) {
			cashboxes = cashboxService.listCashboxes(arrayStack(paymentPointsFilter), getPager());
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

	public PaymentPointsFilter getPaymentPointsFilter() {
		return paymentPointsFilter;
	}

	public void setPaymentPointsFilter(PaymentPointsFilter paymentPointsFilter) {
		this.paymentPointsFilter = paymentPointsFilter;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

}
