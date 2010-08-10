package org.flexpay.httptester.request.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.httptester.request.response.data.ServiceDetails;

import java.util.ArrayList;
import java.util.List;

public class GetDebtInfoResponse extends Response {

    private List<ServiceDetails> serviceDetailses = new ArrayList<ServiceDetails>();

    public List<ServiceDetails> getServiceDetailses() {
        return serviceDetailses;
    }

    public void setServiceDetailses(List<ServiceDetails> serviceDetailses) {
        this.serviceDetailses = serviceDetailses;
    }

    public void addServiceDetails(ServiceDetails serviceDetails) {

        if (serviceDetailses == null) {
            serviceDetailses = new ArrayList<ServiceDetails>();
        }
        serviceDetailses.add(serviceDetails);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("statusCode", statusCode).
                append("statusMessage", statusMessage).
                append("serviceDetailses", serviceDetailses).
                toString();
    }
}
