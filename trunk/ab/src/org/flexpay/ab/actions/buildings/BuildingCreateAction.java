package org.flexpay.ab.actions.buildings;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class BuildingCreateAction extends FPActionSupport {

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetNameFilter streetNameFilter = new StreetNameFilter();
	private DistrictFilter districtFilter = new DistrictFilter();

	private Long buildingId;
	private BuildingAddress buildings = new BuildingAddress();

	// type id to value mapping
	private Map<Long, String> attributeMap = CollectionUtils.treeMap();

	private ParentService<StreetFilter> parentService;
	private BuildingService buildingService;
	private AddressAttributeTypeService addressAttributeTypeService;
	private DistrictService districtService;

	public BuildingCreateAction() {
		streetNameFilter.setShowSearchString(true);
		streetNameFilter.setNeedAutoChange(false);
		districtFilter.setNeedAutoChange(false);
	}

	@NotNull
	public String doExecute() throws FlexPayException {

		setupAttributes();
		setupFilters();

		if (isSubmit()) {

			// validate 
			if (!districtFilter.needFilter()) {
				addActionError(getText("ab.buildings.create.district_required"));
				return INPUT;
			}
			if (!streetNameFilter.needFilter()) {
				addActionError(getText("ab.buildings.create.street_required"));
				return INPUT;
			}
			if (buildings.getBuildingAttributes().isEmpty()) {
				addActionError(getText("ab.buildings.create.buildings_attr_required"));
				return INPUT;
			}

			log.debug("About to save new building");
			if (buildingId == null) {
				buildings = buildingService.createStreetDistrictBuildings(
						streetNameFilter.getSelectedStub(), districtFilter.getSelectedStub(),
						buildings.getBuildingAttributes());
				addActionError(getText("ab.buildings.created_successfully"));
				return REDIRECT_SUCCESS;
			} else {
				buildings = buildingService.createStreetBuildings(
						new Stub<Building>(buildingId), streetNameFilter.getSelectedStub(),
						buildings.getBuildingAttributes());
				return "edit";
			}
		}

		return INPUT;
	}

	private void setupFilters() throws FlexPayException {
		ArrayStack filterArrayStack = getFilters();
		for (Object filter : filterArrayStack) {
			((PrimaryKeyFilter<?>) filter).initFilter(session);
		}
		ArrayStack filters = parentService.initFilters(filterArrayStack, getUserPreferences().getLocale());
		setFilters(filters);

		if (buildingId != null) {
			Building building = buildingService.read(new Stub<Building>(buildingId));
			districtFilter.setSelectedId(building.getDistrict().getId());
			districtFilter.setReadOnly(true);
		}
		districtService.initFilter(districtFilter, townFilter, getUserPreferences().getLocale());
	}

	private void setupAttributes() {

		log.debug("Attributes: {}", attributeMap);

		for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
			String value = attributeMap.get(type.getId());
			if (StringUtils.isNotBlank(value)) {
				buildings.setBuildingAttribute(value, type);
			}
			attributeMap.put(type.getId(), value);
		}
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return INPUT;
	}

	public String getTypeName(Long typeId) throws FlexPayException {
		AddressAttributeType type = addressAttributeTypeService.read(new Stub<AddressAttributeType>(typeId));
		if (type == null) {
			throw new RuntimeException("Unknown type id: " + typeId);
		}
		return getTranslation(type.getTranslations()).getName();
	}

	public ArrayStack getFilters() {

		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		filters.push(regionFilter);
		filters.push(townFilter);
		filters.push(streetNameFilter);

		return filters;
	}

	public void setFilters(ArrayStack filters) {
		countryFilter = (CountryFilter) filters.peek(3);
		regionFilter = (RegionFilter) filters.peek(2);
		townFilter = (TownFilter) filters.peek(1);
		streetNameFilter = (StreetNameFilter) filters.peek(0);
	}

	public CountryFilter getCountryFilter() {
		return countryFilter;
	}

	public void setCountryFilter(CountryFilter countryFilter) {
		this.countryFilter = countryFilter;
	}

	public RegionFilter getRegionFilter() {
		return regionFilter;
	}

	public void setRegionFilter(RegionFilter regionFilter) {
		this.regionFilter = regionFilter;
	}

	public TownFilter getTownFilter() {
		return townFilter;
	}

	public void setTownFilter(TownFilter townFilter) {
		this.townFilter = townFilter;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	public Long getBuildingId() {
		return buildingId;
	}

	public BuildingAddress getBuildings() {
		return buildings;
	}

	public Map<Long, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<Long, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public StreetNameFilter getStreetNameFilter() {
		return streetNameFilter;
	}

	public void setStreetNameFilter(StreetNameFilter streetNameFilter) {
		this.streetNameFilter = streetNameFilter;
	}

	public DistrictFilter getDistrictFilter() {
		return districtFilter;
	}

	public void setDistrictFilter(DistrictFilter districtFilter) {
		this.districtFilter = districtFilter;
	}

	@Required
	public void setParentService(ParentService<StreetFilter> parentService) {
		this.parentService = parentService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@Required
	public void setBuildingAttributeTypeService(AddressAttributeTypeService addressAttributeTypeService) {
		this.addressAttributeTypeService = addressAttributeTypeService;
	}

}
