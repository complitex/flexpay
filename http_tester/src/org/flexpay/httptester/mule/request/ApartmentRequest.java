package org.flexpay.httptester.mule.request;

import java.util.Properties;

public class ApartmentRequest extends MuleRequest {

    static {
        paramNames.add("apartment.action");
        paramNames.add("apartment.id");
        paramNames.add("apartment.id1");
        paramNames.add("apartment.id2");
        paramNames.add("apartment.number");
        paramNames.add("apartment.buildingId");
    }

    public ApartmentRequest(Properties props) {
        super(props);
    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "mule.request_apartment.file";
    }
}
