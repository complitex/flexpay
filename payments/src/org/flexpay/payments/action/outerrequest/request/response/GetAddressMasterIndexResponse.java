package org.flexpay.payments.action.outerrequest.request.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.action.outerrequest.request.response.data.AddressInfo;

import java.io.Serializable;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

/**
 * @author Pavel Sknar
 *         Date: 06.11.12 12:41
 */
public class GetAddressMasterIndexResponse extends Response implements Serializable {
    public final static String TAG_NAME = "addressMasterIndex";

    private Integer addressInfoType;

    private List<AddressInfo> addressInfos = list();

    @Override
    public String getTagName() {
        return TAG_NAME;
    }

    public Integer getAddressInfoType() {
        return addressInfoType;
    }

    public void setAddressInfoType(Integer addressInfoType) {
        this.addressInfoType = addressInfoType;
    }

    public List<AddressInfo> getAddressInfos() {
        return addressInfos;
    }

    public void setAddressInfos(List<AddressInfo> addressInfos) {
        this.addressInfos = addressInfos;
    }

    public void addRegistryInfo(AddressInfo addressInfo) {
        if (addressInfos == null) {
            addressInfos = list();
        }
        addressInfos.add(addressInfo);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("status", status).
                append("addressInfoType", addressInfoType).
                append("addressInfos", addressInfos).
                toString();
    }
}
