package org.flexpay.payments.process.export.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.handler.ProcessInstanceExecuteHandler;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.process.export.ExportJobParameterNames.CASHBOXES;
import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_POINT_ID;

public class GetCashboxesHandler extends ProcessInstanceExecuteHandler {

	private CashboxService cashboxService;

	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		Long paymentPointId = required(PAYMENT_POINT_ID, parameters);

		List<Cashbox> cashboxes = cashboxService.findCashboxesForPaymentPoint(paymentPointId);
		List<Long> cashboxesCollection = list();

		/*if (cashboxes.size() > 0) {
			cashboxesCollection.add(cashboxes.get(0).getId());
		}      */
		for (Cashbox cashbox : cashboxes) {
			cashboxesCollection.add(cashbox.getId());
		}

		parameters.put(CASHBOXES, cashboxesCollection);

		return RESULT_NEXT;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

}
