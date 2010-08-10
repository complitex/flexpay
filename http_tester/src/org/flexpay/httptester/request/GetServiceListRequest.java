package org.flexpay.httptester.request;

import org.apache.commons.digester.Digester;
import org.flexpay.httptester.request.response.GetServiceListResponse;

import java.security.Signature;
import java.util.Properties;

public class GetServiceListRequest extends Request<GetServiceListResponse> {

    static {
        paramNames.add("requestId");
    }

    public GetServiceListRequest(Properties props) {
        super(props);
    }

    @Override
    protected void initResponseParser() {
        parser = new Digester();

        parser.addObjectCreate("response", GetServiceListResponse.class);
        parser.addBeanPropertySetter("response/serviceList/requestId", "requestId");
        parser.addBeanPropertySetter("response/serviceList/statusCode", "statusCode");
        parser.addBeanPropertySetter("response/serviceList/statusMessage", "statusMessage");

        parser.addObjectCreate("response/serviceList/serviceInfo", GetServiceListResponse.ServiceInfo.class);

        parser.addSetNext("response/serviceList/serviceInfo", "addServiceInfo");
        parser.addBeanPropertySetter("response/serviceList/serviceInfo/serviceId", "serviceId");
        parser.addBeanPropertySetter("response/serviceList/serviceInfo/serviceName", "serviceName");
        parser.addBeanPropertySetter("response/serviceList/serviceInfo/serviceProvider", "serviceProvider");

        parser.addBeanPropertySetter("response/signature", "signature");

    }

    @Override
    protected void addParamsToSignature() throws Exception {

        updateRequestSignature(params.get("requestId"));

    }

    @Override
    protected void addParamsToResponseSignature(Signature signature) throws Exception {

        updateSignature(signature, response.getRequestId());
        updateSignature(signature, response.getStatusCode());
        updateSignature(signature, response.getStatusMessage());

        for (GetServiceListResponse.ServiceInfo serviceInfo : response.getServiceInfos()) {

            updateSignature(signature, serviceInfo.getServiceId());
            updateSignature(signature, serviceInfo.getServiceName());
            updateSignature(signature, serviceInfo.getServiceProvider());

        }

    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "request_get_service_list.file";
    }
}
