package org.flexpay.payments.process.export.handler;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.process.export.ExportJobParameterNames.CURRENT_INDEX_PAYMENT_POINT;
import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_POINTS;
import static org.flexpay.payments.process.export.ExportJobParameterNames.CASHBOXES;

public class GetCashboxesActionHandler extends FlexPayActionHandler {

	private CashboxService cashboxService;

	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {

		Integer index = (Integer) parameters.get(CURRENT_INDEX_PAYMENT_POINT);
		Long[] paymentPoints = (Long[]) parameters.get(PAYMENT_POINTS);

		Long paymentPointId = paymentPoints[index];

		List<Cashbox> cashboxes = cashboxService.findCashboxesForPaymentPoint(paymentPointId);
		List<Long> cashboxesCollection = list();

		for (Cashbox cashbox : cashboxes) {
			cashboxesCollection.add(cashbox.getId());
		}

		parameters.put(CASHBOXES, cashboxesCollection.toArray(new Long[cashboxesCollection.size()]));

		return RESULT_NEXT;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}
}
