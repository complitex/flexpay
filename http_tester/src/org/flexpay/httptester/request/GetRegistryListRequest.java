package org.flexpay.httptester.request;

import org.apache.commons.digester.Digester;
import org.flexpay.httptester.request.response.GetRegistryListResponse;

import java.security.Signature;
import java.util.Properties;

public class GetRegistryListRequest extends Request<GetRegistryListResponse> {

    static {
        paramNames.add("requestId");
        paramNames.add("periodBeginDate");
        paramNames.add("periodEndDate");
        paramNames.add("registryType");
    }

    public GetRegistryListRequest(Properties props) {
        super(props);
    }

    @Override
    protected void initResponseParser() {
        parser = new Digester();

        parser.addObjectCreate("response", GetRegistryListResponse.class);
        parser.addBeanPropertySetter("response/registryList/requestId", "requestId");
        parser.addBeanPropertySetter("response/registryList/statusCode", "statusCode");
        parser.addBeanPropertySetter("response/registryList/statusMessage", "statusMessage");

        parser.addObjectCreate("response/registryList/registryInfo", GetRegistryListResponse.RegistryInfo.class);

        parser.addSetNext("response/registryList/registryInfo", "addRegistryInfo");
        parser.addBeanPropertySetter("response/registryList/registryInfo/registryId", "registryId");
        parser.addBeanPropertySetter("response/registryList/registryInfo/registryDate", "registryDate");
        parser.addBeanPropertySetter("response/registryList/registryInfo/registryType", "registryType");
        parser.addBeanPropertySetter("response/registryList/registryInfo/recordsCount", "recordsCount");
        parser.addBeanPropertySetter("response/registryList/registryInfo/totalSum", "totalSum");
        parser.addBeanPropertySetter("response/registryList/registryInfo/registryComment", "registryComment");

        parser.addBeanPropertySetter("response/signature", "signature");

    }

    @Override
    protected void addParamsToSignature() throws Exception {

        updateRequestSignature(params.get("requestId"));
        updateRequestSignature(params.get("periodBeginDate"));
        updateRequestSignature(params.get("periodEndDate"));
        updateRequestSignature(params.get("registryType"));

    }

    @Override
    protected void addParamsToResponseSignature(Signature signature) throws Exception {

        updateSignature(signature, response.getRequestId());
        updateSignature(signature, response.getStatusCode());
        updateSignature(signature, response.getStatusMessage());

        for (GetRegistryListResponse.RegistryInfo registryInfo : response.getRegistryInfos()) {

            updateSignature(signature, registryInfo.getRegistryId());
            updateSignature(signature, registryInfo.getRegistryDate());
            updateSignature(signature, registryInfo.getRegistryType());
            updateSignature(signature, registryInfo.getRecordsCount());
            updateSignature(signature, registryInfo.getTotalSum());
            updateSignature(signature, registryInfo.getRegistryComment());

        }

    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "request_get_registry_list.file";
    }
}
