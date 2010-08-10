package org.flexpay.payments.actions.outerrequest.request;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.actions.outerrequest.request.response.GetRegistryListResponse;
import org.flexpay.payments.actions.outerrequest.request.response.data.RegistryInfo;
import org.jetbrains.annotations.NotNull;

import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetRegistryListRequest extends Request<GetRegistryListResponse> {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    private Date periodBeginDate;
    private Date periodEndDate;
    private Integer registryType;

    public GetRegistryListRequest() {
        super(new GetRegistryListResponse());
    }

    @Override
    protected void addResponseBody(Signature signature) throws FlexPayException {

        if (response.getRegistryInfos() != null) {
            for (RegistryInfo registryInfo : response.getRegistryInfos()) {
                sResponse.append("<registryInfo>");
                addFieldToResponse(signature, "registryId", registryInfo.getRegistryId());
                addFieldToResponse(signature, "registryDate", DATE_FORMAT.format(registryInfo.getRegistryDate()));
                addFieldToResponse(signature, "registryType", registryInfo.getRegistryType());
                addFieldToResponse(signature, "recordsCount", registryInfo.getRecordsCount());
                addFieldToResponse(signature, "totalSum", registryInfo.getTotalSum());
                addFieldToResponse(signature, "registryComment", registryInfo.getRegistryComment());
                sResponse.append("</registryInfo>");
            }
        }

    }

    @Override
    public void updateRequestSignature(Signature signature) throws Exception {
        updateSignature(signature, getPeriodBeginDate());
        updateSignature(signature, getPeriodEndDate());
        updateSignature(signature, registryType == null ? "" : registryType.toString());
    }

    @Override
    public boolean validate() {
        return true;
    }

    public static void configParser(@NotNull Digester digester) {
        digester.addObjectCreate("request/getRegistryList", GetRegistryListRequest.class);
        digester.addSetNext("request/getRegistryList", "setRequest");
        digester.addBeanPropertySetter("request/getRegistryList/requestId", "requestId");
        digester.addBeanPropertySetter("request/getRegistryList/periodBeginDate", "periodBeginDate");
        digester.addBeanPropertySetter("request/getRegistryList/periodEndDate", "periodEndDate");
        digester.addBeanPropertySetter("request/getRegistryList/registryType", "registryType");
    }

    @Override
    public void copyResponse(GetRegistryListResponse res) {
        response.setStatus(res.getStatus());
        response.setRegistryInfos(res.getRegistryInfos());
    }

    public String getPeriodBeginDate() {
        return DATE_FORMAT.format(periodBeginDate);
    }

    public void setPeriodBeginDate(String periodBeginDate) {
        try {
            this.periodBeginDate = DATE_FORMAT.parse(periodBeginDate);
        } catch (Exception e) {
            log.error("Can't parse request date parameter periodBeginDate - {}", periodBeginDate);
        }
    }

    public Date getPeriodBeginDateDate() {
        return periodBeginDate;
    }

    public String getPeriodEndDate() {
        return DATE_FORMAT.format(periodEndDate);
    }

    public void setPeriodEndDate(String periodEndDate) {
        try {
            this.periodEndDate = DATE_FORMAT.parse(periodEndDate);
        } catch (Exception e) {
            log.error("Can't parse request date parameter periodEndDate - {}", periodEndDate);
        }
    }

    public Date getPeriodEndDateDate() {
        return periodEndDate;
    }

    public Integer getRegistryType() {
        return registryType;
    }

    public void setRegistryType(Integer registryType) {
        this.registryType = registryType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("requestId", requestId).
                append("login", login).
                append("periodBeginDate", periodBeginDate).
                append("periodEndDate", periodEndDate).
                append("registryType", registryType).
                append("requestSignatureString", requestSignatureString).
                append("locale", locale).
                append("response", response).
                toString();
    }
}
