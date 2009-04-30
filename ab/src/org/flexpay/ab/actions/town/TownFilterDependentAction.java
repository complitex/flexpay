package org.flexpay.ab.actions.town;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.AddressAttribute;
import org.flexpay.ab.persistence.AddressAttributeTypeTranslation;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;

/**
 * Town filter dependent action
 */
public abstract class TownFilterDependentAction extends FPActionSupport {

	protected CountryFilter countryFilter = new CountryFilter();
	protected RegionFilter regionFilter = new RegionFilter();
	protected TownFilter townFilter = new TownFilter();

	private ParentService<?> parentService;

	protected void initFilters() {
		try {
			ArrayStack filterArrayStack = getFilters();
			for (Object filter : filterArrayStack) {
				((ObjectFilter) filter).initFilter(session);
			}
			ArrayStack filters = parentService.initFilters(filterArrayStack, userPreferences.getLocale());
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

		return filters;
	}

	public String getBuildingNumber(@Nullable BuildingAddress buildingAddress) throws Exception {
		if (buildingAddress != null) {
			return getBuildingNumber(buildingAddress.getBuildingAttributes());
		}

		return null;
	}

	public String getBuildingNumber(Collection<AddressAttribute> attributes) throws Exception {

		StringBuilder number = new StringBuilder();
		for (AddressAttribute attribute : attributes) {

			AddressAttributeTypeTranslation typeTranslation =
					getTranslation(attribute.getBuildingAttributeType().getTranslations());
			if (typeTranslation.getShortName() != null) {
				number.append(typeTranslation.getShortName()).append(' ');
			} else {
				number.append(typeTranslation.getName()).append(' ');
			}

			number.append(attribute.getValue()).append(' ');
		}

		return number.toString().trim();
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

	@Required
	public void setParentService(ParentService<?> parentService) {
		this.parentService = parentService;
	}

}
