package org.flexpay.httptester.outerrequest.request;

import org.apache.commons.digester3.Digester;
import org.flexpay.httptester.outerrequest.request.response.GetQuittanceDebtInfoResponse;
import org.flexpay.httptester.outerrequest.request.response.data.ServiceDetails;

import java.security.Signature;
import java.util.Properties;

public class GetQuittanceDebtInfoRequest extends Request<GetQuittanceDebtInfoResponse> {

    static {
        paramNames.add("requestId");
        paramNames.add("searchType");
        paramNames.add("searchCriteria");
    }

    public GetQuittanceDebtInfoRequest(Properties props) {
        super(props);
    }

    @Override
    protected void initResponseParser() {
        parser = new Digester();

        parser.addObjectCreate("response", GetQuittanceDebtInfoResponse.class);
        parser.addBeanPropertySetter("response/quittanceDebtInfo/requestId", "requestId");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/statusCode", "statusCode");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/statusMessage", "statusMessage");

        parser.addObjectCreate("response/quittanceDebtInfo/quittanceInfo", GetQuittanceDebtInfoResponse.QuittanceInfo.class);
        parser.addSetNext("response/quittanceDebtInfo/quittanceInfo", "addQuittanceInfo");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/quittanceNumber", "quittanceNumber");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/accountNumber", "accountNumber");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/creationDate", "creationDate");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/personFirstName", "personFirstName");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/personMiddleName", "personMiddleName");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/personLastName", "personLastName");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/country", "country");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/region", "region");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/townName", "townName");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/townType", "townType");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/streetName", "streetName");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/streetType", "streetType");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/buildingNumber", "buildingNumber");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/buildingBulk", "buildingBulk");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/apartmentNumber", "apartmentNumber");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/totalToPay", "totalToPay");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/totalPayed", "totalPayed");

        parser.addObjectCreate("response/quittanceDebtInfo/quittanceInfo/serviceDetails", ServiceDetails.class);
        parser.addSetNext("response/quittanceDebtInfo/quittanceInfo/serviceDetails", "addServiceDetails");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceId", "serviceId");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceName", "serviceName");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/incomingBalance", "incomingBalance");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/outgoingBalance", "outgoingBalance");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/amount", "amount");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/expence", "expence");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/rate", "rate");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/recalculation", "recalculation");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/benifit", "benifit");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/subsidy", "subsidy");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/payment", "payment");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/payed", "payed");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceProviderAccount", "serviceProviderAccount");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/personFirstName", "personFirstName");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/personMiddleName", "personMiddleName");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/personLastName", "personLastName");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/country", "country");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/region", "region");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/townName", "townName");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/townType", "townType");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/streetName", "streetName");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/streetType", "streetType");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/buildingNumber", "buildingNumber");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/buildingBulk", "buildingBulk");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/apartmentNumber", "apartmentNumber");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/roomNumber", "roomNumber");

        parser.addObjectCreate("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceAttribute", ServiceDetails.ServiceAttribute.class);
        parser.addSetNext("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceAttribute", "addAttribute");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceAttribute/name", "name");
        parser.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceAttribute/value", "value");

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

        for (GetQuittanceDebtInfoResponse.QuittanceInfo quittanceInfo : response.getQuittanceInfos()) {

            updateSignature(signature, quittanceInfo.getQuittanceNumber());
            updateSignature(signature, quittanceInfo.getAccountNumber());
            updateSignature(signature, quittanceInfo.getCreationDate());
            updateSignature(signature, quittanceInfo.getPersonFirstName());
            updateSignature(signature, quittanceInfo.getPersonMiddleName());
            updateSignature(signature, quittanceInfo.getPersonLastName());
            updateSignature(signature, quittanceInfo.getCountry());
            updateSignature(signature, quittanceInfo.getRegion());
            updateSignature(signature, quittanceInfo.getTownName());
            updateSignature(signature, quittanceInfo.getTownType());
            updateSignature(signature, quittanceInfo.getStreetName());
            updateSignature(signature, quittanceInfo.getStreetType());
            updateSignature(signature, quittanceInfo.getBuildingNumber());
            updateSignature(signature, quittanceInfo.getBuildingBulk());
            updateSignature(signature, quittanceInfo.getApartmentNumber());
            updateSignature(signature, quittanceInfo.getTotalToPay());
            updateSignature(signature, quittanceInfo.getTotalPayed());

            for (ServiceDetails serviceDetails : quittanceInfo.getServiceDetailses()) {

                updateSignature(signature, serviceDetails.getServiceId());
                updateSignature(signature, serviceDetails.getServiceName());
                updateSignature(signature, serviceDetails.getIncomingBalance());
                updateSignature(signature, serviceDetails.getOutgoingBalance());
                updateSignature(signature, serviceDetails.getAmount());
                updateSignature(signature, serviceDetails.getExpence());
                updateSignature(signature, serviceDetails.getRate());
                updateSignature(signature, serviceDetails.getRecalculation());
                updateSignature(signature, serviceDetails.getBenifit());
                updateSignature(signature, serviceDetails.getSubsidy());
                updateSignature(signature, serviceDetails.getPayment());
                updateSignature(signature, serviceDetails.getPayed());
                updateSignature(signature, serviceDetails.getServiceProviderAccount());
                updateSignature(signature, serviceDetails.getPersonFirstName());
                updateSignature(signature, serviceDetails.getPersonMiddleName());
                updateSignature(signature, serviceDetails.getPersonLastName());
                updateSignature(signature, serviceDetails.getCountry());
                updateSignature(signature, serviceDetails.getRegion());
                updateSignature(signature, serviceDetails.getTownName());
                updateSignature(signature, serviceDetails.getTownType());
                updateSignature(signature, serviceDetails.getStreetName());
                updateSignature(signature, serviceDetails.getStreetType());
                updateSignature(signature, serviceDetails.getBuildingNumber());
                updateSignature(signature, serviceDetails.getBuildingBulk());
                updateSignature(signature, serviceDetails.getApartmentNumber());
                updateSignature(signature, serviceDetails.getRoomNumber());

                for (ServiceDetails.ServiceAttribute serviceAttribute : serviceDetails.getAttributes()) {
                    updateSignature(signature, serviceAttribute.getName());
                    updateSignature(signature, serviceAttribute.getValue());
                }
            }
        }
    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "request_get_quittance_debt_info.file";
    }


}
