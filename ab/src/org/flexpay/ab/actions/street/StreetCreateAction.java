package org.flexpay.ab.actions.street;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.actions.nametimedependent.CreateAction;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.jetbrains.annotations.NotNull;

public class StreetCreateAction extends CreateAction<
		StreetName, StreetNameTemporal, Street, StreetNameTranslation> {

	private StreetTypeService streetTypeService;
	private StreetService streetService;
	private TownService townService;

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetTypeFilter streetTypeFilter = new StreetTypeFilter();

	public StreetCreateAction() {
		object = new Street();
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

		streetTypeService.initFilter(streetTypeFilter, userPreferences.getLocale());
		if (isSubmit()) {
			if (!streetTypeFilter.needFilter()) {
				addActionError(getText("ab.error.street.no_type"));
				return INPUT;
			}
			if (!townFilter.needFilter()) {
				addActionError(getText("ab.error.street.no_town"));
				return INPUT;
			}

			StreetName name = new StreetName();
			for (StreetNameTranslation translation : nameTranslations) {
				if (StringUtils.isNotBlank(translation.getName())) {
					name.addNameTranslation(translation);
				}
			}
			StreetType type = streetTypeService.read(streetTypeFilter.getSelectedId());

			object.setTypeForDate(type, date);
			object.setNameForDate(name, date);

			Town town = townService.readFull(townFilter.getSelectedStub());
			object.setParent(town);

			streetService.save(object);

			return SUCCESS;
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

	public StreetTypeFilter getStreetTypeFilter() {
		return streetTypeFilter;
	}

	public void setStreetTypeFilter(StreetTypeFilter streetTypeFilter) {
		this.streetTypeFilter = streetTypeFilter;
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

	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	public void setTownService(TownService townService) {
		this.townService = townService;
	}
}
