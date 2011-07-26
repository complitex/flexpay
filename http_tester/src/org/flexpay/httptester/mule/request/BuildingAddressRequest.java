package org.flexpay.httptester.mule.request;

import java.util.Properties;

public class BuildingAddressRequest extends MuleRequest {

    static {
        paramNames.add("buildingAddress.action");
        paramNames.add("buildingAddress.id");
        paramNames.add("buildingAddress.id1");
        paramNames.add("buildingAddress.id2");
        paramNames.add("buildingAddress.buildingId");
        paramNames.add("buildingAddress.streetId");
        paramNames.add("buildingAddress.primary");
        paramNames.add("buildingAddress.attribute1.id");
        paramNames.add("buildingAddress.attribute1.value");
        paramNames.add("buildingAddress.attribute2.id");
        paramNames.add("buildingAddress.attribute2.value");
    }

    public BuildingAddressRequest(Properties props) {
        super(props);
    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "mule.request_buildingAddress.file";
    }
}
