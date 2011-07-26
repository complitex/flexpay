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

        String buildingNumber = "";
        String bulkNumber = "";
        String partNumber = "";

        for (MuleAddressAttribute attr : attributes) {
            AddressAttributeType type = addressAttributeTypeService.readFull(new Stub<AddressAttributeType>(attr.getId()));
            if (type == null) {
                throw new FlexPayException("Incorrect addressAttributeType id - " + attr.getId());
            } else if (type.isBuildingNumber()) {
                buildingNumber = attr.getValue();
            } else if (type.isBulkNumber()) {
                bulkNumber = attr.getValue();
            } else if (type.isPartNumber()) {
                partNumber = attr.getValue();
            }
        }

        address.setPrimaryStatus(primary);
        address.setStreet(new Street(streetId));
        address.setBuildingAttribute(buildingNumber, getBuildingAttributeTypeNumber());
        address.setBuildingAttribute(bulkNumber, getBuildingAttributeTypeBulk());
        address.setBuildingAttribute(partNumber, getBuildingAttributeTypePart());

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
