package org.flexpay.payments.process.export.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.handler.ProcessInstanceExecuteHandler;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.CASHBOX_ID;

public class AddProcessIdToCashboxHandler extends ProcessInstanceExecuteHandler {

	private CashboxService cashboxService;

    @Transactional (readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		Long cashboxId = required(CASHBOX_ID, parameters);

		log.debug("Cashbox: {}, process instance id: {}",
				new Object[]{cashboxId, getProcessInstanceId(parameters)});

		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));

		if (cashbox == null) {
			log.error("Cashbox {} did not find", cashboxId);
			return RESULT_ERROR;
		}

		cashbox.setTradingDayProcessInstanceId(getProcessInstanceId(parameters));
		updateCashbox(cashbox);

		return RESULT_NEXT;
	}

	private void updateCashbox(Cashbox cashbox) {
		try {
			cashboxService.update(cashbox);
		} catch (FlexPayExceptionContainer flexPayExceptionContainer) {
			log.error("Failed update cashbox", flexPayExceptionContainer);
		}
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

}

