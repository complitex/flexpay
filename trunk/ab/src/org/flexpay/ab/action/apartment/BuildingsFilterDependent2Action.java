package org.flexpay.ab.action.apartment;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.ab.util.TranslationUtil;
import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;

/**
 * Uses StreetNameFilter
 */
public abstract class BuildingsFilterDependent2Action<T> extends FPActionWithPagerSupport<T> {

	protected CountryFilter countryFilter = new CountryFilter();
	protected RegionFilter regionFilter = new RegionFilter();
	protected TownFilter townFilter = new TownFilter();
	protected StreetNameFilter streetNameFilter = new StreetNameFilter();
	protected BuildingsFilter buildingsFilter = new BuildingsFilter();

	private ParentService<?> parentService;

	@SuppressWarnings ({"unchecked"})
	protected void initFilters() {
		try {
			ArrayStack filterArrayStack = getFilters();
			for (Object filter : filterArrayStack) {
				((ObjectFilter) filter).initFilter(session);
			}
			ArrayStack filters = parentService.initFilters(filterArrayStack, getUserPreferences().getLocale());
			setFilters(filters);
		} catch (FlexPayException e) {
			if (!ignoreFilterInitErrors()) {
				addActionError(e);
			}

			log.info("Failed init filters", e);
		}
	}

	public ArrayStack getFilters() {

		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		filters.push(regionFilter);
		filters.push(townFilter);
		filters.push(streetNameFilter);
		filters.push(buildingsFilter);

		return filters;
	}

	public String getBuildingNumber(@Nullable BuildingAddress buildingAddress) throws Exception {
		if (buildingAddress != null) {
			return TranslationUtil.getBuildingNumber(buildingAddress, getUserPreferences().getLocale());
		}

		return null;
	}

	protected boolean ignoreFilterInitErrors() {
		return false;
	}

	public void setFilters(ArrayStack filters) {
		setFilters(filters, 5);
	}

	protected int setFilters(ArrayStack filters, int n) {
		countryFilter = (CountryFilter) filters.peek(--n);
		regionFilter = (RegionFilter) filters.peek(--n);
		townFilter = (TownFilter) filters.peek(--n);
		streetNameFilter = (StreetNameFilter) filters.peek(--n);
		buildingsFilter = (BuildingsFilter) filters.peek(--n);
		return n;
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

	public BuildingsFilter getBuildingsFilter() {
		return buildingsFilter;
	}

	public void setBuildingsFilter(BuildingsFilter buildingsFilter) {
		this.buildingsFilter = buildingsFilter;
	}

	@Required
	public void setParentService(ParentService<?> parentService) {
		this.parentService = parentService;
	}

}
