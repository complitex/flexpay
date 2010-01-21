package org.flexpay.payments.actions.reports;

import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.reports.payments.AccPaymentsReportRequest;

public class AccReturnedPaymentsReportAction extends AccPaymentsReportAction {

	private static final String PREFIX = "AccReturned";

	private static final String ACC_RETURNED_ALL_PAYMENT_POINTS_NO_DETAILS = PREFIX + "AllPaymentPointsNoDetails";
	private static final String ACC_RETURNED_PAYMENT_POINT_NO_DETAILS = PREFIX + "PaymentPointNoDetails";

	private static final String ACC_RETURNED_ALL_PAYMENT_POINTS_CASHBOXES = PREFIX + "AllPaymentPointsCashboxes";
	private static final String ACC_RETURNED_PAYMENT_POINT_CASHBOXES = PREFIX + "PaymentPointCashboxes";
	private static final String ACC_RETURNED_CASHBOX = PREFIX + "Cashbox";

	private static final String ACC_RETURNED_ALL_PAYMENT_POINTS_PAYMENTS = PREFIX + "AllPaymentPointsPayments";
	private static final String ACC_RETURNED_PAYMENT_POINT_PAYMENTS = PREFIX + "PaymentPointPayments";
	private static final String ACC_RETURNED_CASHBOX_PAYMENTS = PREFIX + "CashboxPayments";

	@Override
	protected int getPaymentStatus() {
		return OperationStatus.RETURNED;
	}

	@Override
	protected String getReportName(AccPaymentsReportRequest request) {

		Long paymentPointId = request.getPaymentPointId();
		Long cashboxId = request.getCashboxId();

		switch (request.getDetailsLevel()) {
			case AccPaymentsReportRequest.DETAILS_LEVEL_PAYMENT_POINT:
				if (paymentPointId == null && cashboxId == null) {
					return ACC_RETURNED_ALL_PAYMENT_POINTS_NO_DETAILS;
				} else if (paymentPointId != null && cashboxId == null) {
					return ACC_RETURNED_PAYMENT_POINT_NO_DETAILS;
				}
				break;
			case AccPaymentsReportRequest.DETAILS_LEVEL_CASHBOX:
				if (paymentPointId == null && cashboxId == null) {
					return ACC_RETURNED_ALL_PAYMENT_POINTS_CASHBOXES;
				} else if (paymentPointId != null && cashboxId == null) {
					return ACC_RETURNED_PAYMENT_POINT_CASHBOXES;
				} else if (paymentPointId != null && cashboxId != null) {
					return ACC_RETURNED_CASHBOX;
				}
				break;
			case AccPaymentsReportRequest.DETAILS_LEVEL_PAYMENT:
				if (paymentPointId == null && cashboxId == null) {
					return ACC_RETURNED_ALL_PAYMENT_POINTS_PAYMENTS;
				} else if (paymentPointId != null && cashboxId == null) {
					return ACC_RETURNED_PAYMENT_POINT_PAYMENTS;
				} else if (paymentPointId != null && cashboxId != null) {
					return ACC_RETURNED_CASHBOX_PAYMENTS;
				}
				break;
			default:
				break;
		}

		return null;
	}
}
