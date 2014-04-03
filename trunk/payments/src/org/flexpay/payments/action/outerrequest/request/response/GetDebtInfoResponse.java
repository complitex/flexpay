package org.flexpay.payments.action.outerrequest.request.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.flexpay.payments.action.outerrequest.request.response.data.ServiceDetails;
import org.flexpay.payments.util.DebtInfoResponseDeserializer;

import java.io.Serializable;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

@JsonDeserialize(using = DebtInfoResponseDeserializer.class)
public class GetDebtInfoResponse extends SearchResponse implements Serializable {

    public final static String TAG_NAME = "debtInfo";

    private List<ServiceDetails> serviceDetailses = list();

    @Override
    public String getTagName() {
        return TAG_NAME;
    }

    public List<ServiceDetails> getServiceDetailses() {
        return serviceDetailses;
    }

    public void setServiceDetailses(List<ServiceDetails> serviceDetailses) {
        this.serviceDetailses = serviceDetailses;
    }

    public void addServiceDetails(ServiceDetails serviceDetails) {
        if (serviceDetailses == null) {
            serviceDetailses = list();
        }
        serviceDetailses.add(serviceDetails);
    }

    public void addAllServiceDetails(List<ServiceDetails> serviceDetailses) {
        if (this.serviceDetailses == null) {
            this.serviceDetailses = list();
        }
        this.serviceDetailses.addAll(serviceDetailses);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("status", status).
                append("jmsRequestId", jmsRequestId).
                append("serviceDetailses", serviceDetailses).
                toString();
    }

}
