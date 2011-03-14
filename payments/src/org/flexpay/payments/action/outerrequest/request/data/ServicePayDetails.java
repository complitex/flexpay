package org.flexpay.payments.action.outerrequest.request.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.math.BigDecimal;

public class ServicePayDetails {

    private Long serviceId;
    private String serviceProviderAccount;
    private BigDecimal paySum;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceProviderAccount() {
        return serviceProviderAccount;
    }

    public void setServiceProviderAccount(String serviceProviderAccount) {
        this.serviceProviderAccount = serviceProviderAccount;
    }

    public BigDecimal getPaySum() {
        return paySum;
    }

    public void setPaySum(BigDecimal paySum) {
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
