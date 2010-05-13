package org.flexpay.payments.persistence.quittance;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class PayRequest {

    private BigDecimal totalToPay;
    private List<ServicePayDetails> servicePayDetails = list();

    public BigDecimal getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(BigDecimal totalToPay) {
        this.totalToPay = totalToPay;
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
                append("totalToPay", totalToPay).
                append("servicePayDetails", servicePayDetails).
                toString();
    }
}
