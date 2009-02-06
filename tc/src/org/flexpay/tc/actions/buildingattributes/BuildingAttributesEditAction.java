package org.flexpay.tc.actions.buildingattributes;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.bti.persistence.*;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.bti.service.BtiBuildingService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;

public class BuildingAttributesEditAction extends FPActionSupport {

    private BuildingAddress building = new BuildingAddress();
    private List<BuildingAddress> alternateAddresses = CollectionUtils.list();
    private Date attributeDate = DateUtil.now();
    private Map<Long, String> attributeMap = new HashMap<Long, String>();

    private AddressService addressService;
    private BuildingService buildingService;
    private BtiBuildingService btiBuildingService;
    private BuildingAttributeTypeService buildingAttributeTypeService;

    @NotNull
    protected String doExecute() throws Exception {

        if (isNotSubmit()) {
            loadBuildingAttributes();
            return INPUT;
        } else {
            updateBuildingAttributes();
            return REDIRECT_SUCCESS;
        }
    }

    private void loadBuildingAttributes() throws FlexPayException {

        building = buildingService.readFull(stub(building));

        // alternatuve addresses loading
        for (BuildingAddress address : buildingService.getBuildingBuildings(building.getBuildingStub())) {
            if (!building.equals(address)) {
                alternateAddresses.add(buildingService.readFull(stub(address)));
            }
        }

        // loading bti building and it's attributes
        BtiBuilding btiBuilding = btiBuildingService.readWithAttributesByAddress(stub(building));

        //for (BuildingAttributeBase attr : attrs) {
        for (BuildingAttributeBase attr : btiBuilding.getAttributes()) {
            BuildingAttributeType type = buildingAttributeTypeService.readFull(attr.getAttributeTypeStub());
            attributeMap.put(type.getId(), attr.getValueForDate(attributeDate));
        }
    }

    private void updateBuildingAttributes() {

        BtiBuilding btiBuilding = btiBuildingService.readWithAttributesByAddress(stub(building));

        for (Long id : attributeMap.keySet()) {

            String value = attributeMap.get(id);

            BuildingAttributeType type = getAttributeTypeById(id);

            if (type instanceof BuildingAttributeTypeSimple) {
                BuildingAttribute newAttribute = new BuildingAttribute();
                newAttribute.setAttributeType(type);
                newAttribute.setValueForDate(value, attributeDate);

                btiBuilding.setAttribute(newAttribute);
            } else if (type instanceof BuildingAttributeTypeEnum) {

                // TODO implement
                log.debug(" !!!!! Enum attribute processing is still not implemented");
            }

        }

        btiBuildingService.updateAttributes(btiBuilding);
    }

    public String getAttributeTypeName(Long id) {
        BuildingAttributeType type = getAttributeTypeById(id);
        return getTranslation(type.getTranslations()).getName();
    }

    public boolean isBuildingAttributeTypeSimple(Long id) {

        return getAttributeTypeById(id) instanceof BuildingAttributeTypeSimple;
    }

    public boolean isBuildingAttributeTypeEnum(Long id) {

        return getAttributeTypeById(id) instanceof BuildingAttributeTypeEnum;
    }

    public SortedSet<BuildingAttributeTypeEnumValue> getTypeValues(Long id) {
        BuildingAttributeTypeEnum type = (BuildingAttributeTypeEnum) getAttributeTypeById(id);
        return type.getSortedValues();
    }

    // TODO move to proper class
    private BuildingAttributeType getAttributeTypeById(Long id) {
        return buildingAttributeTypeService.readFull(new Stub<BuildingAttributeType>(id));
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

    public List<BuildingAddress> getAlternateAddresses() {
        return alternateAddresses;
    }

    public void setAlternateAddresses(List<BuildingAddress> alternateAddresses) {
        this.alternateAddresses = alternateAddresses;
    }

    public String getAttributeDate() {
        return format(attributeDate);
    }

    public void setAttributeDate(String attributeDate) {
        this.attributeDate = DateUtil.parseBeginDate(attributeDate);
    }

    public Map<Long, String> getAttributeMap() {
        return attributeMap;
    }

    public void setAttributeMap(Map<Long, String> attributeMap) {
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
    public void setBtiBuildingService(BtiBuildingService btiBuildingService) {
        this.btiBuildingService = btiBuildingService;
    }

    @Required
    public void setBuildingAttributeTypeService(BuildingAttributeTypeService buildingAttributeTypeService) {
        this.buildingAttributeTypeService = buildingAttributeTypeService;
    }
}
