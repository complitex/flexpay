package org.flexpay.payments.process.export.handler;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.CASH_BOXES;
import static org.flexpay.payments.process.export.ExportJobParameterNames.CURRENT_INDEX_CASH_BOX;

public class AddProcessIdToCashBoxActionHandler extends FlexPayActionHandler {

	private CashboxService cashboxService;

	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {

		Integer index = (Integer)parameters.get(CURRENT_INDEX_CASH_BOX);
		Long[] cashBoxes = (Long[])parameters.get(CASH_BOXES);

		Long cashBoxId = cashBoxes[index];

		log.debug("Cashbox: {}, process instance id: {}", cashBoxId, getProcessId());

		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashBoxId));

		if (cashbox == null) {
			log.error("Cash box {} did not find", cashBoxId);
			return RESULT_ERROR;
		}

		cashbox.setTradingDayProcessInstanceId(getProcessId());
		try {
			cashboxService.update(cashbox);
		} catch (FlexPayExceptionContainer flexPayExceptionContainer) {
			log.error("Failed update cash box", flexPayExceptionContainer);
		}

		return RESULT_NEXT;
	}

	@Required
	public void setCashboxService(CashboxService cashPointService) {
		this.cashboxService = cashPointService;
	}
}
