package org.flexpay.httptester.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class PayDebtResponse {

    private String requestId;
    private String operationId;
    private String statusCode;
    private String statusMessage;
	private String signature;

    private List<ServicePayInfo> servicePayInfos = new ArrayList<ServicePayInfo>();

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<ServicePayInfo> getServicePayInfos() {
        return servicePayInfos;
    }

    public void setServicePayInfos(List<ServicePayInfo> servicePayInfos) {
        this.servicePayInfos = servicePayInfos;
    }

    public void addServicePayInfo(ServicePayInfo servicePayInfo) {
        servicePayInfos.add(servicePayInfo);
    }

    public static class ServicePayInfo {

        private String serviceId;
        private String documentId;
        private String statusCode;
        private String statusMessage;

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getDocumentId() {
            return documentId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
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
                    append("serviceId", serviceId).
                    append("documentId", documentId).
                    append("statusCode", statusCode).
                    append("statusMessage", statusMessage).
                    toString();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("operationId", operationId).
                append("statusCode", statusCode).
                append("statusMessage", statusMessage).
                append("signature", signature).
                append("servicePayInfos", servicePayInfos).
                toString();
    }
}
