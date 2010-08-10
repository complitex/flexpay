package org.flexpay.payments.actions.outerrequest.request.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.actions.outerrequest.request.response.data.ServicePayInfo;

import java.io.Serializable;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class PayDebtResponse extends Response implements Serializable {

    public final static String TAG_NAME = "payInfo";

    private Long operationId;

    private List<ServicePayInfo> servicePayInfos = list();

    @Override
    public String getTagName() {
        return TAG_NAME;
    }

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
                append("status", status).
                append("operationId", operationId).
                append("servicePayInfos", servicePayInfos).
                toString();
    }
}
