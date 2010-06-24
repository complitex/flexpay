package org.flexpay.payments.service.statistics;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.math.BigDecimal;

public class OperationTypeStatistics {

	private Integer operationTypeCode;
	private Long count;
	private BigDecimal sum;

    public Integer getOperationTypeCode() {
        return operationTypeCode;
    }

    public void setOperationTypeCode(Integer operationTypeCode) {
        this.operationTypeCode = operationTypeCode;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("operationTypeCode", operationTypeCode).
                append("count", count).
                append("sum", sum).
                toString();
    }
}
