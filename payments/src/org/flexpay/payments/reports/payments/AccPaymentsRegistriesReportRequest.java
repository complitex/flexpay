package org.flexpay.payments.reports.payments;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class AccPaymentsRegistriesReportRequest extends AccReportRequest {

    private Long serviceProviderId;

    public Long getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Long serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("format", format).
                append("beginDate", beginDate).
                append("endDate", endDate).
                append("paymentCollectorId", paymentCollectorId).
				append("requiredFileterdByPymentCollector", requiredFileterdByPymentCollector).
                append("locale", locale).
                append("serviceProviderId", serviceProviderId).
                toString();
    }
}
