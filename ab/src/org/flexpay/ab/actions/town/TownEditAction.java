package org.flexpay.ab.actions.town;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

/**
 * Town simple editor
 */
public class TownEditAction extends FPActionSupport {

	private Town town = new Town();
	private Long countryFilter;
    private Long regionFilter;
    private TownTypeFilter townTypeFilter = new TownTypeFilter();
    private BeginDateFilter beginDateFilter = new BeginDateFilter();
    private Map<Long, String> names = treeMap();

	private String crumbCreateKey;
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
	@Override
    protected String doExecute() throws Exception {

		if (town == null || town.getId() == null) {
			log.debug("Incorrect town id");
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		if (town.isNotNew()) {
			Stub<Town> stub = stub(town);
			town = townService.readWithHierarchy(stub);

			if (town == null) {
				log.debug("Can't get town with id {} from DB", stub.getId());
				addActionError(getText("common.object_not_selected"));
				return REDIRECT_ERROR;
			} else if (town.isNotActive()) {
				log.debug("Town with id {} is disabled", stub.getId());
				addActionError(getText("common.object_not_selected"));
				return REDIRECT_ERROR;
			}

		}

		if (names == null) {
			log.debug("Incorrect \"names\" parameter");
			names = treeMap();
		}

        initFilters();

        if (isSubmit()) {
            if (!doValidate()) {
                return INPUT;
            }
            updateTown();

			addActionMessage(getText("ab.town.saved"));

            return REDIRECT_SUCCESS;
        }

        initData();

		if (town.isNotNew()) {
			regionFilter = town.getRegion().getId();
			countryFilter = town.getCountry().getId();
		}

        return INPUT;
    }

    private boolean doValidate() {

		if (regionFilter == null || regionFilter <= 0) {
			log.debug("Incorrect region id in filter ({})", regionFilter);
			addActionError(getText("ab.error.town.no_region"));
		}

        if (townTypeFilter == null || !townTypeFilter.needFilter()) {
			log.debug("Incorrect townTypeFilter value");
            addActionError(getText("ab.error.town.no_type"));
        }

        if (beginDateFilter == null || !beginDateFilter.needFilter()) {
			log.debug("Incorrect beginDateFilter value");
            addActionError(getText("ab.error.town.no_begin_date"));
        }

        return !hasActionErrors();
    }

    /*
    * Creates new town if it is a new one (haven't been yet persisted) or updates persistent one
    */
    private void updateTown() throws FlexPayExceptionContainer {

        TownName townName = getTownName();
        town.setNameForDate(townName, beginDateFilter.getDate());
        town.setTypeForDate(new TownType(townTypeFilter.getSelectedId()), beginDateFilter.getDate());

        // setup region for new object
        if (town.isNew()) {
            town.setRegion(new Region(regionFilter));
            townService.create(town);
        } else {
            townService.update(town);
        }
    }

    private TownName getTownName() {

        TownName townName = new TownName();
        for (Map.Entry<Long, String> name : names.entrySet()) {
            String value = name.getValue();
            Language lang = getLang(name.getKey());
            townName.setTranslation(new TownNameTranslation(value, lang));
        }

        return townName;
    }

	private void initFilters() throws Exception {
		townTypeFilter = townTypeService.initFilter(townTypeFilter, getUserPreferences().getLocale());
	}

	private void initData() {
		TownNameTemporal temporal = town.getCurrentNameTemporal();
		beginDateFilter.setDate(temporal != null ? temporal.getBegin() : DateUtil.now());

		TownTypeTemporal tmprlType = town.getCurrentTypeTemporal();
		if (tmprlType != null) {
			townTypeFilter.setSelectedId(tmprlType.getValue().getId());
		}

		TownName townName = temporal != null ? temporal.getValue() : null;
		if (townName != null) {
			for (TownNameTranslation name : townName.getTranslations()) {
				names.put(name.getLang().getId(), name.getName());
			}
		}

		for (Language lang : org.flexpay.common.util.config.ApplicationConfig.getLanguages()) {
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
		}

	}

    /**
     * Get default error execution result
     * <p/>
     * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
     *
     * @return {@link #ERROR} by default
     */
    @NotNull
	@Override
    protected String getErrorResult() {
        return INPUT;
    }

	@Override
	protected void setBreadCrumbs() {
		if (town != null && town.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public Long getCountryFilter() {
		return countryFilter;
	}

	public Long getRegionFilter() {
		return regionFilter;
	}

	public void setRegionFilter(Long regionFilter) {
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
    public void setTownTypeService(TownTypeService townTypeService) {
        this.townTypeService = townTypeService;
    }

	@Required
    public void setTownService(TownService townService) {
        this.townService = townService;
    }

}
