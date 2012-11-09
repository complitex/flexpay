package org.flexpay.httptester.outerrequest.request.response.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.flexpay.httptester.outerrequest.request.response.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Sknar
 *         Date: 08.11.12 14:28
 */
public class GetAddressMasterIndexResponse extends Response {

    private String addressInfoType;

    private List<AddressInfo> addressInfos = new ArrayList<AddressInfo>();

    public String getAddressInfoType() {
        return addressInfoType;
    }

    public void setAddressInfoType(String addressInfoType) {
        this.addressInfoType = addressInfoType;
    }

    public List<AddressInfo> getAddressInfos() {
        return addressInfos;
    }

    public void setAddressInfos(List<AddressInfo> addressInfos) {
        this.addressInfos = addressInfos;
    }

    public void addAddressInfo(AddressInfo addressInfo) {
        this.addressInfos.add(addressInfo);
    }

    public static class AddressInfo {

        private String masterIndex;

        private String data;

        public AddressInfo() {
        }

        public AddressInfo(String data, String masterIndex) {
            this.masterIndex = masterIndex;
            this.data = data;
        }

        public String getMasterIndex() {
            return masterIndex;
        }

        public void setMasterIndex(String masterIndex) {
            this.masterIndex = masterIndex;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                    append("masterIndex", masterIndex).
                    append("data", data).
                    toString();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("statusCode", statusCode).
                append("statusMessage", statusMessage).
                append("signature", signature).
                append("addressInfoType", addressInfoType).
                append("addressInfos", addressInfos).
                toString();
    }
}
