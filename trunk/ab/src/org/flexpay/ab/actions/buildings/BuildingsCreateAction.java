package org.flexpay.ab.actions.buildings;

import com.opensymphony.xwork2.Preparable;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.Nls;

import java.util.*;

public class BuildingsCreateAction extends FPActionSupport implements Preparable {

	private ParentService parentService;
	private BuildingService buildingService;
	private DistrictService districtService;

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetFilter streetFilter = new StreetFilter();

	private List<District> districtList = Collections.emptyList();
	private District district;

	private String action;
	private Long districtId;
	private Long buildingId;
	// private String numberValue;
	// private String bulkValue;
	private Buildings buildings = new Buildings();
	private Map<String, BuildingAttribute> attributeMap;

	private String filterError;
	private String districtError;
	private String streetError;
	private String buildingAttrError;
	private String creatingError;

	public void prepare() {
		Set<BuildingAttribute> buildingAttributeSet = new HashSet<BuildingAttribute>();
		attributeMap = new HashMap<String, BuildingAttribute>();
		for (BuildingAttributeType type : buildingService.getAttributeTypes()) {
			if (attributeMap.get("" + type.getId()) == null) {
				BuildingAttribute attr = new BuildingAttribute();
				attr.setBuildingAttributeType(type);
				buildingAttributeSet.add(attr);
				attributeMap.put("" + type.getId(), attr);
			}
		}
		buildings.setBuildingAttributes(buildingAttributeSet);
	}

	@Nls
	public String execute() throws FlexPayException {
		if ("create".equals(action)) {

			if (districtId == null) {
				districtError = "ab.buildings.create.district_required";
			}
			if (streetFilter.getSelectedId() == null) {
				streetError = "ab.buildings.create.street_required";
			}

			boolean isAttrEmpty = true;
			for (BuildingAttribute attr : buildings.getBuildingAttributes()) {
				if (!StringUtils.isEmpty(attr.getValue())) {
					isAttrEmpty = false;
					break;
				}
			}
			if (isAttrEmpty) {
				buildingAttrError = "ab.buildings.create.buildings_attr_required";
			}

			if (streetFilter.getSelectedId() != null && districtId != null
				&& !isAttrEmpty) {

				try {
					if (buildingId == null) {
						buildings = buildingService.createStreetDistrictBuildings(
								streetFilter.getSelectedStub(),
								new Stub<District>(districtId),
								buildings.getBuildingAttributes());
						return "list";
					} else {
						buildings = buildingService.createStreetBuildings(
								new Stub<Building>(buildingId), streetFilter.getSelectedStub(),
								buildings.getBuildingAttributes());
						return "edit";
					}
				} catch (FlexPayException e) {
					creatingError = e.getErrorKey();
				}
			}
		}

		try {
			ArrayStack filterArrayStack = getFilters();
			for (Object filter : filterArrayStack) {
				((PrimaryKeyFilter) filter).initFilter(session);
			}
			ArrayStack filters = parentService.initFilters(filterArrayStack,
					getUserPreferences().getLocale());
			setFilters(filters);

			if (buildingId != null) {
				Building building = buildingService.readBuilding(buildingId);
				district = districtService.read(building.getDistrict().getId());
			} else {
				districtList = districtService.findByTown(townFilter
						.getSelectedId());
			}
		} catch (FlexPayException e) {
			filterError = e.getErrorKey();
		}

		return "form";
	}

