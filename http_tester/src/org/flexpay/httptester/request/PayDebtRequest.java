package org.flexpay.httptester.request;

import org.apache.commons.digester.Digester;
import org.flexpay.httptester.request.response.PayResponse;

import java.security.Signature;
import java.util.Properties;

public class PayDebtRequest extends Request<PayResponse> {

    static {
        paramNames.add("requestId");
        paramNames.add("totalPaySum");
        paramNames.add("serviceId1");
        paramNames.add("serviceProviderAccount1");
        paramNames.add("paySum1");
    }

    public PayDebtRequest(Properties props) {
        super(props);
    }

    @Override
    protected void initResponseParser() {

        parser = new Digester();

        parser.addObjectCreate("response", PayResponse.class);
        parser.addBeanPropertySetter("response/payInfo/requestId", "requestId");
        parser.addBeanPropertySetter("response/payInfo/statusCode", "statusCode");
        parser.addBeanPropertySetter("response/payInfo/statusMessage", "statusMessage");

        parser.addBeanPropertySetter("response/payInfo/operationId", "operationId");

        parser.addObjectCreate("response/payInfo/servicePayInfo", PayResponse.ServicePayInfo.class);

        parser.addSetNext("response/payInfo/servicePayInfo", "addServicePayInfo");
        parser.addBeanPropertySetter("response/payInfo/servicePayInfo/serviceId", "serviceId");
        parser.addBeanPropertySetter("response/payInfo/servicePayInfo/documentId", "documentId");
        parser.addBeanPropertySetter("response/payInfo/servicePayInfo/serviceStatusCode", "statusCode");
        parser.addBeanPropertySetter("response/payInfo/servicePayInfo/serviceStatusMessage", "statusMessage");

        parser.addBeanPropertySetter("response/signature", "signature");

    }

    @Override
    protected void addParamsToSignature() throws Exception {

        updateRequestSignature(params.get("requestId"));
        updateRequestSignature(params.get("totalPaySum"));
        updateRequestSignature(params.get("serviceId1"));
        updateRequestSignature(params.get("serviceProviderAccount1"));
        updateRequestSignature(params.get("paySum1"));

    }

    @Override
    protected void addParamsToResponseSignature(Signature signature) throws Exception {

        updateSignature(signature, response.getRequestId());
        updateSignature(signature, response.getOperationId());
        updateSignature(signature, response.getStatusCode());
        updateSignature(signature, response.getStatusMessage());

        for (PayResponse.ServicePayInfo servicePayInfo : response.getServicePayInfos()) {

            updateSignature(signature, servicePayInfo.getServiceId());
            updateSignature(signature, servicePayInfo.getDocumentId());
            updateSignature(signature, servicePayInfo.getStatusCode());
            updateSignature(signature, servicePayInfo.getStatusMessage());

        }

    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "request_pay.file";
    }

}