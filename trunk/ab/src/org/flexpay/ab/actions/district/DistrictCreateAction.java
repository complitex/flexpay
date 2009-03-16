package org.flexpay.ab.actions.district;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.actions.nametimedependent.CreateAction;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.jetbrains.annotations.NotNull;

public class DistrictCreateAction extends CreateAction<
		DistrictName, DistrictNameTemporal, District, DistrictNameTranslation> {

	private TownService townService;
	private DistrictService districtService;

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();

	public DistrictCreateAction() {
		object = new District();
		townFilter.setNeedAutoChange(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@NotNull
	public String doExecute() throws Exception {
		ArrayStack filterArrayStack = getFilters();
		for (Object filter : filterArrayStack) {
			((PrimaryKeyFilter) filter).initFilter(session);
		}
		ArrayStack filters = parentService.initFilters(filterArrayStack, userPreferences.getLocale());
		setFilters(filters);

		if (isSubmit()) {

			if (!townFilter.needFilter()) {
				addActionError(getText("ab.error.street.no_town"));
				return INPUT;
			}

			DistrictName name = new DistrictName();
			for (DistrictNameTranslation translation : nameTranslations) {
				if (StringUtils.isNotBlank(translation.getName())) {
					name.addNameTranslation(translation);
				}
			}

			object.setNameForDate(name, date);

			Town town = townService.readFull(townFilter.getSelectedStub());
			object.setParent(town);

			if (object.isNew()) {
				districtService.create(object);
			} else {
				districtService.update(object);
			}

			return REDIRECT_SUCCESS;
		}
		return INPUT;
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
	 * Get initial set of filters for action
	 *
	 * @return Collection of filters
	 */
	protected ArrayStack getFilters() {
		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		filters.push(regionFilter);
		filters.push(townFilter);
		return filters;
	}

	/**
	 * Set filters for action
	 *
	 * @param filters collection of filters
	 */
	protected void setFilters(ArrayStack filters) {
		townFilter = (TownFilter) filters.peek(0);
		regionFilter = (RegionFilter) filters.peek(1);
		countryFilter = (CountryFilter) filters.peek(2);
	}

	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}
}
