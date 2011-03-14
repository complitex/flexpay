package org.flexpay.payments.action.outerrequest.request.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.action.outerrequest.request.response.data.ServiceInfo;

import java.io.Serializable;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class GetServiceListResponse extends Response implements Serializable {

    public final static String TAG_NAME = "serviceList";

    private List<ServiceInfo> serviceInfos = list();

    @Override
    public String getTagName() {
        return TAG_NAME;
    }

    public List<ServiceInfo> getServiceInfos() {
        return serviceInfos;
    }

    public void setServiceInfos(List<ServiceInfo> serviceInfos) {
        this.serviceInfos = serviceInfos;
    }

    public void addServiceInfo(ServiceInfo serviceInfo) {
        if (serviceInfos == null) {
            serviceInfos = list();
        }
        serviceInfos.add(serviceInfo);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("status", status).
                append("serviceInfos", serviceInfos).
                toString();
    }

}
