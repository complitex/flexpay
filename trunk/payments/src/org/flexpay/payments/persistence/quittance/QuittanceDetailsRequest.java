package org.flexpay.payments.persistence.quittance;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Request for quittance details to external system
 */
public class QuittanceDetailsRequest implements Serializable {

	/**
	 * Quittance details should be searched by account number
	 */
	public static final int TYPE_ACCOUNT_NUMBER = 1;

	/**
	 * Quittance details should be searched by quittance number
	 */
	public static final int TYPE_QUITTANCE_NUMBER = 2;

	/**
	 * Quittance details should be searched by master index of apartment
	 */
	public static final int TYPE_APARTMENT_NUMBER = 3;

	private String requestId;
	private String request;
	private int type;

	/*
	 * Forbid objects creation
	 */
	private QuittanceDetailsRequest(String request, int type) {
		this.request = request;
		this.type = type;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * Create request from account number
	 *
	 * @param accountNumber Account number
	 * @return Request object
	 */
	public static QuittanceDetailsRequest accountNumberRequest(@NotNull String accountNumber) {
		return new QuittanceDetailsRequest(accountNumber, TYPE_ACCOUNT_NUMBER);
	}

	/**
	 * Create request from quittance number
	 *
	 * @param quittanceNumber Quittance number
	 * @return Request object
	 */
	public static QuittanceDetailsRequest quittanceNumberRequest(@NotNull String quittanceNumber) {
		return new QuittanceDetailsRequest(quittanceNumber, TYPE_QUITTANCE_NUMBER);
	}

	/**
	 * Create request from apartment master index
	 *
	 * @param apartmentNumber Apartment master index
	 * @return Request object
	 */
	public static QuittanceDetailsRequest apartmentNumberRequest(@NotNull String apartmentNumber) {
		return new QuittanceDetailsRequest(apartmentNumber, TYPE_APARTMENT_NUMBER);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("requestId", requestId).
				append("request", request).
				append("type", type).
				toString();
	}
}
