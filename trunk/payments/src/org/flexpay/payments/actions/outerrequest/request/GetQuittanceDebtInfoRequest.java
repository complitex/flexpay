package org.flexpay.payments.actions.outerrequest.request;

import org.apache.commons.digester.Digester;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.actions.outerrequest.request.response.GetQuittanceDebtInfoResponse;
import org.flexpay.payments.actions.outerrequest.request.response.data.QuittanceInfo;
import org.flexpay.payments.actions.outerrequest.request.response.data.ServiceDetails;
import org.jetbrains.annotations.NotNull;

import java.security.Signature;

public class GetQuittanceDebtInfoRequest extends SearchRequest<GetQuittanceDebtInfoResponse> {

    public GetQuittanceDebtInfoRequest() {
        super(new GetQuittanceDebtInfoResponse());
    }

    @Override
    protected void addResponseBody(Signature signature) throws FlexPayException {

        if (response.getInfos() != null) {
            for (QuittanceInfo quittanceInfo : response.getInfos()) {
                sResponse.append("<quittanceInfo>");
                addFieldToResponse(signature, "quittanceNumber", quittanceInfo.getQuittanceNumber());
                addFieldToResponse(signature, "accountNumber", quittanceInfo.getAccountNumber());
                addFieldToResponse(signature, "creationDate", quittanceInfo.getCreationDate());
                addFieldToResponse(signature, "personFirstName", quittanceInfo.getPersonFirstName());
                addFieldToResponse(signature, "personMiddleName", quittanceInfo.getPersonMiddleName());
                addFieldToResponse(signature, "personLastName", quittanceInfo.getPersonLastName());
                addFieldToResponse(signature, "country", quittanceInfo.getCountry());
                addFieldToResponse(signature, "region", quittanceInfo.getRegion());
                addFieldToResponse(signature, "townName", quittanceInfo.getTownName());
                //TODO: Сейчас в информаци о плательщике нет поля townType, поэтому оно всегда пустое
                addFieldToResponse(signature, "townType", quittanceInfo.getTownType());
                addFieldToResponse(signature, "streetName", quittanceInfo.getStreetName());
                addFieldToResponse(signature, "streetType", quittanceInfo.getStreetType());
                addFieldToResponse(signature, "buildingNumber", quittanceInfo.getBuildingNumber());
                addFieldToResponse(signature, "buildingBulk", quittanceInfo.getBuildingBulk());
                addFieldToResponse(signature, "apartmentNumber", quittanceInfo.getApartmentNumber());
                addFieldToResponse(signature, "totalToPay", quittanceInfo.getTotalToPay());
                addFieldToResponse(signature, "totalPayed", quittanceInfo.getTotalPayed());

                for (ServiceDetails serviceDetails : quittanceInfo.getServiceDetailses()) {
                    sResponse.append("<serviceDetails>");
                    addFieldToResponse(signature, "serviceId", serviceDetails.getServiceId());
                    addFieldToResponse(signature, "serviceName", serviceDetails.getServiceName());
                    addFieldToResponse(signature, "incomingBalance", serviceDetails.getIncomingBalance());
                    addFieldToResponse(signature, "outgoingBalance", serviceDetails.getOutgoingBalance());
                    addFieldToResponse(signature, "amount", serviceDetails.getAmount());
                    addFieldToResponse(signature, "expence", serviceDetails.getExpence());
                    addFieldToResponse(signature, "rate", serviceDetails.getRate());
                    addFieldToResponse(signature, "recalculation", serviceDetails.getRecalculation());
                    addFieldToResponse(signature, "benifit", serviceDetails.getBenifit());
                    addFieldToResponse(signature, "subsidy", serviceDetails.getSubsidy());
                    addFieldToResponse(signature, "payment", serviceDetails.getPayment());
                    addFieldToResponse(signature, "payed", serviceDetails.getPayed());
                    addFieldToResponse(signature, "serviceProviderAccount", serviceDetails.getServiceProviderAccount());
                    addFieldToResponse(signature, "personFirstName", serviceDetails.getPersonFirstName());
                    addFieldToResponse(signature, "personMiddleName", serviceDetails.getPersonMiddleName());
                    addFieldToResponse(signature, "personLastName", serviceDetails.getPersonLastName());
                    addFieldToResponse(signature, "country", serviceDetails.getCountry());
                    addFieldToResponse(signature, "region", serviceDetails.getRegion());
                    addFieldToResponse(signature, "townName", serviceDetails.getTownName());
                    //TODO: Сейчас в информаци о плательщике нет поля townType, поэтому оно всегда пустое
                    addFieldToResponse(signature, "townType", serviceDetails.getTownType());
                    addFieldToResponse(signature, "streetName", serviceDetails.getStreetName());
                    addFieldToResponse(signature, "streetType", serviceDetails.getStreetType());
                    addFieldToResponse(signature, "buildingNumber", serviceDetails.getBuildingNumber());
                    addFieldToResponse(signature, "buildingBulk", serviceDetails.getBuildingBulk());
                    addFieldToResponse(signature, "apartmentNumber", serviceDetails.getApartmentNumber());
                    addFieldToResponse(signature, "roomNumber", serviceDetails.getRoomNumber());

                    if (serviceDetails.getAttributes() != null) {
                        for (ServiceDetails.ServiceAttribute serviceAttribute : serviceDetails.getAttributes()) {
                            sResponse.append("<serviceAttribute>");
                            addFieldToResponse(signature, "name", serviceAttribute.getName());
                            addFieldToResponse(signature, "value", serviceAttribute.getValue());
                            sResponse.append("</serviceAttribute>");
                        }
                    }

                    sResponse.append("</serviceDetails>");
                }


                sResponse.append("</quittanceInfo>");
            }
        }

    }

    public static void configParser(@NotNull Digester digester) {
        digester.addObjectCreate("request/getQuittanceDebtInfo", GetQuittanceDebtInfoRequest.class);
        digester.addSetNext("request/getQuittanceDebtInfo", "setRequest");
        digester.addBeanPropertySetter("request/getQuittanceDebtInfo/requestId", "requestId");
        digester.addBeanPropertySetter("request/getQuittanceDebtInfo/searchType", "searchTypeString");
        digester.addBeanPropertySetter("request/getQuittanceDebtInfo/searchCriteria", "searchCriteria");
    }

    @Override
    public void copyResponse(GetQuittanceDebtInfoResponse res) {
        response.setStatus(res.getStatus());
        response.setInfos(res.getInfos());
    }

}
