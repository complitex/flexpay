package org.flexpay.ab.actions.apartment;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.AddressAttribute;
import org.flexpay.ab.persistence.AddressAttributeTypeTranslation;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public abstract class BuildingsFilterDependentAction extends FPActionSupport {

	private ParentService parentService;

	protected CountryFilter countryFilter = new CountryFilter();
	protected RegionFilter regionFilter = new RegionFilter();
	protected TownFilter townFilter = new TownFilter();
	protected StreetFilter streetFilter = new StreetFilter();
	protected StreetNameFilter streetNameFilter = new StreetNameFilter();
	protected BuildingsFilter buildingsFilter = new BuildingsFilter();

	private String filtersError;

	protected void initFilters() {
		try {
			ArrayStack filterArrayStack = getFilters();
			for (Object filter : filterArrayStack) {
				((PrimaryKeyFilter) filter).initFilter(session);
			}
			ArrayStack filters = parentService.initFilters(filterArrayStack, userPreferences.getLocale());
			setFilters(filters);
		} catch (FlexPayException e) {
			filtersError = e.getErrorKey();
		}
	}

	/**
	 * Getter for property 'filters'.
	 *
	 * @return Value for property 'filters'.
	 */
	public ArrayStack getFilters() {

		if (streetNameFilter.needFilter() && !streetFilter.needFilter()) {
			streetFilter.setSelectedId(streetNameFilter.getSelectedId());
		}

		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		filters.push(regionFilter);
		filters.push(townFilter);
		filters.push(streetFilter);
		filters.push(buildingsFilter);

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

	/**
	 * Setter for property 'filters'.
	 *
	 * @param filters Value to set for property 'filters'.
	 */
	public void setFilters(ArrayStack filters) {

		setFilters(filters, 5);
	}

	protected int setFilters(ArrayStack filters, int n) {
		countryFilter = (CountryFilter) filters.peek(--n);
		regionFilter = (RegionFilter) filters.peek(--n);
		townFilter = (TownFilter) filters.peek(--n);
		streetFilter = (StreetFilter) filters.peek(--n);
		buildingsFilter = (BuildingsFilter) filters.peek(--n);
		return n;
	}

	/**
	 * @return the countryFilter
	 */
	public CountryFilter getCountryFilter() {
		return countryFilter;
	}

	/**
	 * @param countryFilter the countryFilter to set
	 */
	public void setCountryFilter(CountryFilter countryFilter) {
		this.countryFilter = countryFilter;
	}

	/**
	 * @return the regionFilter
	 */
	public RegionFilter getRegionFilter() {
		return regionFilter;
	}

	/**
	 * @param regionFilter the regionFilter to set
	 */
	public void setRegionFilter(RegionFilter regionFilter) {
		this.regionFilter = regionFilter;
	}

	/**
	 * @return the townFilter
	 */
	public TownFilter getTownFilter() {
		return townFilter;
	}

	/**
	 * @param townFilter the townFilter to set
	 */
	public void setTownFilter(TownFilter townFilter) {
		this.townFilter = townFilter;
	}

	/**
	 * @return the streetFilter
	 */
	public StreetFilter getStreetFilter() {
		return streetFilter;
	}

	/**
	 * @param streetFilter the streetFilter to set
	 */
	public void setStreetFilter(StreetFilter streetFilter) {
		this.streetFilter = streetFilter;
	}

	public StreetNameFilter getStreetNameFilter() {
		return streetNameFilter;
	}

	public void setStreetNameFilter(StreetNameFilter streetNameFilter) {
		this.streetNameFilter = streetNameFilter;
	}

	/**
	 * @return the buildingsFilter
	 */
	public BuildingsFilter getBuildingsFilter() {
		return buildingsFilter;
	}

	/**
	 * @param buildingsFilter the buildingsFilter to set
	 */
	public void setBuildingsFilter(BuildingsFilter buildingsFilter) {
		this.buildingsFilter = buildingsFilter;
	}

	/**
	 * Setter for property 'parentService'.
	 *
	 * @param parentService Value to set for property 'parentService'.
	 */
	public void setParentService(ParentService parentService) {
		this.parentService = parentService;
	}

	/**
	 * @return the filtersError
	 */
	public String getFiltersError() {
		return filtersError;
	}
}
