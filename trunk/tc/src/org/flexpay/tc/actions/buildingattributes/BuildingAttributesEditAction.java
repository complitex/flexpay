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
import org.flexpay.bti.service.BuildingAttributeGroupService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class BuildingAttributesEditAction extends FPActionSupport {

    private BuildingAddress building = new BuildingAddress();
    private List<BuildingAddress> alternateAddresses = CollectionUtils.list();
    private Date attributeDate = DateUtil.now();
    private Map<Long, String> attributeMap = new HashMap<Long, String>();
    private Map<String, Map<Long, String>> attributeGroups = new HashMap<String, Map<Long, String>>();

    private AddressService addressService;
    private BuildingService buildingService;
    private BtiBuildingService btiBuildingService;
    private BuildingAttributeTypeService buildingAttributeTypeService;
    private BuildingAttributeGroupService buildingAttributeGroupService;

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

        Long buildingId = building.getId();

        building = buildingService.readFull(stub(building));
        if (building == null) {
            throw new FlexPayException("No building with id " + buildingId + " can be retrieved");
        }

        // alternative addresses loading
        loadAlternativeAddresses();

        // loading bti building and it's attributes
        BtiBuilding btiBuilding = btiBuildingService.readWithAttributesByAddress(stub(building));
        if (btiBuilding == null) {
            throw new FlexPayException("No bti building with id " + building.getId() + " can be retrieved");
        }

        List<BuildingAttributeType> attributeTypes = buildingAttributeTypeService.listTypes();
        for (BuildingAttributeType type : attributeTypes) {

            BuildingAttributeBase attribute = btiBuilding.getAttribute(type);

            putAttribute(type, attribute);
        }
    }

    private void putAttribute(BuildingAttributeType type, BuildingAttributeBase attribute) throws FlexPayException {
        BuildingAttributeGroup group = buildingAttributeGroupService.readFull(stub(type.getGroup()));

        String groupName = getAttributeGroupName(group);
        if (group == null) {
            throw new FlexPayException("No group was found with name " + groupName);
        }

        // put attribute into proper group
        Map<Long, String> attributeGroup = attributeGroups.get(groupName);
        if (attributeGroup == null) {
            attributeGroup = new HashMap<Long, String>();
            attributeGroups.put(groupName, attributeGroup);
        }

        if (attribute != null) {
            attributeGroup.put(type.getId(), attribute.getValueForDate(attributeDate));
            attributeMap.put(type.getId(), attribute.getValueForDate(attributeDate));
        } else {
            attributeGroup.put(type.getId(), "");
            attributeMap.put(type.getId(), "");
        }
    }

    private void loadAlternativeAddresses() throws FlexPayException {
        for (BuildingAddress address : buildingService.getBuildingBuildings(building.getBuildingStub())) {
            if (!building.equals(address)) {
                alternateAddresses.add(buildingService.readFull(stub(address)));
            }
        }
    }

    private void updateBuildingAttributes() throws FlexPayException {

        BtiBuilding btiBuilding = btiBuildingService.readWithAttributesByAddress(stub(building));
        if (btiBuilding == null) {
            throw new FlexPayException("No bti building with id " + building.getId() + " can be retrieved");
        }

        for (Long id : attributeMap.keySet()) {

            String value = attributeMap.get(id);
            BuildingAttributeType type = getAttributeTypeById(id);

            if (StringUtils.isEmpty(value)) {
                btiBuilding.removeAttribute(type);
                continue;
            }

            BuildingAttributeBase attribute = btiBuilding.getAttribute(type);

            if (attribute != null) {
                attribute.setValueForDate(value, attributeDate);
            } else {
                attribute = new BuildingTempAttribute();
                attribute.setAttributeType(type);
                attribute.setValueForDate(value, attributeDate);
            }

            btiBuilding.setAttribute(attribute);
        }

        btiBuildingService.updateAttributes(btiBuilding);
    }

    public String getAttributeTypeName(Long id) {

        BuildingAttributeType type = getAttributeTypeById(id);
        return getTranslation(type.getTranslations()).getName();
    }

    public String getAttributeGroupName(BuildingAttributeGroup group) {

        return getTranslation(group.getTranslations()).getName();
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

    public Map<String, Map<Long, String>> getAttributeGroups() {
        return attributeGroups;
    }

    public void setAttributeGroups(Map<String, Map<Long, String>> attributeGroups) {
        this.attributeGroups = attributeGroups;
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

    @Required
    public void setBuildingAttributeGroupService(BuildingAttributeGroupService buildingAttributeGroupService) {
        this.buildingAttributeGroupService = buildingAttributeGroupService;
    }
}