	/**
	 * @return the errorKey
	 */
	public String getFilterError() {
		return filterError;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Getter for property 'filters'.
	 *
	 * @return Value for property 'filters'.
	 */
	public ArrayStack getFilters() {

		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		filters.push(regionFilter);
		filters.push(townFilter);
		filters.push(streetFilter);

		return filters;
	}

	/**
	 * Setter for property 'filters'.
	 *
	 * @param filters Value to set for property 'filters'.
	 */
	public void setFilters(ArrayStack filters) {
		countryFilter = (CountryFilter) filters.peek(3);
		regionFilter = (RegionFilter) filters.peek(2);
		townFilter = (TownFilter) filters.peek(1);
		streetFilter = (StreetFilter) filters.peek(0);
	}

	/**
	 * Getter for property 'countryFilter'.
	 *
	 * @return Value for property 'countryFilter'.
	 */
	public CountryFilter getCountryFilter() {
		return countryFilter;
	}

	/**
	 * Setter for property 'countryFilter'.
	 *
	 * @param countryFilter Value to set for property 'countryFilter'.
	 */
	public void setCountryFilter(CountryFilter countryFilter) {
		this.countryFilter = countryFilter;
	}

	/**
	 * Getter for property 'regionFilter'.
	 *
	 * @return Value for property 'regionFilter'.
	 */
	public RegionFilter getRegionFilter() {
		return regionFilter;
	}

	/**
	 * Setter for property 'regionFilter'.
	 *
	 * @param regionFilter Value to set for property 'regionFilter'.
	 */
	public void setRegionFilter(RegionFilter regionFilter) {
		this.regionFilter = regionFilter;
	}

	/**
	 * Getter for property 'townFilter'.
	 *
	 * @return Value for property 'townFilter'.
	 */
	public TownFilter getTownFilter() {
		return townFilter;
	}

	/**
	 * Setter for property 'townFilter'.
	 *
	 * @param townFilter Value to set for property 'townFilter'.
	 */
	public void setTownFilter(TownFilter townFilter) {
		this.townFilter = townFilter;
	}

	/**
	 * Getter for property 'streetFilter'.
	 *
	 * @return Value for property 'streetFilter'.
	 */
	public StreetFilter getStreetFilter() {
		return streetFilter;
	}

	/**
	 * Setter for property 'streetFilter'.
	 *
	 * @param streetFilter Value to set for property 'streetFilter'.
	 */
	public void setStreetFilter(StreetFilter streetFilter) {
		this.streetFilter = streetFilter;
	}

	/**
	 * Sets the Map of session attributes in the implementing class.
	 *
	 * @param session a Map of HTTP session attribute name/value pairs.
	 */
	public void setSession(Map session) {
		this.session = session;
	}

	/**
	 * @param parentService the parentService to set
	 */
	public void setParentService(ParentService parentService) {
		this.parentService = parentService;
	}

	/**
	 * @param buildingService the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	/**
	 * @return the typeNumber
	 */
	/*
	 * public BuildingAttributeType getTypeNumber() { return typeNumber; }
	 * 
	 *//**
 * @return the typeBulk
 */
	/*
	 * public BuildingAttributeType getTypeBulk() { return typeBulk; }
	 */

	/**
	 * @return the districtList
	 */
	public List<District> getDistrictList() {
		return districtList;
	}

	/**
	 * @param districtService the districtService to set
	 */
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	/**
	 * @return the districtId
	 */
	public Long getDistrictId() {
		return districtId;
	}

	/**
	 * @param districtId the districtId to set
	 */
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	/**
	 * @return the districtError
	 */
	public String getDistrictError() {
		return districtError;
	}

	/**
	 * @return the streetError
	 */
	public String getStreetError() {
		return streetError;
	}

	/**
	 * @return the buildingAttrError
	 */
	public String getBuildingAttrError() {
		return buildingAttrError;
	}

	/**
	 * @param buildingId the buildingId to set
	 */
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	/**
	 * @return the district
	 */
	public District getDistrict() {
		return district;
	}

	/**
	 * @return the buildingId
	 */
	public Long getBuildingId() {
		return buildingId;
	}

	/**
	 * @return the createdBuildings
	 */
	public Buildings getBuildings() {
		return buildings;
	}

	/**
	 * @return the attributeMap
	 */
	public Map<String, BuildingAttribute> getAttributeMap() {
		return attributeMap;
	}

	/**
	 * @param attributeMap the attributeMap to set
	 */
	public void setAttributeMap(Map<String, BuildingAttribute> attributeMap) {
		this.attributeMap = attributeMap;
	}

	/**
	 * @return the creatingError
	 */
	public String getCreatingError() {
		return creatingError;
	}
}
