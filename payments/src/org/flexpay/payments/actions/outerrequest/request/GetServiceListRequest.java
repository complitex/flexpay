package org.flexpay.payments.actions.outerrequest.request;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.actions.outerrequest.request.response.GetServiceListResponse;
import org.flexpay.payments.actions.outerrequest.request.response.data.ServiceInfo;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.security.Signature;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class GetServiceListRequest extends Request<GetServiceListResponse> {

    public GetServiceListRequest() {
        super(new GetServiceListResponse());
    }

    @Override
    protected void addResponseBody(Signature signature) throws FlexPayException {

        if (response.getServiceInfos() != null) {
            for (ServiceInfo serviceInfo : response.getServiceInfos()) {
                sResponse.append("<serviceInfo>");
                addFieldToResponse(signature, "serviceId", serviceInfo.getServiceId());
                addFieldToResponse(signature, "serviceName", serviceInfo.getServiceName());
                addFieldToResponse(signature, "serviceProvider", serviceInfo.getServiceProvider());
                sResponse.append("</serviceInfo>");
            }
        }

    }

    public static void configParser(@NotNull Digester digester) {
        digester.addObjectCreate("request/getServiceList", GetServiceListRequest.class);
        digester.addSetNext("request/getServiceList", "setRequest");
        digester.addBeanPropertySetter("request/getServiceList/requestId", "requestId");
    }

    @Override
    public void copyResponse(GetServiceListResponse res) {
        response.setStatus(res.getStatus());
        response.setServiceInfos(res.getServiceInfos());
    }

    @Override
    public List<byte[]> getFieldsToSign() throws UnsupportedEncodingException {
        return list();
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("requestId", requestId).
                append("login", login).
                append("requestSignatureString", requestSignatureString).
                append("locale", locale).
                append("response", response).
                toString();
    }
}
