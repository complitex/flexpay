package org.flexpay.payments.process.export.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.handler.ProcessInstanceExecuteHandler;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.service.OperationService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_COLLECTOR_ID;

/**
 * @author Pavel Sknar
 *         Date: 21.10.11 15:35
 */
public class DeleteBlankOperationsHandler extends ProcessInstanceExecuteHandler {

	private PaymentCollectorService paymentCollectorService;
	private OperationService operationService;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		log.info("Start delete blank operations...");

		PaymentCollector paymentCollector = getPaymentCollector(parameters);
		if (paymentCollector == null) {
			return RESULT_ERROR;
		}

		log.debug("Payment collector found: {}", paymentCollector);

		operationService.deleteBlankOperations(Stub.stub(paymentCollector));

		return RESULT_NEXT;
	}

	private PaymentCollector getPaymentCollector(Map<String, Object> parameters) {
		Long paymentCollectorId = (Long) parameters.get(PAYMENT_COLLECTOR_ID);
        if (paymentCollectorId == null) {
            log.error("Can't find {} paramenter", PAYMENT_COLLECTOR_ID);
            return null;
        }
		PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));

		if (paymentCollector == null) {
			log.error("Payment point was not found (searching by id {})", parameters.get(PAYMENT_COLLECTOR_ID));
		}
		return paymentCollector;
	}

	@Required
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}
}
