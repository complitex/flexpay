package org.flexpay.payments.actions.request.data.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.actions.request.data.response.data.ServicePayInfo;

import java.io.Serializable;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class PayInfoResponse extends SimpleResponse implements Serializable {

    private Long operationId;

    private List<ServicePayInfo> servicePayInfos = list();

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
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
                append("requestId", requestId).
                append("operationId", operationId).
                append("status", status).
                append("servicePayInfos", servicePayInfos).
                toString();
    }
}
