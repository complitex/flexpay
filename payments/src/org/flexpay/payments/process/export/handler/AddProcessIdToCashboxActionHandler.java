package org.flexpay.payments.process.export.handler;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.CASHBOXES;
import static org.flexpay.payments.process.export.ExportJobParameterNames.CURRENT_INDEX_CASHBOX;

public class AddProcessIdToCashboxActionHandler extends FlexPayActionHandler {

	private CashboxService cashboxService;

	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {

		Integer index = (Integer) parameters.get(CURRENT_INDEX_CASHBOX);
		Long[] cashboxes = (Long[]) parameters.get(CASHBOXES);

		Long cashboxId = cashboxes[index];

		log.debug("Cashbox: {}, process instance id: {}", cashboxId, getProcessId());

		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));

		if (cashbox == null) {
			log.error("Cashbox {} did not find", cashboxId);
			return RESULT_ERROR;
		}

		cashbox.setTradingDayProcessInstanceId(getProcessId());
		try {
			cashboxService.update(cashbox);
		} catch (FlexPayExceptionContainer flexPayExceptionContainer) {
			log.error("Failed update cashbox", flexPayExceptionContainer);
		}

		return RESULT_NEXT;
	}

	@Required
	public void setCashboxService(CashboxService cashPointService) {
		this.cashboxService = cashPointService;
	}
}
