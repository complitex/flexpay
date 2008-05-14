package org.flexpay.ab.actions.buildings;

import java.util.Map;

import org.apache.commons.collections.ArrayStack;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;

public class BuildingsCreateAction extends CommonAction implements SessionAware {

	private ParentService parentService;
	private BuildingService buildingService;

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetFilter streetFilter = new StreetFilter();
	private Map session;

	private BuildingAttributeType typeNumber;
	private BuildingAttributeType typeBulk;
	
	private String action;

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	public String execute() throws FlexPayException {
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
		} catch (FlexPayException e) {
			e.toString();
		}

		return "form";
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

}
