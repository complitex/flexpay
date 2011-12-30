package org.flexpay.payments.process.export.helper;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Pavel Sknar
 *         Date: 29.12.11 19:13
 */
public class AddProcessIdToPaymentCollectorFacadeImpl implements AddProcessIdToPaymentCollectorFacade {

    private final static Logger log = LoggerFactory.getLogger(AddProcessIdToPaymentCollectorFacadeImpl.class);

    private PaymentCollectorService paymentCollectorService;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public boolean facade(Long paymentCollectorId, Long processInstanceId) {
        PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));

		if (paymentCollector == null) {
			log.error("Payment collector '{}' did not find ", paymentCollectorId);
			return false;
		}

		paymentCollector.setTradingDayProcessInstanceId(processInstanceId);

		try {
			paymentCollectorService.update(paymentCollector);
			return true;
		} catch (FlexPayExceptionContainer flexPayExceptionContainer) {
			log.error("Failed update payment collector", flexPayExceptionContainer);
		}
		return false;
	}

	@Required
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}
}
