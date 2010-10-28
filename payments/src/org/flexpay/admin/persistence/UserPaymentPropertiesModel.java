package org.flexpay.admin.persistence;

public class UserPaymentPropertiesModel {

	private String username;
	private Long paymentPointId;
	private Long paymentCollectorId;
	private Long cashBoxId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getPaymentPointId() {
		return paymentPointId;
	}

	public void setPaymentPointId(Long paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public Long getPaymentCollectorId() {
		return paymentCollectorId;
	}

	public void setPaymentCollectorId(Long paymentCollectorId) {
		this.paymentCollectorId = paymentCollectorId;
	}

	public Long getCashBoxId() {
		return cashBoxId;
	}

	public void setCashBoxId(Long cashBoxId) {
		this.cashBoxId = cashBoxId;
	}

	public static UserPaymentPropertiesModel getInstance() {
		return new UserPaymentPropertiesModel();
	}
}
