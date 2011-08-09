package org.flexpay.ab.action.town;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

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
	private RegionService regionService;

    @NotNull
	@Override
    protected String doExecute() throws Exception {

		if (town == null || town.getId() == null) {
			log.warn("Incorrect town id");
			addActionError(getText("ab.error.town.incorrect_town_id"));
			return REDIRECT_ERROR;
		}

		if (town.isNotNew()) {
			Stub<Town> stub = stub(town);
			town = townService.readWithHierarchy(stub);
			if (town == null) {
				log.warn("Can't get town with id {} from DB", stub.getId());
				addActionError(getText("ab.error.town.cant_get_town"));
				return REDIRECT_ERROR;
			} else if (town.isNotActive()) {
				log.warn("Town with id {} is disabled", stub.getId());
				addActionError(getText("ab.error.town.cant_get_town"));
				return REDIRECT_ERROR;
			}

		}

		correctNames();
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

    private boolean doValidate() throws Exception {

		if (regionFilter == null || regionFilter <= 0) {
			log.warn("Incorrect region id in filter ({})", regionFilter);
			addActionError(getText("ab.error.region.incorrect_region_id"));
			regionFilter = 0L;
		} else if (town.isNew()) {
			Region region = regionService.readFull(new Stub<Region>(regionFilter));
			if (region == null) {
				log.warn("Can't get region with id {} from DB", regionFilter);
				addActionError(getText("ab.error.region.cant_get_region"));
				regionFilter = 0L;
			} else if (region.isNotActive()) {
				log.warn("Region with id {} is disabled", regionFilter);
				addActionError(getText("ab.error.region.cant_get_region"));
				regionFilter = 0L;
			}
		}

        if (!townTypeFilter.needFilter()) {
			log.warn("Incorrect townTypeFilter value {}", townTypeFilter.getSelectedId());
            addActionError(getText("ab.error.town_type.incorrect_town_type_id"));
		} else {
			TownType townType = townTypeService.readFull(new Stub<TownType>(townTypeFilter.getSelectedId()));
			if (townType == null) {
				log.warn("Can't get town type with id {} from DB", townTypeFilter.getSelectedId());
				addActionError(getText("ab.error.town_type.cant_get_town_type"));
				townTypeFilter.setSelectedId(0L);
			} else if (townType.isNotActive()) {
				log.warn("Town type with id {} is disabled", townTypeFilter.getSelectedId());
				addActionError(getText("ab.error.town_type.cant_get_town_type"));
				townTypeFilter.setSelectedId(0L);
			}
		}

        if (!beginDateFilter.needFilter()) {
            log.warn("Incorrect begin date in filter ({})", beginDateFilter);
            addActionError(getText("ab.error.town.no_begin_date"));
        }

        return !hasActionErrors();
    }

    /**
     * Creates new town if it is a new one
	 * (haven't been yet persisted) or updates persistent one
	 *
	 * @throws FlexPayExceptionContainer if some errors
     */
    private void updateTown() throws FlexPayExceptionContainer {

		TownName townName = new TownName();
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			townName.setTranslation(new TownNameTranslation(value, lang));
		}

        town.setNameForDate(townName, beginDateFilter.getDate());
        town.setTypeForDate(new TownType(townTypeFilter.getSelectedId()), beginDateFilter.getDate());
        town.setNameDate(beginDateFilter.getDate());

        if (town.isNew()) {
            town.setRegion(new Region(regionFilter));
            townService.create(town);
        } else {
            townService.update(town);
        }
    }

	private void initFilters() throws Exception {

		if (beginDateFilter == null) {
			log.warn("BeginDateFilter parameter is null");
			beginDateFilter = new BeginDateFilter();
		}

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

		for (Language lang : getLanguages()) {
			if (!names.containsKey(lang.getId())) {
				names.put(lang.getId(), "");
			}
		}

	}

	private void correctNames() {
		if (names == null) {
			log.warn("Names parameter is null");
			names = treeMap();
		}
		Map<Long, String> newNames = treeMap();
		for (Language lang : getLanguages()) {
			newNames.put(lang.getId(), names.containsKey(lang.getId()) ? names.get(lang.getId()) : "");
		}
		names = newNames;
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

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

}
