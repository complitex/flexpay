package org.flexpay.ab.action.building;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.flexpay.ab.util.config.ApplicationConfig.*;
import static org.flexpay.common.util.CollectionUtils.treeMap;

/**
 * Create building with a single required address
 */
public class BuildingCreateAction extends FPActionSupport {

    public final String BUILDINGS_SEPARATOR = ",";
    public final String INTERVAL_SEPARATOR = "\\.\\.";

    private Long streetFilter;
    private Long districtFilter;

    private Map<Long, String> attributesMap = treeMap();
    private Building building;

    private ObjectsFactory objectsFactory;
    private AddressAttributeTypeService addressAttributeTypeService;
    private BuildingService buildingService;
    private StreetService streetService;
    private DistrictService districtService;

    @NotNull
    @Override
    public String doExecute() throws Exception {

        correctAttributes();

        if (isNotSubmit()) {
            return INPUT;
        }

        if (!doValidate()) {
            return INPUT;
        }

        setBuildingAttributes();

        if (hasActionErrors()) {
            return INPUT;
        }

        addActionMessage(getText("ab.building.saved"));
        
        return REDIRECT_SUCCESS;
    }

    private void createBuilding(BuildingAddress address) throws FlexPayExceptionContainer {
        if (address == null) {
            log.debug("Address are null. Building not created");
            return;
        }
        Building building = objectsFactory.newBuilding();
        building.setDistrict(new District(districtFilter));
        building.addAddress(address);
        buildingService.create(building);
    }

    private BuildingAddress createAddress(String buildingNumber, String bulkNumber, String partNumber) throws FlexPayExceptionContainer {

        if (isEmpty(buildingNumber) && isEmpty(bulkNumber) && isEmpty(partNumber)) {
            log.debug("All attributes are empty. Building address not created.");
            return null;
        }

        BuildingAddress address = new BuildingAddress();
        address.setPrimaryStatus(true);
        address.setStreet(new Street(streetFilter));
        address.setBuildingAttribute(buildingNumber, getBuildingAttributeTypeNumber());
        address.setBuildingAttribute(bulkNumber, getBuildingAttributeTypeBulk());
        address.setBuildingAttribute(partNumber, getBuildingAttributeTypePart());

        return address;
    }

    @SuppressWarnings({"ObjectToString"})
    private void setBuildingAttributes() throws FlexPayExceptionContainer {

        log.debug("Building Attributes map = {}", attributesMap);

        String buildingNumber = "";
        String bulkNumber = "";
        String partNumber = "";

        for (Map.Entry<Long, String> attr : attributesMap.entrySet()) {
            AddressAttributeType type = addressAttributeTypeService.readFull(new Stub<AddressAttributeType>(attr.getKey()));
            if (type.isBuildingNumber()) {
                buildingNumber = attr.getValue();
            } else if (type.isBulkNumber()) {
                bulkNumber = attr.getValue();
            } else if (type.isPartNumber()) {
                partNumber = attr.getValue();
            }
        }

        String[] bIntervals = buildingNumber.contains(BUILDINGS_SEPARATOR) ? buildingNumber.trim().split(BUILDINGS_SEPARATOR) : new String[] {buildingNumber.trim()};
        log.debug("Building intervals = {}", bIntervals.toString());

        for (String interval : bIntervals) {

            String[] bValues = interval.contains("..") ? interval.trim().split(INTERVAL_SEPARATOR) : new String[] {interval.trim()};
            log.debug("Building values = {}", bValues.toString());

            if (bValues.length == 1) {
                log.debug("Creating building with number = {}, bulk = {}, part = {}", new Object[] {bValues[0], bulkNumber, partNumber});
                createBuilding(createAddress(bValues[0], bulkNumber, partNumber));
            } else if (bValues.length == 2) {

                int start;
                int finish;

                try {
                    start = Integer.parseInt(bValues[0].trim());
                } catch (NumberFormatException e) {
                    log.debug("Incorrect start value in building interval");
                    addActionError(getText("ab.error.building.incorrect_start_value_in_interval"));
                    return;
                }
                try {
                    finish = Integer.parseInt(bValues[1].trim());
                } catch (NumberFormatException e) {
                    log.debug("Incorrect finish value in building interval");
                    addActionError(getText("ab.error.building.incorrect_finish_value_in_interval"));
                    return;
                }

                if (start > finish) {
                    log.debug("Incorrect building interval: start value more than finish value");
                    addActionError(getText("ab.error.building.incorrect_start_value_more_than_finish_value"));
                    return;
                }

                for (int i = start; i <= finish; i++) {
                    log.debug("Creating building with number = {}, bulkNumber = {}, partNumber = {}", new Object[] {i, bulkNumber, partNumber});
                    createBuilding(createAddress(i + "", bulkNumber, partNumber));
                }
            }
        }
    }

