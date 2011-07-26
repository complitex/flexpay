package org.flexpay.httptester.outerrequest.request.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class GetServiceListResponse extends Response {

    private List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();

    public List<ServiceInfo> getServiceInfos() {
        return serviceInfos;
    }

    public void setServiceInfos(List<ServiceInfo> serviceInfos) {
        this.serviceInfos = serviceInfos;
    }

    public void addServiceInfo(ServiceInfo serviceInfo) {
        serviceInfos.add(serviceInfo);
    }

    public static class ServiceInfo {

        private String serviceId;
        private String serviceName;
        private String serviceProvider;

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceProvider() {
            return serviceProvider;
        }

        public void setServiceProvider(String serviceProvider) {
            this.serviceProvider = serviceProvider;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                    append("serviceId", serviceId).
                    append("serviceName", serviceName).
                    append("serviceProvider", serviceProvider).
                    toString();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("statusCode", statusCode).
                append("statusMessage", statusMessage).
                append("signature", signature).
                append("serviceInfos", serviceInfos).
                toString();
    }
}
