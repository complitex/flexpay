package org.flexpay.ab.actions.town;

import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.ActionUtil;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.collections.ArrayStack;

import java.util.Map;

public class TownEditSimpleAction extends FPActionSupport {

	private RegionService regionService;
	private TownTypeService townTypeService;
	private TownService townService;

	private CountryFilter countryFilter = new CountryFilter();
	private RegionFilter regionFilter = new RegionFilter();
	private TownTypeFilter townTypeFilter = new TownTypeFilter();
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private Map<Long, String> names = CollectionUtils.treeMap();
	private Town town = new Town();

	/**
	 * Constructor TownEditSimpleAction creates a new TownEditSimpleAction instance.
	 */
	public TownEditSimpleAction() {
		// disable automatic form submit on region filter change
		regionFilter.setNeedAutoChange(false);
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

		if (town.getId() == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		Town twn = town.isNew() ? town : townService.readFull(stub(town));
		if (twn == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		// init defaults and show input form
		if (!isSubmit()) {
			town = twn;
			initDefaults();
			initFilters();
			return INPUT;
		}

		initFilters();

		// collect data and save
		if (!regionFilter.needFilter()) {
			addActionError(getText("ab.error.town.no_region"));
			return INPUT;
		}
		if (!townTypeFilter.needFilter()) {
			addActionError(getText("ab.error.town.no_type"));
			return INPUT;
		}

		// Type
		twn.setTypeForDate(new TownType(townTypeFilter.getSelectedId()), beginDateFilter.getDate());

		// Name
		// init translations
		// todo refactor to common method used everywhere
		TownName townName = new TownName();
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			TownNameTranslation translation = new TownNameTranslation();
			translation.setLang(lang);
			translation.setName(value);
			townName.addNameTranslation(translation);
		}
		twn.setNameForDate(townName, beginDateFilter.getDate());

		log.debug("Names timeline: {}", twn.getNamesTimeLine());

		// setup region for new object
		if (twn.isNew()) {
			twn.setRegion(new Region(regionFilter.getSelectedId()));
		}

		townService.save(twn);
		return REDIRECT_SUCCESS;
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
		return INPUT;
	}

	private void initFilters() throws Exception {
		townTypeFilter = townTypeService.initFilter(townTypeFilter, getUserPreferences().getLocale());

		ArrayStack filters = CollectionUtils.arrayStack(countryFilter, regionFilter);
		regionService.initFilters(filters, getUserPreferences().getLocale());

		if (town.isNotNew()) {
			regionFilter.setReadOnly(true);
			countryFilter.setReadOnly(true);
		}
	}

	private void initDefaults() {
		TownNameTemporal tmprl = town.getCurrentNameTemporal();
		if (tmprl == null) {
			names = ActionUtil.getLangIdsToTranslations(new TownName());
			beginDateFilter.setDate(ApplicationConfig.getPastInfinite());
		} else {
			names = ActionUtil.getLangIdsToTranslations(tmprl.getValue());
			beginDateFilter.setDate(tmprl.getBegin());
		}

		TownTypeTemporal tmprlType = town.getCurrentTypeTemporal();
		if (tmprlType != null) {
			townTypeFilter.setSelectedId(tmprlType.getValue().getId());
		}

		if (town.isNotNew()) {
			regionFilter.setSelectedId(town.getRegionStub().getId());
		}
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

	public TownTypeFilter getTownTypeFilter() {
		return townTypeFilter;
	}

	public void setTownTypeFilter(TownTypeFilter townTypeFilter) {
		this.townTypeFilter = townTypeFilter;
	}

	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public Town getTown() {
		return town;
	}

	public void setTown(Town town) {
		this.town = town;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

	public void setTownService(TownService townService) {
		this.townService = townService;
	}
}
