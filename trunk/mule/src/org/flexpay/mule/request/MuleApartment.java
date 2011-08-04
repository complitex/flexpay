package org.flexpay.mule.request;

import org.apache.commons.lang.builder.ToStringBuilder;

public class MuleApartment extends MuleIdObject {

    private String number;
    private Long buildingId;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("ids", ids).
                append("number", number).
                append("buildingId", buildingId).
                toString();
    }
}
