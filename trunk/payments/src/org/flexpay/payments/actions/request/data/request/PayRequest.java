package org.flexpay.payments.actions.request.data.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.actions.request.data.response.data.ServicePayDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import static org.flexpay.common.util.CollectionUtils.list;

public class PayRequest {

    private String requestId;
    private Locale locale = Locale.ENGLISH;
    private BigDecimal totalToPay;
    private List<ServicePayDetails> servicePayDetails = list();

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public BigDecimal getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(BigDecimal totalToPay) {
        this.totalToPay = totalToPay;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public List<ServicePayDetails> getServicePayDetails() {
        return servicePayDetails;
    }

    public void setServicePayDetails(List<ServicePayDetails> servicePayDetails) {
        this.servicePayDetails = servicePayDetails;
    }

    public void addServicePayDetails(String serviceId, String serviceProviderAccount, String paySum) {

        if (servicePayDetails == null) {
            servicePayDetails = list();
        }

        ServicePayDetails spDetails = new ServicePayDetails();
        spDetails.setServiceId(Long.parseLong(serviceId));
        spDetails.setServiceProviderAccount(serviceProviderAccount);
        spDetails.setPaySum(new BigDecimal(paySum));

        servicePayDetails.add(spDetails);

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("locale", locale).
                append("totalToPay", totalToPay).
                append("servicePayDetails", servicePayDetails).
                toString();
    }
}
