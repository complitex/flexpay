package org.flexpay.httptester.outerrequest.request;

import org.apache.commons.digester3.Digester;
import org.flexpay.httptester.outerrequest.request.response.GetDebtInfoResponse;
import org.flexpay.httptester.outerrequest.request.response.data.ServiceDetails;

import java.security.Signature;
import java.util.Properties;

public class GetDebtInfoRequest extends Request<GetDebtInfoResponse> {

    static {
        paramNames.add("requestId");
        paramNames.add("searchType");
        paramNames.add("searchCriteria");
    }

    public GetDebtInfoRequest(Properties props) {
        super(props);
    }

    @Override
    protected void initResponseParser() {
        parser = new Digester();

        parser.addObjectCreate("response", GetDebtInfoResponse.class);
        parser.addBeanPropertySetter("response/debtInfo/requestId", "requestId");
        parser.addBeanPropertySetter("response/debtInfo/statusCode", "statusCode");
        parser.addBeanPropertySetter("response/debtInfo/statusMessage", "statusMessage");

        parser.addObjectCreate("response/debtInfo/serviceDetails", ServiceDetails.class);
        parser.addSetNext("response/debtInfo/serviceDetails", "addServiceDetails");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/serviceId", "serviceId");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/serviceName", "serviceName");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/amount", "amount");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/serviceProviderAccount", "serviceProviderAccount");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/personFirstName", "personFirstName");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/personMiddleName", "personMiddleName");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/personLastName", "personLastName");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/townName", "townName");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/townType", "townType");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/streetName", "streetName");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/streetType", "streetType");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/buildingNumber", "buildingNumber");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/buildingBulk", "buildingBulk");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/apartmentNumber", "apartmentNumber");
        parser.addBeanPropertySetter("response/debtInfo/serviceDetails/roomNumber", "roomNumber");

        parser.addBeanPropertySetter("response/signature", "signature");

    }

    @Override
    protected void addParamsToSignature() throws Exception {
        updateRequestSignature(params.get("requestId"));
        updateRequestSignature(params.get("searchType"));
        updateRequestSignature(params.get("searchCriteria"));
    }

    @Override
    protected void addParamsToResponseSignature(Signature signature) throws Exception {
        updateSignature(signature, response.getRequestId());
        updateSignature(signature, response.getStatusCode());
        updateSignature(signature, response.getStatusMessage());

        for (ServiceDetails serviceDetails : response.getServiceDetailses()) {

            updateSignature(signature, serviceDetails.getServiceId());
            updateSignature(signature, serviceDetails.getServiceName());
            updateSignature(signature, serviceDetails.getAmount());
            updateSignature(signature, serviceDetails.getServiceProviderAccount());
            updateSignature(signature, serviceDetails.getPersonFirstName());
            updateSignature(signature, serviceDetails.getPersonMiddleName());
            updateSignature(signature, serviceDetails.getPersonLastName());
            updateSignature(signature, serviceDetails.getTownName());
            updateSignature(signature, serviceDetails.getTownType());
            updateSignature(signature, serviceDetails.getStreetName());
            updateSignature(signature, serviceDetails.getStreetType());
            updateSignature(signature, serviceDetails.getBuildingNumber());
            updateSignature(signature, serviceDetails.getBuildingBulk());
            updateSignature(signature, serviceDetails.getApartmentNumber());
            updateSignature(signature, serviceDetails.getRoomNumber());
        }
    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "request_get_debt_info.file";
    }


}
