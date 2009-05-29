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
import org.springframework.beans.factory.annotation.Required;

public class DistrictCreateAction extends CreateAction<
		DistrictName, DistrictNameTemporal, District, DistrictNameTranslation> {

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();

	private TownService townService;
	private DistrictService districtService;

	public DistrictCreateAction() {
		object = new District();
		townFilter.setNeedAutoChange(false);
	}

	@NotNull
	public String doExecute() throws Exception {
		ArrayStack filterArrayStack = getFilters();
		for (Object filter : filterArrayStack) {
			((PrimaryKeyFilter<?>) filter).initFilter(session);
		}
		ArrayStack filters = parentService.initFilters(filterArrayStack, userPreferences.getLocale());
		setFilters(filters);

		if (isSubmit()) {

			if (!doValidate()) {
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

	private boolean doValidate() {

		if (!townFilter.needFilter()) {
			addActionError(getText("ab.error.street.no_town"));
		}

		return !hasActionErrors();
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

	protected ArrayStack getFilters() {
		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		filters.push(regionFilter);
		filters.push(townFilter);
		return filters;
	}

	protected void setFilters(ArrayStack filters) {
		townFilter = (TownFilter) filters.peek(0);
		regionFilter = (RegionFilter) filters.peek(1);
		countryFilter = (CountryFilter) filters.peek(2);
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

}
