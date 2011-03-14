package org.flexpay.payments.action.outerrequest.request;

import org.apache.commons.digester.Digester;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.action.outerrequest.request.response.GetDebtInfoResponse;
import org.flexpay.payments.action.outerrequest.request.response.data.ServiceDetails;
import org.jetbrains.annotations.NotNull;

import java.security.Signature;

public class GetDebtInfoRequest extends SearchRequest<GetDebtInfoResponse> {

    public GetDebtInfoRequest() {
        super(new GetDebtInfoResponse());
    }

    @Override
    protected void addResponseBody(Signature signature) throws FlexPayException {

        if (response.getServiceDetailses() != null) {
            for (ServiceDetails serviceDetails : response.getServiceDetailses()) {
                sResponse.append("<serviceDetails>");
                addFieldToResponse(signature, "serviceId", serviceDetails.getServiceId());
                addFieldToResponse(signature, "serviceName", serviceDetails.getServiceName());
                addFieldToResponse(signature, "amount", serviceDetails.getAmount());
                addFieldToResponse(signature, "serviceProviderAccount", serviceDetails.getServiceProviderAccount());
                addFieldToResponse(signature, "personFirstName", serviceDetails.getPersonFirstName());
                addFieldToResponse(signature, "personMiddleName", serviceDetails.getPersonMiddleName());
                addFieldToResponse(signature, "personLastName", serviceDetails.getPersonLastName());
                addFieldToResponse(signature, "townName", serviceDetails.getTownName());
                addFieldToResponse(signature, "townType", serviceDetails.getTownType());
                addFieldToResponse(signature, "streetName", serviceDetails.getStreetName());
                addFieldToResponse(signature, "streetType", serviceDetails.getStreetType());
                addFieldToResponse(signature, "buildingNumber", serviceDetails.getBuildingNumber());
                addFieldToResponse(signature, "buildingBulk", serviceDetails.getBuildingBulk());
                addFieldToResponse(signature, "apartmentNumber", serviceDetails.getApartmentNumber());
                addFieldToResponse(signature, "roomNumber", serviceDetails.getRoomNumber());
                sResponse.append("</serviceDetails>");
            }
        }

    }

    public static void configParser(@NotNull Digester digester) {
        digester.addObjectCreate("request/getDebtInfo", GetDebtInfoRequest.class);
        digester.addSetNext("request/getDebtInfo", "setRequest");
        digester.addBeanPropertySetter("request/getDebtInfo/requestId", "requestId");
        digester.addBeanPropertySetter("request/getDebtInfo/searchType", "searchTypeString");
        digester.addBeanPropertySetter("request/getDebtInfo/searchCriteria", "searchCriteria");
    }

    @Override
    public void copyResponse(GetDebtInfoResponse res) {
        response.setStatus(res.getStatus());
        response.setServiceDetailses(res.getServiceDetailses());
    }

}
