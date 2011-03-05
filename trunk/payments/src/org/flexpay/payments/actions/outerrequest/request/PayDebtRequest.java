package org.flexpay.payments.actions.outerrequest.request;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.actions.outerrequest.request.data.ServicePayDetails;
import org.flexpay.payments.actions.outerrequest.request.response.PayDebtResponse;
import org.flexpay.payments.actions.outerrequest.request.response.data.ServicePayInfo;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.Signature;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class PayDebtRequest extends Request<PayDebtResponse> {

    private BigDecimal totalPaySum;
    private List<ServicePayDetails> servicePayDetails = list();

    public PayDebtRequest() {
        super(new PayDebtResponse());
    }

    @Override
    public List<byte[]> getFieldsToSign() throws UnsupportedEncodingException {
        List<byte[]> bytes = list(getBytes(totalPaySum.toString()));
        for (ServicePayDetails payDetails : servicePayDetails) {
            bytes.add(getBytes(payDetails.getServiceId().toString()));
            bytes.add(getBytes(payDetails.getServiceProviderAccount()));
            bytes.add(getBytes(payDetails.getPaySum().toString()));
        }
        return bytes;
    }

    @Override
    public boolean validate() {
        BigDecimal sum = new BigDecimal("0.00");
        for (ServicePayDetails spd : servicePayDetails) {
            sum = sum.add(spd.getPaySum());
        }
        log.debug("sum = {}, totalPaySum = {}", sum.toString(), totalPaySum);
        return sum.equals(totalPaySum);
    }

    @Override
    protected void addResponseBody(Signature signature) throws FlexPayException {

        addFieldToResponse(signature, "operationId", response.getOperationId());

        if (response.getServicePayInfos() != null) {
            for (ServicePayInfo servicePayInfo : response.getServicePayInfos()) {
                sResponse.append("<servicePayInfo>");
                addFieldToResponse(signature, "serviceId", servicePayInfo.getServiceId());
                addFieldToResponse(signature, "documentId", servicePayInfo.getDocumentId());
                addFieldToResponse(signature, "serviceStatusCode", servicePayInfo.getServiceStatus().getCode());
                addFieldToResponse(signature, "serviceStatusMessage", getStatusText(servicePayInfo.getServiceStatus()));
                sResponse.append("</servicePayInfo>");
            }
        }

    }

    public static void configParser(@NotNull Digester digester) {
        digester.addObjectCreate("request/payDebt", PayDebtRequest.class);
        digester.addSetNext("request/payDebt", "setRequest");
        digester.addBeanPropertySetter("request/payDebt/totalPaySum", "totalPaySum");
        digester.addBeanPropertySetter("request/payDebt/requestId", "requestId");
        digester.addCallMethod("request/payDebt/servicePayDetails", "addServicePayDetails", 3);
        digester.addCallParam("request/payDebt/servicePayDetails/serviceId", 0);
        digester.addCallParam("request/payDebt/servicePayDetails/serviceProviderAccount", 1);
        digester.addCallParam("request/payDebt/servicePayDetails/paySum", 2);
    }

    @Override
    public void copyResponse(PayDebtResponse res) {
        response.setStatus(res.getStatus());
        response.setOperationId(res.getOperationId());
        response.setServicePayInfos(res.getServicePayInfos());
    }

    public BigDecimal getTotalPaySum() {
        return totalPaySum;
    }

    public void setTotalPaySum(BigDecimal totalPaySum) {
        this.totalPaySum = totalPaySum;
    }

    public void setTotalPaySum(String totalPaySum) {
        this.totalPaySum = new BigDecimal(totalPaySum).setScale(2);
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
        spDetails.setPaySum(new BigDecimal(paySum).setScale(2));

        servicePayDetails.add(spDetails);

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("login", login).
                append("totalPaySum", totalPaySum).
                append("servicePayDetails", servicePayDetails).
                append("requestSignatureString", requestSignatureString).
                append("locale", locale).
                append("response", response).
                toString();
    }
}
