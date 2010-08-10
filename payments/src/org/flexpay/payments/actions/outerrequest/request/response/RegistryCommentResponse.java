package org.flexpay.payments.actions.outerrequest.request.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class RegistryCommentResponse extends Response implements Serializable {

    public final static String TAG_NAME = "registryCommentInfo";
    
    @Override
    public String getTagName() {
        return TAG_NAME;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("status", status).
                toString();
    }

}
