package org.flexpay.tc.actions.buildingattributes;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.bti.persistence.building.*;
import org.flexpay.bti.service.BtiBuildingService;
import org.flexpay.bti.service.BuildingAttributeGroupService;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

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
	private Map<Long, String> attributeMapDBValues = CollectionUtils.map();

	// This list contains idattribute group identifiers
	private List<Long> attributeGroups = CollectionUtils.list();

	// date submission button
	private String dateSubmitted;

	// required services
	private AddressService addressService;
	private BuildingService buildingService;
	private BtiBuildingService btiBuildingService;
	private BuildingAttributeTypeService buildingAttributeTypeService;
	private BuildingAttributeGroupService buildingAttributeGroupService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		loadAttributeTypes();

		if (isSubmit()) {
			if (!doValidate()) {
				return INPUT;
			}
			updateBuildingAttributes();
			return SUCCESS;
		}

		if (isDateSubmit()) {
			loadAttributeValues();
			return INPUT;
		}

		loadAttributeValues();
		return INPUT;
	}

	private void loadAttributeTypes() throws FlexPayException {
		loadBuildingWithAttributes();
	}

	private void loadAttributeValues() {

		for (Long typeId : attributeMapDBValues.keySet()) {
			attributeMap.put(typeId, attributeMapDBValues.get(typeId));
		}
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}


	// loading building attributes
	private void loadBuildingWithAttributes() throws FlexPayException {

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

			BuildingAttribute attribute = btiBuilding.getAttributeForDate(type, attributeDate);
			putAttribute(type, attribute);
		}
	}

	private void putAttribute(BuildingAttributeType type, BuildingAttribute attribute) throws FlexPayException {

		BuildingAttributeGroup group = buildingAttributeGroupService.readFull(stub(type.getGroup()));
		if (group == null) {
			throw new FlexPayException("No group was found with name " + type.getGroup().getId());
		}

		// put attribute into proper group
		if (!attributeGroups.contains(group.getId())) {
			attributeGroups.add(group.getId());
		}

		attributeMapDBValues.put(type.getId(), attribute != null ? attribute.getStringValue() : "");
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

			BuildingAttribute attribute = new BuildingAttribute();
			attribute.setAttributeType(type);
			attribute.setStringValue(value);
			btiBuilding.setTmpAttributeForDate(attribute, attributeDate);
		}

		btiBuildingService.updateAttributes(btiBuilding);
	}

	public boolean isDateSubmit() {
		return dateSubmitted != null;
	}

	public List<Long> getGroupAttributeTypes(Long groupId) {

		List<Long> result = CollectionUtils.list();

		for (Long typeId : attributeMapDBValues.keySet()) {
			BuildingAttributeType type = getAttributeTypeById(typeId);
			if (groupId.equals(type.getGroup().getId())) {
				result.add(typeId);
			}
		}

		return result;
	}

	public String getAttributeValue(Long typeId) {
		return attributeMap.get(typeId);
	}

	// rendering utility methods
	public boolean isTempAttribute(Long typeId) {
		BuildingAttributeType type = getAttributeTypeById(typeId);
		return type.isTemp();
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

	public String getAddress(@NotNull Long buildingId) throws Exception {
		return addressService.getBuildingsAddress(new Stub<BuildingAddress>(buildingId), getUserPreferences().getLocale());
	}

	public String formatDate(Date date) {
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

	// utility methods
	private BuildingAttributeType getAttributeTypeById(Long typeId) {

		return buildingAttributeTypeService.readFull(new Stub<BuildingAttributeType>(typeId));
	}

	private String getAttributeNewValueByTypeName(String uniqueCode) throws FlexPayException {

		BuildingAttributeType type = buildingAttributeTypeService.findTypeByName(uniqueCode);

		if (type == null) {
			throw new FlexPayException("Can't resolve building attribute type with unique code " + uniqueCode);
		}

		return attributeMap.get(type.getId());
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

	public void setDateSubmitted(String dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public boolean doValidate() {

		BuildingAttributesValidator validator = new BuildingAttributesValidator();

		try {
			// checking number of floors
			String floorsNumber = getAttributeNewValueByTypeName("ATTR_FLOORS_NUMBER");
			if (!validator.checkFloorsNumber(floorsNumber)) {
				addActionError(getText(validator.getErrorMessageCode()));
			}

			// checking porches
			String porchesNumber = getAttributeNewValueByTypeName("ATTR_DOORWAYS_NUMBER");
			if (!validator.checkPorchesNumber(porchesNumber)) {
				addActionError(getText(validator.getErrorMessageCode()));
			}

			// checking apartments
			String apartmentsNumber = getAttributeNewValueByTypeName("ATTR_APARTMENTS_NUMBER");
			if (!validator.checkApartmentsNumber(apartmentsNumber)) {
				addActionError(getText(validator.getErrorMessageCode()));
			}

			// checking year of building construction
			String constructionYear = getAttributeNewValueByTypeName("ATTR_BUILD_YEAR");
			if (!validator.checkConstructionYear(constructionYear)) {
				addActionError(getText(validator.getErrorMessageCode()));
			}

			// checking living area
			String livingArea = getAttributeNewValueByTypeName("ATTR_LIVE_SQUARE");
			if (!validator.checkArea(livingArea)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_living_square"));
			}

			// checking area values
			String softRoofSquare = getAttributeNewValueByTypeName("ATTR_SOFT_ROOF_SQUARE");
			if (!validator.checkArea(softRoofSquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_soft_roof_square"));
			}

			String hardSlateRoofSquare = getAttributeNewValueByTypeName("ATTR_HARD_SLATE_ROOF_SQUARE");
			if (!validator.checkArea(hardSlateRoofSquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_hard_slate_roof_square"));
			}

			String hardMetalRoofSquare = getAttributeNewValueByTypeName("ATTR_HARD_METAL_ROOF_SQUARE");
			if (!validator.checkArea(hardMetalRoofSquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_hard_metal_roof_square"));
			}

			String basementSquare = getAttributeNewValueByTypeName("ATTR_BASEMENT_SQUARE");
			if (!validator.checkArea(basementSquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_basement_square"));
			}

			String technicalFloorsSquare = getAttributeNewValueByTypeName("ATTR_TECHNICAL_FLOORS_SQUARE");
			if (!validator.checkArea(technicalFloorsSquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_technical_floor_square"));
			}

			String arretSquare = getAttributeNewValueByTypeName("ATTR_ARRET_SQUARE");
			if (!validator.checkArea(arretSquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_arret_square"));
			}

			String firstFloorTotalSquare = getAttributeNewValueByTypeName("ATTR_FIRST_FLOORS_TOTAL_SQUARE");
			if (!validator.checkArea(firstFloorTotalSquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_first_floor_total_square"));
			}

			String liftedApartmentsTotalSquare = getAttributeNewValueByTypeName("ATTR_LIFTED_APARTMENTS_TOTAL_SQUARE");
			if (!validator.checkArea(liftedApartmentsTotalSquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_lifted_apartments_total_square"));
			}

			String adsSuitedApartmentsTotalSquare = getAttributeNewValueByTypeName("ATTR_ADS_SUITED_APARTMENTS_TOTAL_SQUARE");
			if (!validator.checkArea(adsSuitedApartmentsTotalSquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_ads_suited_apartments_total_square"));
			}

			// checking near house territory area
			String nearHouseNonCategorySquare = getAttributeNewValueByTypeName("ATTR_NEAR_HOUSE_NONCATEGORY_TERRITORY_TOTAL_SQUARE");
			if (!validator.checkArea(nearHouseNonCategorySquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_near_house_non_category_square"));
			}

			String nearHouse1stCategorySquare = getAttributeNewValueByTypeName("ATTR_NEAR_HOUSE_1ST_CATEGORY_TERRITORY_TOTAL_SQUARE");
			if (!validator.checkArea(nearHouse1stCategorySquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_near_house_1st_category_square"));
			}

			String nearHouse2ndCategorySquare = getAttributeNewValueByTypeName("ATTR_NEAR_HOUSE_2ND_CATEGORY_TERRITORY_TOTAL_SQUARE");
			if (!validator.checkArea(nearHouse2ndCategorySquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_near_house_2nd_category_square"));
			}

			String nearHouse3rdCategorySquare = getAttributeNewValueByTypeName("ATTR_NEAR_HOUSE_3D_CATEGORY_TERRITORY_TOTAL_SQUARE");
			if (!validator.checkArea(nearHouse3rdCategorySquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_near_house_3rd_category_square"));
			}

			String nearHouseTotalSquare = getAttributeNewValueByTypeName("ATTR_NEAR_HOUSE_TERRITORY_TOTAL_SQUARE");
			if (!validator.checkArea(nearHouseTotalSquare)) {
				addActionError(getText("tc.errors.building_attributes.validation.invalid_near_house_total_square"));
			}

			// checking summ
			if (StringUtils.isNotEmpty(nearHouseNonCategorySquare)
				&& StringUtils.isNotEmpty(nearHouse1stCategorySquare)
				&& StringUtils.isNotEmpty(nearHouse2ndCategorySquare)
				&& StringUtils.isNotEmpty(nearHouse3rdCategorySquare)
				&& StringUtils.isNotEmpty(nearHouseTotalSquare)
					) {
				try {
					BigDecimal nonCategory = new BigDecimal(nearHouseNonCategorySquare);
					BigDecimal firstCategory = new BigDecimal(nearHouse1stCategorySquare);
					BigDecimal secondCategory = new BigDecimal(nearHouse2ndCategorySquare);
					BigDecimal thirdCategory = new BigDecimal(nearHouse3rdCategorySquare);
					BigDecimal userTotal = new BigDecimal(nearHouseTotalSquare);
					BigDecimal realTotal = BigDecimal.ZERO;
					realTotal = realTotal.add(nonCategory);
					realTotal = realTotal.add(firstCategory);
					realTotal = realTotal.add(secondCategory);
					realTotal = realTotal.add(thirdCategory);

					if (realTotal.compareTo(userTotal) != 0) {
						addActionError(getText("tc.errors.building_attributes.validation.invalid_near_house_total_square_bad_summ"));
					}
				} catch (NumberFormatException nfe) {
					log.info("Not all near house squares are valid. Total summ check is skipped.");
				}
			} else {
				log.info("Not all near house squares are present. Total summ check is skipped.");
			}
		} catch (FlexPayException e) {
			log.error("Building attributes validation error: " + e.getMessage(), e);
		}

		return !hasActionErrors();
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
