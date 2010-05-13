package org.flexpay.payments.persistence.quittance;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class PayInfoResponse implements Serializable {

    public static final int STATUS_SUCCESS = 1;

    public static final int STATUS_INCORRECT_AUTHENTICATION_DATA = 8;
    public static final int STATUS_UNKNOWN_REQUEST = 9;
    public static final int STATUS_INTERNAL_ERROR = 14;
    public static final int STATUS_RECIEVE_TIMEOUT = 15;
    public static final int STATUS_REQUEST_IS_NOT_PROCESSED = 18;

    private Long operationId;
    private int statusCode = STATUS_SUCCESS;
    private String statusMessage;

    private List<ServicePayInfo> servicePayInfos = list();

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public int getStatusCode() {
        return statusCode;
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

    public List<ServicePayInfo> getServicePayInfos() {
        return servicePayInfos;
    }

    public void setServicePayInfos(List<ServicePayInfo> servicePayInfos) {
        this.servicePayInfos = servicePayInfos;
    }

    public void addServicePayInfo(ServicePayInfo servicePayInfo) {
        if (servicePayInfos == null) {
            servicePayInfos = list();
        }
        servicePayInfos.add(servicePayInfo);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("operationId", operationId).
                append("statusCode", statusCode).
                append("statusMessage", statusMessage).
                append("servicePayInfos", servicePayInfos).
                toString();
    }
}
