package org.flexpay.payments.actions;

/**
 * Interface represents action which provides information about payment point identifier
 *
 * According to secure requirements ALL THE ACTIONS IN PAYMENTS MUST IMPLEMENT IT
 */
public interface PaymentPointAwareAction {

	Long getPaymentPointId();

	void setPaymentPointId(Long paymentPointId);

	Long getOrganizationId();

	void setOrganizationId(Long organizationId);
}
