package org.flexpay.payments.reports.payments;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Report data holder for payments report
 */
public class PaymentReportData implements Serializable {

	private Long paymentPointId;
	private Integer operationCount;
	private Long operationId;
	private String serviceProviderAccount;
	private String fio;
	private Integer serviceTypeCode;
	private BigDecimal documentSum;
	private Long documentId;

	public Long getPaymentPointId() {
		return paymentPointId;
	}

	public void setPaymentPointId(Long paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public Long getOperationId() {
		return operationId;
	}

	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	public String getServiceProviderAccount() {
		return serviceProviderAccount;
	}

	public void setServiceProviderAccount(String serviceProviderAccount) {
		this.serviceProviderAccount = serviceProviderAccount;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public Integer getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(Integer serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public BigDecimal getDocumentSum() {
		return documentSum;
	}

	public void setDocumentSum(BigDecimal documentSum) {
		this.documentSum = documentSum;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Integer getOperationCount() {
		return operationCount;
	}

	public void setOperationCount(Integer operationCount) {
		this.operationCount = operationCount;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("paymentPointId", paymentPointId).
				append("operationCount", operationCount).
				append("operationId", operationId).
				append("serviceProviderAccount", serviceProviderAccount).
				append("fio", fio).
				append("serviceTypeCode", serviceTypeCode).
				append("documentSum", documentSum).
				append("documentId", documentId).
				toString();
	}

}
