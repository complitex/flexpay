package org.flexpay.payments.action.outerrequest.request;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.StringUtil;
import org.flexpay.payments.action.outerrequest.request.response.GetAddressMasterIndexResponse;
import org.flexpay.payments.action.outerrequest.request.response.data.AddressInfo;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.security.Signature;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

/**
 * @author Pavel Sknar
 *         Date: 06.11.12 12:20
 */
public class GetAddressMasterIndexRequest extends Request<GetAddressMasterIndexResponse> {

    private String parentMasterIndex;
    private Integer parentAddressInfoType;
    private String parentAddressInfoTypeString;
    private String searchingData;

    public GetAddressMasterIndexRequest() {
        super(new GetAddressMasterIndexResponse());
    }

    @Override
    protected void addResponseBody(Signature signature) throws FlexPayException {

        if (response.getAddressInfos() != null) {
            sResponse.append("<addressInfo>");
            addFieldToResponse(signature, "addressInfoType", response.getAddressInfoType());
            for (AddressInfo addressInfo : response.getAddressInfos()) {
                sResponse.append("<item>");
                addFieldToResponse(signature, "masterIndex", addressInfo.getMasterIndex());
                addFieldToResponse(signature, "data", addressInfo.getData());
                sResponse.append("</item>");
            }
            sResponse.append("</addressInfo>");
        }

    }

    @Override
    public List<byte[]> getFieldsToSign() throws UnsupportedEncodingException {
        return list(getBytes(StringUtil.getString(parentMasterIndex)),
                getBytes(StringUtil.getString(parentAddressInfoTypeString)),
                getBytes(StringUtil.getString(searchingData)));
    }

    @Override
    public boolean validate() {
        return true;
    }

    public static void configParser(@NotNull Digester digester) {
        digester.addObjectCreate("request/getAddressMasterIndex", GetAddressMasterIndexRequest.class);
        digester.addSetNext("request/getAddressMasterIndex", "setRequest");
        digester.addBeanPropertySetter("request/getAddressMasterIndex/requestId", "requestId");
        digester.addBeanPropertySetter("request/getAddressMasterIndex/parentMasterIndex", "parentMasterIndex");
        digester.addBeanPropertySetter("request/getAddressMasterIndex/parentAddressInfoType", "parentAddressInfoType");
        digester.addBeanPropertySetter("request/getAddressMasterIndex/searchingData", "searchingData");
    }

    @Override
    public void copyResponse(GetAddressMasterIndexResponse res) {
        response.setStatus(res.getStatus());
        response.setAddressInfoType(res.getAddressInfoType());
        response.setAddressInfos(res.getAddressInfos());
    }

    public String getParentMasterIndex() {
        return parentMasterIndex;
    }

    public void setParentMasterIndex(String parentMasterIndex) {
        this.parentMasterIndex = parentMasterIndex;
    }

    public Integer getParentAddressInfoType() {
        return parentAddressInfoType;
    }

    public void setParentAddressInfoType(@NotNull Integer parentAddressInfoType) {
        this.parentAddressInfoType = parentAddressInfoType;
        this.parentAddressInfoTypeString = Integer.toString(parentAddressInfoType);
    }

    public String getSearchingData() {
        return searchingData;
    }

    public void setSearchingData(String searchingData) {
        this.searchingData = searchingData;
    }

    public String getParentAddressInfoTypeString() {
        return parentAddressInfoTypeString;
    }

    public void setParentAddressInfoTypeString(String parentAddressInfoTypeString) {
        this.parentAddressInfoTypeString = parentAddressInfoTypeString;
        this.parentAddressInfoType = Integer.valueOf(parentAddressInfoTypeString);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("requestId", requestId).
                append("login", login).
                append("parentMasterIndex", parentMasterIndex).
                append("parentAddressInfoType", parentAddressInfoType).
                append("searchingData", searchingData).
                append("requestSignatureString", requestSignatureString).
                append("locale", locale).
                append("response", response).
                toString();
    }
}
