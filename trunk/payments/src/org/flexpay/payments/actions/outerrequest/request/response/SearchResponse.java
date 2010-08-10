package org.flexpay.payments.actions.outerrequest.request.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class SearchResponse extends Response {

    protected String jmsRequestId;

    public String getJmsRequestId() {
        return jmsRequestId;
    }

    public void setJmsRequestId(String jmsRequestId) {
        this.jmsRequestId = jmsRequestId;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("status", status).
                append("jmsRequestId", jmsRequestId).
                toString();
    }
}
