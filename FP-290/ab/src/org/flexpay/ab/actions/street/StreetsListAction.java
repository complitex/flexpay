package org.flexpay.ab.actions.street;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetName;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.StreetNameFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.persistence.sorter.StreetSorterByName;
import org.flexpay.ab.persistence.sorter.StreetSorterByType;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.LanguageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class StreetsListAction extends FPActionSupport {

	private StreetService streetService;

	// filters
	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownFilter townFilter = new TownFilter();
	private StreetNameFilter streetNameFilter = new StreetNameFilter();

	// sorters
	private StreetSorterByName streetSorterByName = new StreetSorterByName();
	private StreetSorterByType streetSorterByType = new StreetSorterByType();

	protected Page<Street> pager = new Page<Street>();
	protected List<Street> streets = Collections.emptyList();

	public StreetsListAction() {
//		streetSorterByName.activate();
	}

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected String doExecute() throws Exception {

		ArrayStack filterArrayStack = getFilters();
		for (Object filter : filterArrayStack) {
			((PrimaryKeyFilter<?>) filter).initFilter(session);
		}
		ArrayStack filters = streetService.initFilters(filterArrayStack, userPreferences.getLocale());
		setFilters(filters);

		streetSorterByName.setLang(LanguageUtil.getLanguage(userPreferences.getLocale()));
		streetSorterByType.setLang(LanguageUtil.getLanguage(userPreferences.getLocale()));

		List<ObjectSorter> sorters = CollectionUtils.<ObjectSorter>list(streetSorterByName, streetSorterByType);

		initObjects(filters, sorters);
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

	private void initObjects(ArrayStack filters, List<ObjectSorter> sorters) {

		List<Street> streetStubs = streetService.find(filters, sorters, pager);
		streets = CollectionUtils.list();
		for (Street street : streetStubs) {
			streets.add(streetService.readFull(stub(street)));
		}

		log.debug("Fetched: {} streets", streets.size());
		log.debug("Search string: {}", streetNameFilter.getSearchString());
		log.debug("Sorters: {}", sorters);
	}

	public String getStreetName(@NotNull Long id) {

		Street street = streetService.readFull(new Stub<Street>(id));
		if (street == null) {
			throw new RuntimeException("Invalid street id: " + id);
		}

		StreetName name = street.getCurrentName();
		if (name == null) {
			throw new RuntimeException("No current name for street: " + id);
		}

		return getTranslation(name.getTranslations()).getName();
	}

	public String getStreetType(@NotNull Long id) {
		Street street = streetService.readFull(new Stub<Street>(id));
		if (street == null) {
			throw new RuntimeException("Invalid street id: " + id);
		}

		StreetType type = street.getCurrentType();
		if (type == null) {
			throw new RuntimeException("No current type for street: " + id);
		}

		return getTranslation(type.getTranslations()).getName();
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
	 * Get initial set of filters for action
	 *
	 * @return Collection of filters
	 */
	protected ArrayStack getFilters() {
		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		filters.push(regionFilter);
		filters.push(townFilter);
		filters.push(streetNameFilter);
		return filters;
	}

	/**
	 * Set filters for action
	 *
	 * @param filters collection of filters
	 */
	protected void setFilters(ArrayStack filters) {
		int n = 0;
		streetNameFilter = (StreetNameFilter) filters.peek(n++);
		townFilter = (TownFilter) filters.peek(n++);
		regionFilter = (RegionFilter) filters.peek(n++);
		countryFilter = (CountryFilter) filters.peek(n);
	}

	public Page<Street> getPager() {
		return pager;
	}

	public void setPager(Page<Street> pager) {
		this.pager = pager;
	}

	public List<Street> getStreets() {
		return streets;
	}

	public StreetSorterByName getStreetSorterByName() {
		return streetSorterByName;
	}

	public void setStreetSorterByName(StreetSorterByName streetSorterByName) {
		this.streetSorterByName = streetSorterByName;
	}

	public StreetSorterByType getStreetSorterByType() {
		return streetSorterByType;
	}

	public void setStreetSorterByType(StreetSorterByType streetSorterByType) {
		this.streetSorterByType = streetSorterByType;
	}

	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}
}