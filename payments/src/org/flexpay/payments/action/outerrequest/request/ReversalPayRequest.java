package org.flexpay.payments.action.outerrequest.request;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.action.outerrequest.request.response.ReversalPayResponse;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.Signature;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class ReversalPayRequest extends Request<ReversalPayResponse> {

    private Long operationId;
    private BigDecimal totalPaySum;

    public ReversalPayRequest() {
        super(new ReversalPayResponse());
    }

    @Override
    public List<byte[]> getFieldsToSign() throws UnsupportedEncodingException {
        return list(getBytes(operationId.toString()), getBytes(totalPaySum.toString()));
    }

    @Override
    public boolean validate() {
        log.debug("operationId = {}, totalPaySum = {}", operationId, totalPaySum);
        return true;
    }

    @Override
    protected void addResponseBody(Signature signature) throws FlexPayException {

    }

    public static void configParser(@NotNull Digester digester) {
        digester.addObjectCreate("request/reversalPay", ReversalPayRequest.class);
        digester.addSetNext("request/reversalPay", "setRequest");
        digester.addBeanPropertySetter("request/reversalPay/requestId", "requestId");
        digester.addBeanPropertySetter("request/reversalPay/operationId", "operationId");
        digester.addBeanPropertySetter("request/reversalPay/totalPaySum", "totalPaySum");
    }

    @Override
    public void copyResponse(ReversalPayResponse res) {
        response.setStatus(res.getStatus());
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public BigDecimal getTotalPaySum() {
        return totalPaySum;
    }

    public void setTotalPaySum(BigDecimal totalPaySum) {
        this.totalPaySum = totalPaySum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("login", login).
                append("operationId", operationId).
                append("totalPaySum", totalPaySum).
                append("requestSignatureString", requestSignatureString).
                append("locale", locale).
                append("response", response).
                toString();
    }
}
