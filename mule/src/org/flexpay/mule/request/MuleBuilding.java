package org.flexpay.mule.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;

import java.util.Set;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.flexpay.ab.util.config.ApplicationConfig.*;
import static org.flexpay.common.util.CollectionUtils.set;

public class MuleBuilding extends MuleIdObject {

    public final String BUILDINGS_SEPARATOR = ",";
    public final String INTERVAL_SEPARATOR = "\\.\\.";

    private Long districtId;
    private MuleBuildingAddress buildingAddress;

    public Set<Building> convert(AddressAttributeTypeService addressAttributeTypeService) throws FlexPayException {

        String buildingNumber = "";
        String bulkNumber = "";
        String partNumber = "";

        for (MuleAddressAttribute attr : buildingAddress.getAttributes()) {
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

        String[] bIntervals = buildingNumber.contains(BUILDINGS_SEPARATOR) ? buildingNumber.trim().split(BUILDINGS_SEPARATOR) : new String[] {buildingNumber.trim()};

        Set<Building> buildings = set();

        for (String interval : bIntervals) {

            String[] bValues = interval.contains("..") ? interval.trim().split(INTERVAL_SEPARATOR) : new String[] {interval.trim()};

            if (bValues.length == 1) {
                Building building = createBuilding(createAddress(bValues[0], bulkNumber, partNumber));
                if (building != null) {
                    buildings.add(building);
                }
            } else if (bValues.length == 2) {

                int start = Integer.parseInt(bValues[0].trim());
                int finish = Integer.parseInt(bValues[1].trim());

                for (int i = start; i <= finish; i++) {
                    Building building = createBuilding(createAddress(i + "", bulkNumber, partNumber));
                    if (building != null) {
                        buildings.add(building);
                    }
                }
            }
        }

        return buildings;
    }

    private Building createBuilding(BuildingAddress address) {
        if (address == null) {
            return null;
        }
        Building building = Building.newInstance();
        building.setDistrict(new District(districtId));
        building.addAddress(address);
        return building;
    }

    private BuildingAddress createAddress(String buildingNumber, String bulkNumber, String partNumber) {

        if (isEmpty(buildingNumber) && isEmpty(bulkNumber) && isEmpty(partNumber)) {
            return null;
        }

        BuildingAddress address = new BuildingAddress();
        address.setPrimaryStatus(true);
        address.setStreet(new Street(buildingAddress.getStreetId()));
        address.setBuildingAttribute(buildingNumber, getBuildingAttributeTypeNumber());
        address.setBuildingAttribute(bulkNumber, getBuildingAttributeTypeBulk());
        address.setBuildingAttribute(partNumber, getBuildingAttributeTypePart());

        return address;
    }

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
