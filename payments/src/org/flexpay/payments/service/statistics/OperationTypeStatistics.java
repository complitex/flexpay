package org.flexpay.payments.service.statistics;

import java.math.BigDecimal;

public class OperationTypeStatistics {

	private int operationTypeCode;
	private long count;
	private BigDecimal summ;

	public int getOperationTypeCode() {
		return operationTypeCode;
	}

	public void setOperationTypeCode(int operationTypeCode) {
		this.operationTypeCode = operationTypeCode;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public BigDecimal getSumm() {
		return summ;
	}

	public void setSumm(BigDecimal summ) {
		this.summ = summ;
	}
}
