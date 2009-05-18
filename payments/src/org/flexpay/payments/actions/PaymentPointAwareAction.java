package org.flexpay.payments.actions;

/**
 * Interface represents action which provides information about payment point identifier
 *
 * According to secure requirements ALL THE ACTIONS IN PAYMENTS MUST IMPLEMENT IT
 */
public interface PaymentPointAwareAction {

	String getPaymentPointId();

	void setPaymentPointId(String paymentPointId);
}
