package org.flexpay.payments.process.export.handler;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.CASH_BOXES;
import static org.flexpay.payments.process.export.ExportJobParameterNames.CURRENT_INDEX_CASH_BOX;

public class SendSignalToCashBoxParentActionHandler extends SendSignalToProcessActionHandler {

	private CashboxService cashboxService;

	@Override
	public Long getProcessId(Map<String, Object> parameters) {
		Integer index = (Integer)parameters.get(CURRENT_INDEX_CASH_BOX);
		Long[] cashBoxes = (Long[])parameters.get(CASH_BOXES);

		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashBoxes[index]));

		if (cashbox == null) {
			log.error("Cash box {} did not find", cashBoxes[index]);
			return null;
		}

		return cashbox.getPaymentPoint().getTradingDayProcessInstanceId();

	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}
}
