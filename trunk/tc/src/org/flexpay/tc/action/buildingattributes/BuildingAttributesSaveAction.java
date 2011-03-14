package org.flexpay.tc.action.buildingattributes;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.persistence.building.BuildingAttribute;
import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.bti.service.BtiBuildingService;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Action for updating building attributes
 */
public class BuildingAttributesSaveAction extends FPActionSupport {

	private BuildingAddress building = new BuildingAddress();
	private Date attributeDate = DateUtil.now();
	private Map<String, String> attrs = CollectionUtils.map();
	private Map<String, String> typesMap = CollectionUtils.map();

	private BtiBuildingService btiBuildingService;
	private BuildingAttributeTypeService buildingAttributeTypeService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		BtiBuilding btiBuilding = btiBuildingService.readWithAttributesByAddress(stub(building));
		if (btiBuilding == null) {
			throw new FlexPayException("No bti building with id " + building.getId() + " can be retrieved");
		}

		List<BuildingAttributeType> types = buildingAttributeTypeService.readFullAll();

		for (BuildingAttributeType type : types) {
			typesMap.put(type.getUniqueCode(), attrs.get("" + type.getId()));
		}

		if (!doValidate()) {
			return SUCCESS;
		}

		for (BuildingAttributeType type : types) {
			BuildingAttribute attribute = new BuildingAttribute();
			attribute.setAttributeType(type);
			attribute.setStringValue(attrs.get("" + type.getId()));
			btiBuilding.setTmpAttributeForDate(attribute, attributeDate);
		}

		btiBuildingService.updateAttributes(btiBuilding);

		addActionMessage(getText("common.changes_saved"));

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public boolean doValidate() {

		BuildingAttributesValidator validator = new BuildingAttributesValidator();

		if (!validator.checkFloorsNumber(typesMap.get("ATTR_FLOORS_NUMBER"))) {
			addActionError(getText(validator.getErrorMessageCode()));
		}

		if (!validator.checkPorchesNumber(typesMap.get("ATTR_DOORWAYS_NUMBER"))) {
			addActionError(getText(validator.getErrorMessageCode()));
		}

		if (!validator.checkApartmentsNumber(typesMap.get("ATTR_APARTMENTS_NUMBER"))) {
			addActionError(getText(validator.getErrorMessageCode()));
		}

		if (!validator.checkConstructionYear(typesMap.get("ATTR_BUILD_YEAR"))) {
			addActionError(getText(validator.getErrorMessageCode()));
		}

		if (!validator.checkArea(typesMap.get("ATTR_LIVE_SQUARE"))) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_living_square"));
		}

		if (!validator.checkArea(typesMap.get("ATTR_SOFT_ROOF_SQUARE"))) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_soft_roof_square"));
		}

		if (!validator.checkArea(typesMap.get("ATTR_HARD_SLATE_ROOF_SQUARE"))) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_hard_slate_roof_square"));
		}

		if (!validator.checkArea(typesMap.get("ATTR_HARD_METAL_ROOF_SQUARE"))) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_hard_metal_roof_square"));
		}

		if (!validator.checkArea(typesMap.get("ATTR_BASEMENT_SQUARE"))) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_basement_square"));
		}

		if (!validator.checkArea(typesMap.get("ATTR_TECHNICAL_FLOORS_SQUARE"))) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_technical_floor_square"));
		}

		if (!validator.checkArea(typesMap.get("ATTR_ARRET_SQUARE"))) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_arret_square"));
		}

		if (!validator.checkArea(typesMap.get("ATTR_FIRST_FLOORS_TOTAL_SQUARE"))) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_first_floor_total_square"));
		}

		if (!validator.checkArea(typesMap.get("ATTR_LIFTED_APARTMENTS_TOTAL_SQUARE"))) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_lifted_apartments_total_square"));
		}

		if (!validator.checkArea(typesMap.get("ATTR_ADS_SUITED_APARTMENTS_TOTAL_SQUARE"))) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_ads_suited_apartments_total_square"));
		}

		String nearHouseNonCategorySquare = typesMap.get("ATTR_NEAR_HOUSE_NONCATEGORY_TERRITORY_TOTAL_SQUARE");
		String nearHouse1stCategorySquare = typesMap.get("ATTR_NEAR_HOUSE_1ST_CATEGORY_TERRITORY_TOTAL_SQUARE");
		String nearHouse2ndCategorySquare = typesMap.get("ATTR_NEAR_HOUSE_2ND_CATEGORY_TERRITORY_TOTAL_SQUARE");
		String nearHouse3rdCategorySquare = typesMap.get("ATTR_NEAR_HOUSE_3D_CATEGORY_TERRITORY_TOTAL_SQUARE");
		String nearHouseTotalSquare = typesMap.get("ATTR_NEAR_HOUSE_TERRITORY_TOTAL_SQUARE");

		if (!validator.checkArea(nearHouseNonCategorySquare)) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_near_house_non_category_square"));
		}

		if (!validator.checkArea(nearHouse1stCategorySquare)) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_near_house_1st_category_square"));
		}

		if (!validator.checkArea(nearHouse2ndCategorySquare)) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_near_house_2nd_category_square"));
		}

		if (!validator.checkArea(nearHouse3rdCategorySquare)) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_near_house_3rd_category_square"));
		}

		if (!validator.checkArea(nearHouseTotalSquare)) {
			addActionError(getText("tc.errors.building_attributes.validation.invalid_near_house_total_square"));
		}

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
				realTotal = realTotal.add(nonCategory).add(firstCategory).add(secondCategory).add(thirdCategory);

				if (realTotal.compareTo(userTotal) != 0) {
					addActionError(getText("tc.errors.building_attributes.validation.invalid_near_house_total_square_bad_sum"));
				}
			} catch (NumberFormatException nfe) {
				log.debug("Not all near house squares are valid. Total sum check is skipped.");
			}
		} else {
			log.debug("Not all near house squares are present. Total sum check is skipped.");
		}

		return !hasActionErrors();
	}

	public void setBuilding(BuildingAddress building) {
		this.building = building;
	}

	public void setAttributeDate(String attributeDate) {
		this.attributeDate = DateUtil.parseBeginDate(attributeDate);
	}

	public void setAttrs(Map<String, String> attrs) {
		this.attrs = attrs;
	}

	public Map<String, String> getAttrs() {
		return attrs;
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
