package org.flexpay.payments.action.outerrequest.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class OuterRequest {

    private Request<?> request;

    public void setLogin(String login) {
        request.setLogin(login);
    }

    public void setSignature(String signature) {
        request.setRequestSignatureString(signature);
    }

    public Request<?> getRequest() {
        return request;
    }

    public void setRequest(Request<?> request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("request", request).
                toString();
    }
}
