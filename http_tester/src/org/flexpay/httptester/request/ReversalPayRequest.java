package org.flexpay.httptester.request;

import org.apache.commons.digester.Digester;
import org.flexpay.httptester.request.response.ReversalPayResponse;

import java.security.Signature;
import java.util.Properties;

public class ReversalPayRequest extends Request<ReversalPayResponse> {

    static {
        paramNames.add("requestId");
        paramNames.add("operationId");
        paramNames.add("totalPaySum");
    }

    public ReversalPayRequest(Properties props) {
        super(props);
    }

    @Override
    protected void initResponseParser() {

        parser = new Digester();

        parser.addObjectCreate("response", ReversalPayResponse.class);
        parser.addBeanPropertySetter("response/reversalInfo/requestId", "requestId");
        parser.addBeanPropertySetter("response/reversalInfo/statusCode", "statusCode");
        parser.addBeanPropertySetter("response/reversalInfo/statusMessage", "statusMessage");
        parser.addBeanPropertySetter("response/signature", "signature");

    }

    @Override
    protected void addParamsToSignature() throws Exception {

        updateRequestSignature(params.get("requestId"));
        updateRequestSignature(params.get("operationId"));
        updateRequestSignature(params.get("totalPaySum"));

    }

    @Override
    public void addParamsToResponseSignature(Signature signature) throws Exception {

        updateSignature(signature, response.getRequestId());
        updateSignature(signature, response.getStatusCode());
        updateSignature(signature, response.getStatusMessage());

    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "request_reversal_pay.file";
    }

}
