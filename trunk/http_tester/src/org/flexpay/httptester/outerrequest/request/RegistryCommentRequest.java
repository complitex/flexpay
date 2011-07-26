package org.flexpay.httptester.outerrequest.request;

import org.apache.commons.digester3.Digester;
import org.flexpay.httptester.outerrequest.request.response.RegistryCommentResponse;

import java.security.Signature;
import java.util.Properties;

public class RegistryCommentRequest extends Request<RegistryCommentResponse> {

    static {
        paramNames.add("requestId");
        paramNames.add("registryId");
        paramNames.add("orderNumber");
        paramNames.add("orderDate");
        paramNames.add("orderComment");
    }

    public RegistryCommentRequest(Properties props) {
        super(props);
    }

    @Override
    protected void initResponseParser() {

        parser = new Digester();

        parser.addObjectCreate("response", RegistryCommentResponse.class);
        parser.addBeanPropertySetter("response/registryCommentInfo/requestId", "requestId");
        parser.addBeanPropertySetter("response/registryCommentInfo/statusCode", "statusCode");
        parser.addBeanPropertySetter("response/registryCommentInfo/statusMessage", "statusMessage");
        parser.addBeanPropertySetter("response/signature", "signature");

    }

    @Override
    protected void addParamsToSignature() throws Exception {

        updateRequestSignature(params.get("requestId"));
        updateRequestSignature(params.get("registryId"));
        updateRequestSignature(params.get("orderNumber"));
        updateRequestSignature(params.get("orderDate"));
        updateRequestSignature(params.get("orderComment"));

    }

    @Override
    public void addParamsToResponseSignature(Signature signature) throws Exception {

        updateSignature(signature, response.getRequestId());
        updateSignature(signature, response.getStatusCode());
        updateSignature(signature, response.getStatusMessage());

    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "request_registry_comment.file";
    }

}
