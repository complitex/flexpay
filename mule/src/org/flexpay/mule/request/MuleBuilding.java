package org.flexpay.mule.request;

import org.apache.commons.lang.builder.ToStringBuilder;

public class MuleBuilding extends MuleIdObject {

    private Long districtId;
    private MuleBuildingAddress buildingAddress;

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public MuleBuildingAddress getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(MuleBuildingAddress buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("ids", ids).
                append("districtId", districtId).
                append("buildingAddress", buildingAddress).
                toString();
    }
}
