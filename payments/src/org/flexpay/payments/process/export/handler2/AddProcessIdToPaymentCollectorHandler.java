package org.flexpay.payments.process.export.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.handler.ProcessInstanceExecuteHandler;
import org.flexpay.payments.process.export.helper.AddProcessIdToPaymentCollectorFacade;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_COLLECTOR_ID;

public class AddProcessIdToPaymentCollectorHandler extends ProcessInstanceExecuteHandler {
	private AddProcessIdToPaymentCollectorFacade addProcessIdToPaymentCollectorFacade;

	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

        Long paymentCollectorId = required(PAYMENT_COLLECTOR_ID, parameters);

		return addProcessIdToPaymentCollectorFacade.facade(paymentCollectorId, getProcessInstanceId(parameters))? RESULT_NEXT: RESULT_ERROR;
	}

    @Required
    public void setAddProcessIdToPaymentCollectorFacade(AddProcessIdToPaymentCollectorFacade addProcessIdToPaymentCollectorFacade) {
        this.addProcessIdToPaymentCollectorFacade = addProcessIdToPaymentCollectorFacade;
    }
}
