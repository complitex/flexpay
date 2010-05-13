package org.flexpay.payments.persistence.quittance;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public abstract class DetailsResponse implements Serializable {

    public static final int STATUS_SUCCESS = 1;

    public static final int STATUS_UNKNOWN_REQUEST = 9;
    public static final int STATUS_QUITTANCE_NOT_FOUND = 10;
    public static final int STATUS_ACCOUNT_NOT_FOUND = 11;
    public static final int STATUS_APARTMENT_NOT_FOUND = 12;
    public static final int STATUS_INVALID_QUITTANCE_NUMBER = 13;
    public static final int STATUS_INTERNAL_ERROR = 14;
    public static final int STATUS_RECIEVE_TIMEOUT = 15;

    protected String requestId;
    protected int statusCode;
    protected String statusMessage;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean isSuccess() {
        return statusCode == STATUS_SUCCESS;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("statusCode", statusCode).
                append("statusMessage", statusMessage).
                toString();
    }
}
