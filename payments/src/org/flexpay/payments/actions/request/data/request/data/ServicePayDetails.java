package org.flexpay.payments.actions.request.data.request.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ServicePayDetails {

    private String serviceId;
    private String serviceProviderAccount;
    private String paySum;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceProviderAccount() {
        return serviceProviderAccount;
    }

    public void setServiceProviderAccount(String serviceProviderAccount) {
        this.serviceProviderAccount = serviceProviderAccount;
    }

    public String getPaySum() {
        return paySum;
    }

    public void setPaySum(String paySum) {
        this.paySum = paySum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("serviceId", serviceId).
                append("serviceProviderAccount", serviceProviderAccount).
                append("paySum", paySum).
                toString();
    }
}
