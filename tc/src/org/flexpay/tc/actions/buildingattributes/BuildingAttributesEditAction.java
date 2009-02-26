package org.flexpay.tc.actions.buildingattributes;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.bti.persistence.*;
import org.flexpay.bti.service.BtiBuildingService;
import org.flexpay.bti.service.BuildingAttributeGroupService;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.tc.persistence.Tariff;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.*;

// TODO the class is too big. Need to split it down into workers (delegates)

/**
 * Action for updating building attributes
 */
public class BuildingAttributesEditAction extends FPActionSupport {

    // building primary and alternative addresses
    private BuildingAddress building = new BuildingAddress();
    private List<BuildingAddress> alternateAddresses = CollectionUtils.list();

    // for this date attribute values are loaded/saved (current date by default)
    private Date attributeDate = DateUtil.now();

    // This map (Attribute type id -> Attribute value) is used for saving attributes and is bound to UI form
    private Map<Long, String> attributeMap = CollectionUtils.map();

    // This list contains idattribute group identifiers
    private List<Long> attributeGroups = CollectionUtils.list();

    // date submission button
    private String dateSubmitted;

    // This list contains dates of tariff calculation
    private List<String> tariffCalculationDates = CollectionUtils.list();

    // This map (tariff calculation date -> map (tariff_name -> tariff value)
    // FIXME eliminate map-o-map ;)
    private Map<String, Map<Long, BigDecimal>> tcResultsMap = CollectionUtils.treeMap();

    // required services
    private AddressService addressService;
    private BuildingService buildingService;
    private BtiBuildingService btiBuildingService;
    private BuildingAttributeTypeService buildingAttributeTypeService;
    private BuildingAttributeGroupService buildingAttributeGroupService;
    private TariffCalculationResultService tariffCalculationResultService;
    private TariffService tariffService;

    @NotNull
    protected String doExecute() throws Exception {

        if (isSubmit()) {
            updateBuildingAttributes();
            return REDIRECT_SUCCESS;
        }

        if (isDateSubmit()) {
            // load attributes for date
            loadBuildingAttributes();
            loadTariffCalculationResults();
            return INPUT;
        }

        loadBuildingAttributes();
        loadTariffCalculationResults();
        return INPUT;
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
        if (group == null) {
            throw new FlexPayException("No group was found with name " + type.getGroup().getId());
        }

        // put attribute into proper group
        Long groupId = group.getId();
        if (!attributeGroups.contains(groupId)) {
            attributeGroups.add(groupId);
        }

        if (attribute != null) {
            attributeMap.put(type.getId(), attribute.getValueForDate(attributeDate));
        } else {
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

        for (Long typeId : attributeMap.keySet()) {

            String value = attributeMap.get(typeId);
            BuildingAttributeType type = getAttributeTypeById(typeId);

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

    private void loadTariffCalculationResults() {
        List<Date> calculationDates = tariffCalculationResultService.getUniqueDates();

		Stub<BuildingAddress> buildingStub = stub(building);

        for (Date calculationDate : calculationDates) {

            String calcDateString = FastDateFormat.getInstance("dd.MM.yyyy").format(calculationDate);

            List<TariffCalculationResult> calculationResults = tariffCalculationResultService.
                    getTariffCalcResultsByCalcDateAndAddressId(calculationDate, buildingStub);

            Map<Long, BigDecimal> resultMap = CollectionUtils.treeMap();
            for (TariffCalculationResult result : calculationResults) {

                resultMap.put(result.getTariff().getId(), result.getValue());
            }

            if (resultMap.size() > 0) {
                tariffCalculationDates.add(calcDateString);
                tcResultsMap.put(calcDateString, resultMap);
            }
        }
    }

	public String formatDate(Date date){
		return DateUtil.format(date);
	}
    
    public String getAttributeTypeName(Long typeId) {
        BuildingAttributeType type = getAttributeTypeById(typeId);
        return getTranslation(type.getTranslations()).getName();
    }

    public String getGroupName(Long groupId) {
        BuildingAttributeGroup group = buildingAttributeGroupService.readFull(new Stub<BuildingAttributeGroup>(groupId));
        return getTranslation(group.getTranslations()).getName();
    }

    public boolean isBuildingAttributeTypeSimple(Long typeId) {

        return getAttributeTypeById(typeId) instanceof BuildingAttributeTypeSimple;
    }

    public boolean isBuildingAttributeTypeEnum(Long typeId) {

        return getAttributeTypeById(typeId) instanceof BuildingAttributeTypeEnum;
    }

    public SortedSet<BuildingAttributeTypeEnumValue> getTypeValues(Long typeId) {

        BuildingAttributeTypeEnum type = (BuildingAttributeTypeEnum) getAttributeTypeById(typeId);
        return type.getSortedValues();
    }

    private BuildingAttributeType getAttributeTypeById(Long typeId) {

        return buildingAttributeTypeService.readFull(new Stub<BuildingAttributeType>(typeId));
    }

    public Map<Long, String> getGroupAttributes(Long groupId) {
        Map<Long, String> resultMap = CollectionUtils.map();

        for (Long typeId : attributeMap.keySet()) {
            BuildingAttributeType type = getAttributeTypeById(typeId);

            if (type.getGroup().getId().equals(groupId)) {
                resultMap.put(typeId, attributeMap.get(typeId));
            }
        }

        return resultMap;
    }

    @NotNull
    protected String getErrorResult() {
        return INPUT;
    }

    public boolean isDateSubmit() {
        return dateSubmitted != null;
    }

    public boolean isTempAttribute(Long typeId) {
        BuildingAttributeType type = getAttributeTypeById(typeId);
        return type.isTemp();
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

    public List<Long> getAttributeGroups() {
        return attributeGroups;
    }

    public List<String> getTariffCalculationDates() {
        return tariffCalculationDates;
    }

    public Map<Long, BigDecimal> getTcResults(String calcDate) {
        return tcResultsMap.get(calcDate);
    }

    public String getTariffTranslation(Long tariffId) {
        Tariff tariff = tariffService.readFull(new Stub<Tariff>(tariffId));
        return getTranslation(tariff.getTranslations()).getName();
    }

    public boolean tariffCalculationDatesIsEmpty() {
        return tcResultsMap.isEmpty();
    }

    public List<Long> listTariffIds() {
        List<Tariff> tariffs = tariffService.listTariffs();

        if (tariffs.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> result = CollectionUtils.list();
        for (Tariff t : tariffService.listTariffs()) {
            result.add(t.getId());
        }

        return result;
    }

    public String formatDateWithUnderlines(Date date) {
        String string = formatDate(date);
        return string.replace("/", "_");
    }

    public BigDecimal getTotalTariff(String calcDate) {
        Map<Long, BigDecimal> tcResults = tcResultsMap.get(calcDate);

        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal tariff : tcResults.values()) {
            total = total.add(tariff);
        }

        return total;
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
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

    @Required
    public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
        this.tariffCalculationResultService = tariffCalculationResultService;
    }

    @Required
    public void setTariffService(TariffService tariffService) {
        this.tariffService = tariffService;
    }

}
