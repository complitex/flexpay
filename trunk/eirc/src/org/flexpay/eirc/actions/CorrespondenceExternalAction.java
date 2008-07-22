package org.flexpay.eirc.actions;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.util.config.ApplicationConfig;

import java.util.ArrayList;
import java.util.List;

public class CorrespondenceExternalAction extends FPActionSupport {

	private List<String> elementList; // TODO It's just stub

	private ParentService<TownFilter> parentService;

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();

	public CorrespondenceExternalAction() {
		townFilter.setSelectedId(ApplicationConfig.getDefaultTown().getId());
		regionFilter.setSelectedId(ApplicationConfig.getDefaultRegion().getId());
		countryFilter.setSelectedId(ApplicationConfig.getDefaultCountry().getId());
	}

	public String doExecute() throws FlexPayException {

		ArrayStack filters = parentService.initFilters(getFilters(), userPreferences.getLocale());
		setFilters(filters);

		elementList = retrieveElementList();

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	protected String getErrorResult() {
		return SUCCESS;
	}

	private List<String> retrieveElementList() {

		// TODO It's just stub
		List<String> strings = new ArrayList<String>();
		strings.add("record1");
		strings.add("record2");
		strings.add("record3");
		strings.add("record4");
		strings.add("record5");
		strings.add("record6");

		return strings;
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

		return filters;
	}

	/**
	 * Setter for property 'filters'.
	 *
	 * @param filters Value to set for property 'filters'.
	 */
	public void setFilters(ArrayStack filters) {
		countryFilter = (CountryFilter) filters.peek(2);
		regionFilter = (RegionFilter) filters.peek(1);
		townFilter = (TownFilter) filters.peek(0);
	}

	/**
	 * @return the elementList
	 */
	public List<String> getElementList() {
		return elementList;
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

	public void setParentService(ParentService<TownFilter> parentService) {
		this.parentService = parentService;
	}
}
