package org.flexpay.ab.actions.buildings;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;

public class BuildingsCreateAction extends CommonAction implements SessionAware {

	private ParentService parentService;
	private BuildingService buildingService;
	private DistrictService districtService;

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetFilter streetFilter = new StreetFilter();
	private Map session;

	private BuildingAttributeType typeNumber;
	private BuildingAttributeType typeBulk;
	
	private List<District> districtList = Collections.emptyList();
	
	private String action;
	private Long districtId;
	private String numberValue;
	private String bulkValue;
	
	private String filterError;
	private String districtError;
	private String streetError;
	private String buildingAttrError;
	
	
	public String execute() throws FlexPayException {
		if("create".equals(action)) {
			if(districtId == null) {
				districtError = "ab.buildings.create.district_required";
			}
			if(streetFilter.getSelectedId() == null) {
				streetError = "ab.buildings.create.street_required";
			}
			if(StringUtils.isEmpty(numberValue) && StringUtils.isEmpty(bulkValue)) {
				buildingAttrError = "ab.buildings.create.buildings_attr_required";
			}
			
			if(streetFilter.getSelectedId() != null && districtId != null && (!StringUtils.isEmpty(numberValue) || !StringUtils.isEmpty(bulkValue))) {
				buildingService.createBuildings(new Street(streetFilter.getSelectedId()), new District(districtId), numberValue, bulkValue);
				return "list";
			}
		}
		
		typeNumber = buildingService
				.getAttributeType(BuildingAttributeType.TYPE_NUMBER);
		typeBulk = buildingService
				.getAttributeType(BuildingAttributeType.TYPE_BULK);

		if(action != null && action.equals("create")) {
			
		}
		
		try {
			ArrayStack filterArrayStack = getFilters();
			for (Object filter : filterArrayStack) {
				((PrimaryKeyFilter) filter).initFilter(session);
			}
			ArrayStack filters = parentService.initFilters(filterArrayStack,
					getUserPreferences().getLocale());
			setFilters(filters);
			
			districtList = districtService.findByTown(townFilter.getSelectedId());
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
	 * @param filters
	 *            Value to set for property 'filters'.
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
	 * @param countryFilter
	 *            Value to set for property 'countryFilter'.
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
	 * @param regionFilter
	 *            Value to set for property 'regionFilter'.
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
	 * @param townFilter
	 *            Value to set for property 'townFilter'.
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
	 * @param streetFilter
	 *            Value to set for property 'streetFilter'.
	 */
	public void setStreetFilter(StreetFilter streetFilter) {
		this.streetFilter = streetFilter;
	}

	/**
	 * Sets the Map of session attributes in the implementing class.
	 * 
	 * @param session
	 *            a Map of HTTP session attribute name/value pairs.
	 */
	public void setSession(Map session) {
		this.session = session;
	}

	/**
	 * @param parentService
	 *            the parentService to set
	 */
	public void setParentService(ParentService parentService) {
		this.parentService = parentService;
	}

	/**
	 * @param buildingService
	 *            the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	/**
	 * @return the typeNumber
	 */
	public BuildingAttributeType getTypeNumber() {
		return typeNumber;
	}

	/**
	 * @return the typeBulk
	 */
	public BuildingAttributeType getTypeBulk() {
		return typeBulk;
	}

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
	 * @param numberValue the numberValue to set
	 */
	public void setNumberValue(String numberValue) {
		this.numberValue = numberValue;
	}

	/**
	 * @param bulkValue the bulkValue to set
	 */
	public void setBulkValue(String bulkValue) {
		this.bulkValue = bulkValue;
	}

	/**
	 * @return the numberValue
	 */
	public String getNumberValue() {
		return numberValue;
	}

	/**
	 * @return the bulkValue
	 */
	public String getBulkValue() {
		return bulkValue;
	}

	

}
