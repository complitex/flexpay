package org.flexpay.payments.actions.outerrequest.request;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.payments.actions.outerrequest.request.response.RegistryCommentResponse;
import org.jetbrains.annotations.NotNull;

import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistryCommentRequest extends Request<RegistryCommentResponse> {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(RegistryContainer.COMMENTARY_PAYMENT_DATE_FORMAT);

    private Long registryId;
    private String orderNumber;
    private Date orderDate;
    private String orderComment;

    public RegistryCommentRequest() {
        super(new RegistryCommentResponse());
    }

    @Override
    public void updateRequestSignature(Signature signature) throws Exception {
        updateSignature(signature, registryId.toString());
        updateSignature(signature, orderNumber);
        updateSignature(signature, getOrderDate());
        updateSignature(signature, orderComment);
    }

    @Override
    public boolean validate() {
        //TODO
        return true;
    }

    @Override
    protected void addResponseBody(Signature signature) throws FlexPayException {

    }

    public static void configParser(@NotNull Digester digester) {
        digester.addObjectCreate("request/registryComment", RegistryCommentRequest.class);
        digester.addSetNext("request/registryComment", "setRequest");
        digester.addBeanPropertySetter("request/registryComment/requestId", "requestId");
        digester.addBeanPropertySetter("request/registryComment/registryId", "registryId");
        digester.addBeanPropertySetter("request/registryComment/orderNumber", "orderNumber");
        digester.addBeanPropertySetter("request/registryComment/orderDate", "orderDate");
        digester.addBeanPropertySetter("request/registryComment/orderComment", "orderComment");
    }

    @Override
    public void copyResponse(RegistryCommentResponse res) {
        response.setStatus(res.getStatus());
    }

    public Long getRegistryId() {
        return registryId;
    }

    public void setRegistryId(Long registryId) {
        this.registryId = registryId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return DATE_FORMAT.format(orderDate);
    }

    public void setOrderDate(String orderDate) {
        try {
            this.orderDate = DATE_FORMAT.parse(orderDate);
        } catch (Exception e) {
            log.error("Can't parse request date parameter orderDate - {}", orderDate);
        }
    }

    public Date getOrderDateDate() {
        return orderDate;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("login", login).
                append("registryId", registryId).
                append("orderNumber", orderNumber).
                append("orderDate", orderDate).
                append("orderComment", orderComment).
                append("requestSignatureString", requestSignatureString).
                append("locale", locale).
                append("response", response).
                toString();
    }
}
