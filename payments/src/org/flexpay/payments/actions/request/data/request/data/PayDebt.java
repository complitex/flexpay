package org.flexpay.payments.actions.request.data.request.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class PayDebt {

    private String requestId;
    private String totalPaySum;

    private List<ServicePayDetails> servicePayDetails = new ArrayList<ServicePayDetails>();

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTotalPaySum() {
        return totalPaySum;
    }

    public void setTotalPaySum(String totalPaySum) {
        this.totalPaySum = totalPaySum;
    }

    public List<ServicePayDetails> getServicePayDetails() {
        return servicePayDetails;
    }

    public void setServicePayDetails(List<ServicePayDetails> servicePayDetails) {
        this.servicePayDetails = servicePayDetails;
    }

    public void addServicePayDetails(ServicePayDetails servicePayDetails) {
        this.servicePayDetails.add(servicePayDetails);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("totalPaySum", totalPaySum).
                append("servicePayDetails", servicePayDetails).
                toString();
    }
}
