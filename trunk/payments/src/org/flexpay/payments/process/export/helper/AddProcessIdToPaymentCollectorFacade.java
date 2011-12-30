package org.flexpay.payments.process.export.helper;

/**
 * @author Pavel Sknar
 *         Date: 29.12.11 19:53
 */
public interface AddProcessIdToPaymentCollectorFacade {

    boolean facade(Long paymentCollectorId, Long processInstanceId);

}
