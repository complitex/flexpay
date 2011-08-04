package org.flexpay.mule.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;

import java.util.Set;

import static org.flexpay.ab.util.config.ApplicationConfig.*;
import static org.flexpay.common.util.CollectionUtils.set;

public class MuleBuildingAddress extends MuleIdObject {

    private Long buildingId;
    private Long streetId;
    private Boolean primary;
    private Set<MuleAddressAttribute> attributes = set();

    public BuildingAddress convert(BuildingAddress address, AddressAttributeTypeService addressAttributeTypeService) throws FlexPayException {

        for (MuleAddressAttribute attr : attributes) {
            AddressAttributeType type = addressAttributeTypeService.readFull(new Stub<AddressAttributeType>(attr.getId()));
            address.setBuildingAttribute(attr.getValue(), type);
        }

        address.setPrimaryStatus(primary);
        address.setStreet(new Street(streetId));

        return address;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(Long streetId) {
        this.streetId = streetId;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public Set<MuleAddressAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<MuleAddressAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("ids", ids).
                append("buildingId", buildingId).
                append("streetId", streetId).
                append("primary", primary).
                append("attributes", attributes).
                toString();
    }
}
