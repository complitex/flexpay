package org.flexpay.httptester.outerrequest.request;

import org.apache.commons.digester3.Digester;
import org.flexpay.httptester.outerrequest.request.response.data.GetAddressMasterIndexResponse;

import java.security.Signature;
import java.util.Properties;

/**
 * @author Pavel Sknar
 *         Date: 08.11.12 14:04
 */
public class GetAddressMasterIndexRequest extends Request<GetAddressMasterIndexResponse> {

    static {
        paramNames.add("requestId");
        paramNames.add("parentMasterIndex");
        paramNames.add("parentAddressInfoType");
        paramNames.add("searchingData");
    }

    public GetAddressMasterIndexRequest(Properties props) {
        super(props);
    }

    @Override
    protected void initResponseParser() {
        parser = new Digester();

        parser.addObjectCreate("response", GetAddressMasterIndexResponse.class);
        parser.addBeanPropertySetter("response/addressMasterIndex/requestId", "requestId");
        parser.addBeanPropertySetter("response/addressMasterIndex/statusCode", "statusCode");
        parser.addBeanPropertySetter("response/addressMasterIndex/statusMessage", "statusMessage");

        parser.addBeanPropertySetter("response/addressMasterIndex/addressInfo/addressInfoType", "addressInfoType");

        parser.addObjectCreate("response/addressMasterIndex/addressInfo/item", GetAddressMasterIndexResponse.AddressInfo.class);

        parser.addSetNext("response/addressMasterIndex/addressInfo/item", "addAddressInfo");
        parser.addBeanPropertySetter("response/addressMasterIndex/addressInfo/item/masterIndex", "masterIndex");
        parser.addBeanPropertySetter("response/addressMasterIndex/addressInfo/item/data", "data");

        parser.addBeanPropertySetter("response/signature", "signature");

    }

    @Override
    protected void addParamsToSignature() throws Exception {

        updateRequestSignature(params.get("requestId"));
        updateRequestSignature(params.get("parentMasterIndex"));
        updateRequestSignature(params.get("parentAddressInfoType"));
        updateRequestSignature(params.get("searchingData"));

    }

    @Override
    protected void addParamsToResponseSignature(Signature signature) throws Exception {

        updateSignature(signature, response.getRequestId());
        updateSignature(signature, response.getStatusCode());
        updateSignature(signature, response.getStatusMessage());
        updateSignature(signature, response.getAddressInfoType());

        for (GetAddressMasterIndexResponse.AddressInfo addressInfo : response.getAddressInfos()) {

            updateSignature(signature, addressInfo.getMasterIndex());
            updateSignature(signature, addressInfo.getData());
        }

    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "request_get_address_master_index.file";
    }
}
