package org.flexpay.ab.actions.town;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.ActionUtil;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class TownEditSimpleAction extends FPActionSupport {

    private String regionFilter;
    private TownTypeFilter townTypeFilter = new TownTypeFilter();
    private BeginDateFilter beginDateFilter = new BeginDateFilter();

    private Map<Long, String> names = CollectionUtils.treeMap();
    private Town town = new Town();

	private String crumbCreateKey;
    private RegionService regionService;
    private TownTypeService townTypeService;
    private TownService townService;

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

        Town twn = town.isNew() ? town : townService.readFull(stub(town));
        initFilters();

        if (isSubmit()) {

            if (!doValidate()) {
                return INPUT;
            }

            updateTown(twn);

            return REDIRECT_SUCCESS;
        }

        town = twn;
        initDefaults();
        return INPUT;
    }

    private boolean doValidate() {

        if (town.getId() == null) {
            addActionError(getText("common.object_not_selected"));
            return false;
        }

        Town twn = town.isNew() ? town : townService.readFull(stub(town));
        if (twn == null) {
            addActionError(getText("error.invalid_id"));
            return false;
        }

		Long regionFilterLong;
		try {
			regionFilterLong = Long.parseLong(regionFilter);
		} catch (Exception e) {
			log.warn("Incorrect region id in filter ({})", regionFilter);
			addActionError(getText("ab.error.town.no_region"));
		}

        if (!townTypeFilter.needFilter()) {
            addActionError(getText("ab.error.town.no_type"));
        }

        if (!beginDateFilter.needFilter()) {
            addActionError(getText("ab.error.town.no_begin_date"));
        }

        return !hasActionErrors();
    }

    /*
    * Creates new town if it is a new one (haven't been yet persisted) or updates persistent one
    */
    private void updateTown(Town twn) throws FlexPayExceptionContainer {

        TownName townName = getTownName();
        twn.setNameForDate(townName, beginDateFilter.getDate());
        twn.setTypeForDate(new TownType(townTypeFilter.getSelectedId()), beginDateFilter.getDate());

        // setup region for new object
        if (twn.isNew()) {
            twn.setRegion(new Region(Long.parseLong(regionFilter)));
            townService.create(twn);
        } else {
            townService.update(twn);
        }
    }

    private TownName getTownName() {

        TownName townName = new TownName();
        for (Map.Entry<Long, String> name : names.entrySet()) {
            String value = name.getValue();
            Language lang = getLang(name.getKey());
            TownNameTranslation translation = new TownNameTranslation();
            translation.setLang(lang);
            translation.setName(value);
            townName.addNameTranslation(translation);
        }

        return townName;
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

    }

	@Override
	protected void setBreadCrumbs() {
		if (town.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public String getRegionFilter() {
		return regionFilter;
	}

	public void setRegionFilter(String regionFilter) {
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

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
    public void setRegionService(RegionService regionService) {
        this.regionService = regionService;
    }

	@Required
    public void setTownTypeService(TownTypeService townTypeService) {
        this.townTypeService = townTypeService;
    }

	@Required
    public void setTownService(TownService townService) {
        this.townService = townService;
    }

}
