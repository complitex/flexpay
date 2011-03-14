package org.flexpay.payments.action.outerrequest.request.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.action.outerrequest.request.response.data.RegistryInfo;

import java.io.Serializable;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class GetRegistryListResponse extends Response implements Serializable {

    public final static String TAG_NAME = "registryList";

    private List<RegistryInfo> registryInfos = list();

    @Override
    public String getTagName() {
        return TAG_NAME;
    }

    public List<RegistryInfo> getRegistryInfos() {
        return registryInfos;
    }

    public void setRegistryInfos(List<RegistryInfo> registryInfos) {
        this.registryInfos = registryInfos;
    }

    public void addRegistryInfo(RegistryInfo registryInfo) {
        if (registryInfos == null) {
            registryInfos = list();
        }
        registryInfos.add(registryInfo);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("status", status).
                append("registryInfos", registryInfos).
                toString();
    }
}
