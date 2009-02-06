package org.flexpay.tc.actions.buildingattributes;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.DateUtil;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.bti.persistence.*;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.bti.service.BuildingAttributeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;

public class BuildingAttributesEditAction extends FPActionSupport {

    private BuildingAddress building = new BuildingAddress();
    private List<BuildingAddress> alternateBuildingsList = new ArrayList<BuildingAddress>();
    private Date attributeDate = DateUtil.now();
    private Map<BuildingAttributeType, String> attributeMap = new HashMap<BuildingAttributeType, String>();

    private AddressService addressService;
    private BuildingService buildingService;
    private BuildingAttributeService buildingAttributeService;
    private BuildingAttributeTypeService buildingAttributeTypeService;

    @NotNull
    protected String doExecute() throws Exception {

        loadBuildingAttributes();

        if (!isSubmit()) {

            return INPUT;
        }

        log.debug("Submit!");
        updateBuildingAttirbutes();
        
        return REDIRECT_SUCCESS;

    }

    private void loadBuildingAttributes() throws FlexPayException {

        building = buildingService.readFull(stub(building));

        // alternatuve addresses loading
        for (BuildingAddress address : buildingService.getBuildingBuildings(building.getBuildingStub())) {
            if (!building.equals(address)) {
                alternateBuildingsList.add(buildingService.readFull(stub(address)));
            }
        }

        // loading bti building and it's attributes
        BtiBuilding btiBuilding = (BtiBuilding) buildingService.findBuilding(stub(building));
        List<BuildingAttributeBase> attrs = buildingAttributeService.listAttributes(stub(btiBuilding));

        for (BuildingAttributeBase attr : attrs) {
            BuildingAttributeType type = buildingAttributeTypeService.readFull(attr.getAttributeTypeStub());
            attributeMap.put(type, attr.getValueForDate(attributeDate));
        }
    }

    private void updateBuildingAttirbutes() {

        building = buildingService.readFull(stub(building));
        BtiBuilding btiBuilding = (BtiBuilding) buildingService.findBuilding(stub(building));

        for (BuildingAttributeType type : attributeMap.keySet()) {

            if (type instanceof BuildingAttributeTypeSimple) {

                log.debug("Simple");
            }

            if (type instanceof BuildingAttributeTypeEnum) {

                log.debug("Enum");
            }

        }
    }

    public String getAttributeTypeName(BuildingAttributeType type) {
        return getTranslation(type.getTranslations()).getName();
    }

    public boolean isBuildingAttributeTypeSimple(BuildingAttributeType type) {
        return type instanceof BuildingAttributeTypeSimple;
    }

    public boolean isBuildingAttributeTypeEnum(BuildingAttributeType type) {
        return type instanceof BuildingAttributeTypeEnum;
    }

    @NotNull
    protected String getErrorResult() {
        return INPUT;
    }

    /**
     * Returns building primary address by id
     *
     * @param buildingId building identifier
     * @return building primary address by id
     * @throws Exception in case of service error
     */
    public String getAddress(@NotNull Long buildingId) throws Exception {
        return addressService.getBuildingsAddress(new Stub<BuildingAddress>(buildingId), getUserPreferences().getLocale());
    }

    public BuildingAddress getBuilding() {
        return building;
    }

    public void setBuilding(BuildingAddress building) {
        this.building = building;
    }

    public List<BuildingAddress> getAlternateBuildingsList() {
        return alternateBuildingsList;
    }

    public void setAlternateBuildingsList(List<BuildingAddress> alternateBuildingsList) {
        this.alternateBuildingsList = alternateBuildingsList;
    }

    public String getAttributeDate() {
        return format(attributeDate);
    }

    public void setAttributeDate(String attributeDate) {
        this.attributeDate = DateUtil.parseBeginDate(attributeDate);
    }

    public Map<BuildingAttributeType, String> getAttributeMap() {
        return attributeMap;
    }

    public void setAttributeMap(Map<BuildingAttributeType, String> attributeMap) {
        this.attributeMap = attributeMap;
    }

    @Required
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @Required
    public void setBuildingService(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @Required
    public void setBuildingAttributeTypeService(BuildingAttributeTypeService buildingAttributeTypeService) {
        this.buildingAttributeTypeService = buildingAttributeTypeService;
    }

    @Required
    public void setBuildingAttributeService(BuildingAttributeService buildingAttributeService) {
        this.buildingAttributeService = buildingAttributeService;
    }
}
