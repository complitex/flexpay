package org.flexpay.httptester.mule.request;

import java.util.Properties;

public class BuildingRequest extends MuleRequest {

    static {
        paramNames.add("building.action");
        paramNames.add("building.id");
        paramNames.add("building.id1");
        paramNames.add("building.id2");
        paramNames.add("building.districtId");
        paramNames.add("building.buildingAddress.streetId");
        paramNames.add("building.buildingAddress.attribute1.id");
        paramNames.add("building.buildingAddress.attribute1.value");
        paramNames.add("building.buildingAddress.attribute2.id");
        paramNames.add("building.buildingAddress.attribute2.value");
    }

    public BuildingRequest(Properties props) {
        super(props);
    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "mule.request_building.file";
    }
}
