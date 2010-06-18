package org.flexpay.payments.actions.request.data.request.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ReversalPay {

    private String requestId;
    private Long operationId;
    private String totalPaySum;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public String getTotalPaySum() {
        return totalPaySum;
    }

    public void setTotalPaySum(String totalPaySum) {
        this.totalPaySum = totalPaySum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("operationId", operationId).
                append("totalPaySum", totalPaySum).
                toString();
    }
}
