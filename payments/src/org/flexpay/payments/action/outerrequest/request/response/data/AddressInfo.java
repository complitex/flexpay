package org.flexpay.payments.action.outerrequest.request.response.data;

import java.io.Serializable;

/**
 * @author Pavel Sknar
 *         Date: 06.11.12 13:42
 */
public class AddressInfo implements Serializable {

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
}
