package org.flexpay.ab.actions.apartment;

import org.apache.commons.collections.ArrayStack;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.persistence.BuildingAttribute;
import org.flexpay.ab.persistence.BuildingAttributeTypeTranslation;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;

import java.util.Collection;

public class FiltersBaseAction extends FPActionSupport implements
		SessionAware {

	private ParentService<BuildingsFilter> parentService;

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetFilter streetFilter = new StreetFilter();
	private BuildingsFilter buildingsFilter = new BuildingsFilter();

	String filtersError;

	protected void initFilters() {
		try {
			ArrayStack filterArrayStack = getFilters();
			for (Object filter : filterArrayStack) {
				((PrimaryKeyFilter) filter).initFilter(session);
			}
			ArrayStack filters = parentService.initFilters(filterArrayStack,
					userPreferences.getLocale());
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

		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		filters.push(regionFilter);
		filters.push(townFilter);
		filters.push(streetFilter);
		filters.push(buildingsFilter);

		return filters;
	}

	public String getBuildingNumber(Collection<BuildingAttribute> attributes) {

		try {
			if (log.isDebugEnabled()) {
				log.debug("Getting building number, attributes: " + attributes);
			}

			StringBuilder number = new StringBuilder();
			for (BuildingAttribute attribute : attributes) {
				if (attribute == null) {
					continue;
				}
				BuildingAttributeTypeTranslation attributeTypeTranslation =
						getTranslation(attribute.getBuildingAttributeType().getTranslations());
				if (attributeTypeTranslation.getShortName() != null) {
					number.append(attributeTypeTranslation.getShortName()).append(' ');
				} else {
					number.append(attributeTypeTranslation.getName()).append(' ');
				}

				number.append(attribute.getValue()).append(' ');
			}

			if (log.isDebugEnabled()) {
				log.debug("Building: " + number);
			}

			return number.toString().trim();
		} catch (Exception e) {
			log.error("Exception", e);
			return "error";
		}
	}

	/**
	 * Setter for property 'filters'.
	 *
	 * @param filters Value to set for property 'filters'.
	 */
	public void setFilters(ArrayStack filters) {
		countryFilter = (CountryFilter) filters.peek(4);
		regionFilter = (RegionFilter) filters.peek(3);
		townFilter = (TownFilter) filters.peek(2);
		streetFilter = (StreetFilter) filters.peek(1);
		buildingsFilter = (BuildingsFilter) filters.peek(0);
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
	public void setParentService(ParentService<BuildingsFilter> parentService) {
		this.parentService = parentService;
	}

	/**
	 * @return the filtersError
	 */
	public String getFiltersError() {
		return filtersError;
	}

}
