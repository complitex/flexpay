package org.flexpay.ab.actions.buildings;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class BuildingsListAction extends FPActionWithPagerSupport {

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetNameFilter streetNameFilter = new StreetNameFilter();

	private List<BuildingAddress> buildingsList = list();

	private ParentService<StreetFilter> parentService;
	private BuildingService buildingService;

	public BuildingsListAction() {
		streetNameFilter.setShowSearchString(true);
	}

	@NotNull
	public String doExecute() throws Exception {

		ArrayStack filterArrayStack = getFilters();
		for (Object filter : filterArrayStack) {
			((PrimaryKeyFilter<?>) filter).initFilter(session);
		}

		ArrayStack filters = parentService.initFilters(filterArrayStack, userPreferences.getLocale());
		setFilters(filters);

		buildingsList = buildingService.getBuildings(filters, getPager());

		return SUCCESS;
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
		return SUCCESS;
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

	public StreetNameFilter getStreetNameFilter() {
		return streetNameFilter;
	}

	public void setStreetNameFilter(StreetNameFilter streetNameFilter) {
		this.streetNameFilter = streetNameFilter;
	}

	public List<BuildingAddress> getBuildingsList() {
		return buildingsList;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setParentService(ParentService<StreetFilter> parentService) {
		this.parentService = parentService;
	}

}