    private boolean doValidate() {

        if (districtFilter == null || districtFilter <= 0) {
            log.warn("Incorrect district id in filter ({})", districtFilter);
            addActionError(getText("ab.error.district.incorrect_district_id"));
            districtFilter = 0L;
        } else {
            District district = districtService.readFull(new Stub<District>(districtFilter));
            if (district == null) {
                log.warn("Can't get district with id {} from DB", districtFilter);
                addActionError(getText("ab.error.district.cant_get_district"));
                districtFilter = 0L;
            } else if (district.isNotActive()) {
                log.warn("District with id {} is disabled", districtFilter);
                addActionError(getText("ab.error.district.cant_get_district"));
                districtFilter = 0L;
            }
        }

        if (streetFilter == null || streetFilter <= 0) {
            log.warn("Incorrect street id in filter ({})", streetFilter);
            addActionError(getText("ab.error.street.incorrect_street_id"));
            streetFilter = 0L;
        } else {
            Street street = streetService.readFull(new Stub<Street>(streetFilter));
            if (street == null) {
                log.warn("Can't get street with id {} from DB", streetFilter);
                addActionError(getText("ab.error.street.cant_get_street"));
                streetFilter = 0L;
            } else if (street.isNotActive()) {
                log.warn("Street with id {} is disabled", streetFilter);
                addActionError(getText("ab.error.street.cant_get_street"));
                streetFilter = 0L;
            }
        }

        Long buildingNumberAttributeId = ApplicationConfig.getBuildingAttributeTypeNumber().getId();
        if (StringUtils.isEmpty(attributesMap.get(buildingNumberAttributeId))) {
            log.warn("Required building attribute not set");
            addActionError(getText("ab.error.building_address.building_number_required"));
        }

        return !hasActionErrors();
    }

    private void correctAttributes() {
        if (attributesMap == null) {
            log.warn("AttributesMap parameter is null");
            attributesMap = treeMap();
        }
        Map<Long, String> newAttributesMap = treeMap();
        for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
            newAttributesMap.put(type.getId(), attributesMap.containsKey(type.getId()) ? attributesMap.get(type.getId()) : "");
        }
        attributesMap = newAttributesMap;
    }

    /**
     * Get default error execution result
     * <p/>
     * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
     *
     * @return {@link #ERROR} by default
     */
    @NotNull
    @Override
    protected String getErrorResult() {
        return INPUT;
    }

    public String getTypeName(Long typeId) throws FlexPayException {
        AddressAttributeType type = getAddressAttributeTypeService().readFull(new Stub<AddressAttributeType>(typeId));
        if (type == null) {
            throw new RuntimeException("Unknown type id: " + typeId);
        }
        return getTranslationName(type.getTranslations());
    }

    public Long getStreetFilter() {
        return streetFilter;
    }

    public void setStreetFilter(Long streetFilter) {
        this.streetFilter = streetFilter;
    }

    public Long getDistrictFilter() {
        return districtFilter;
    }

    public void setDistrictFilter(Long districtFilter) {
        this.districtFilter = districtFilter;
    }

    public Map<Long, String> getAttributesMap() {
        return attributesMap;
    }

    public void setAttributesMap(Map<Long, String> attributesMap) {
        this.attributesMap = attributesMap;
    }

    public Building getBuilding() {
        return building;
    }

    @Required
    public void setBuildingService(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @Required
    public void setObjectsFactory(ObjectsFactory objectsFactory) {
        this.objectsFactory = objectsFactory;
    }

    public AddressAttributeTypeService getAddressAttributeTypeService() {
        return addressAttributeTypeService;
    }

    @Required
    public void setAddressAttributeTypeService(AddressAttributeTypeService addressAttributeTypeService) {
        this.addressAttributeTypeService = addressAttributeTypeService;
    }

    @Required
    public void setStreetService(StreetService streetService) {
        this.streetService = streetService;
    }

    @Required
    public void setDistrictService(DistrictService districtService) {
        this.districtService = districtService;
    }
}
