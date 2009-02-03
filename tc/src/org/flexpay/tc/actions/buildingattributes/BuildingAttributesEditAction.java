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
import org.flexpay.bti.persistence.BuildingAttributeType;
import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.bti.persistence.BuildingAttributeBase;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.bti.service.BuildingAttributeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class BuildingAttributesEditAction extends FPActionSupport {

    private BuildingAddress building = new BuildingAddress();
    private List<BuildingAddress> alternateBuildingsList = new ArrayList<BuildingAddress>();
    private Date attributeDate = DateUtil.now();
    private Map<String, String> attributeMap = CollectionUtils.treeMap();

    private AddressService addressService;
    private BuildingService buildingService;
    private BuildingAttributeService buildingAttributeService;
    private BuildingAttributeTypeService buildingAttributeTypeService;

    @NotNull
    protected String doExecute() throws Exception {

        if (!isSubmit()) {
            loadBuildingWithAttributes();


        } else {

        }

        return INPUT;
    }

    private void loadBuildingWithAttributes() throws FlexPayException {

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
            log.debug(attr.getAttributeType().toString());

            String key = attr.getAttributeType().getI18nTitle();
            String value = attr.getValueForDate(attributeDate);

            attributeMap.put(key, value);

            // TODO remove
            log.debug("MapEntry [{}, {}]", key, value);
        }

//        for (BuildingAttributeType attributeType : buildingAttributeTypeService.listTypes()) {
//            BuildingAttributeBase attribute = btiBuilding.getAttribute(attributeType);
//
//            String value = "";
//            if (null != attribute) {
//                value = attribute.getValueForDate(attributeDate);
//            }
//
//            attributeMap.put(attributeType.getId(), value);
//

//        }
    }

    @NotNull
    protected String getErrorResult() {

        // TODO processing

        return INPUT;
    }

    /*
     public String getTypeName(Long typeId) throws FlexPayException {
		AddressAttributeType type = addressAttributeTypeService.read(new Stub<AddressAttributeType>(typeId));
		if (type == null) {
			throw new RuntimeException("Unknown type id: " + typeId);
		}
		return getTranslation(type.getTranslations()).getName();
	}
     */

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

    public Date getAttributeDate() {
        return attributeDate;
    }

    public void setAttributeDate(Date attributeDate) {
        this.attributeDate = attributeDate;
    }

    public Map<String, String> getAttributeMap() {
        return attributeMap;
    }

    public void setAttributeMap(Map<String, String> attributeMap) {
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
