package org.flexpay.tc.actions.buildingattributes;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.actions.buildings.BuildingsActionsBase;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.StreetNameFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class BuildingsListAction extends BuildingsActionsBase {
    
	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetNameFilter streetNameFilter = new StreetNameFilter();

	private List<BuildingAddress> buildingsList = list();

	private ParentService parentService;
	private BuildingService buildingService;

	public BuildingsListAction() {
		streetNameFilter.setShowSearchString(true);
	}

    /**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public String doExecute() throws Exception {

        ArrayStack filters = initFilters();

		buildingsList = buildingService.getBuildings(filters, getPager());

		return SUCCESS;
	}

    private ArrayStack initFilters() throws FlexPayException {

        ArrayStack filterArrayStack = getFilters();
        for (Object filter : filterArrayStack) {
            ((PrimaryKeyFilter) filter).initFilter(session);
        }

        ArrayStack filters = parentService.initFilters(filterArrayStack, userPreferences.getLocale());
        setFilters(filters);
        
        return filters;
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
		return SUCCESS;
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
		filters.push(streetNameFilter);

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
		streetNameFilter = (StreetNameFilter) filters.peek(0);
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

	public StreetNameFilter getStreetNameFilter() {
		return streetNameFilter;
	}

	public void setStreetNameFilter(StreetNameFilter streetNameFilter) {
		this.streetNameFilter = streetNameFilter;
	}

	/**
	 * Getter for property 'buildingsList'.
	 *
	 * @return Value for property 'buildingsList'.
	 */
	public List<BuildingAddress> getBuildingsList() {
		return buildingsList;
	}

    /**
     * Setter for property 'buildingsService'.
     *
     * @param buildingService Value to set for property 'buildingsService'.
     */
	@Required
    public void setBuildingService(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    /**
     * Setter for property 'parentService'.
     *
     * @param parentService Value to set for property 'parentService'.
     */
	@Required
    public void setParentService(ParentService parentService) {
        this.parentService = parentService;
    }

}
