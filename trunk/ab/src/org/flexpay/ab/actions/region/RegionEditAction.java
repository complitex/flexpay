package org.flexpay.ab.actions.region;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

/**
 * Region simple editor
 */
public class RegionEditAction extends FPActionSupport {

	private Region region = new Region();
    private Long countryFilter;
    private BeginDateFilter beginDateFilter = new BeginDateFilter();
    private Map<Long, String> names = treeMap();

	private String crumbCreateKey;
    private RegionService regionService;

    @NotNull
	@Override
    protected String doExecute() throws Exception {

		if (region.getId() == null) {
			log.debug("Region id not set");
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

        region = region.isNew() ? region : regionService.readFull(stub(region));
		if (region == null) {
			log.debug("Region is null");
			addActionError(getText("common.object_not_selected"));
			return INPUT;
		}

        if (isSubmit()) {
            if (!doValidate()) {
                return INPUT;
            }
            updateRegion();

			addActionMessage(getText("ab.region.saved"));

            return REDIRECT_SUCCESS;
        }

        initData();
        return INPUT;
    }

    private boolean doValidate() {

		if (countryFilter == null || countryFilter <= 0) {
			log.debug("Incorrect country id in filter ({})", countryFilter);
			addActionError(getText("ab.error.region.no_country"));
		}

        if (beginDateFilter == null || !beginDateFilter.needFilter()) {
			log.debug("Incorrect BeginDateFilter value");
            addActionError(getText("ab.error.region.no_begin_date"));
        }

        return !hasActionErrors();
    }

    /*
    * Creates new region if it is a new one (haven't been yet persisted) or updates persistent one
    */
    private void updateRegion() throws FlexPayExceptionContainer {

		RegionName regionName = new RegionName();
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			regionName.setTranslation(new RegionNameTranslation(value, lang));
		}

        region.setNameForDate(regionName, beginDateFilter.getDate());

        // setup region for new object
        if (region.isNew()) {
            region.setParent(new Country(countryFilter));
            regionService.create(region);
        } else {
            regionService.update(region);
        }
    }

	private void initData() {
		RegionNameTemporal temporal = region.getCurrentNameTemporal();
		beginDateFilter.setDate(temporal != null ? temporal.getBegin() : DateUtil.now());

		RegionName regionName = temporal != null ? temporal.getValue() : null;
		if (regionName != null) {
			for (RegionNameTranslation name : regionName.getTranslations()) {
				names.put(name.getLang().getId(), name.getName());
			}
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
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
		if (region != null && region.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public Long getCountryFilter() {
		return countryFilter;
	}

	public void setCountryFilter(Long countryFilter) {
		this.countryFilter = countryFilter;
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

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
    public void setRegionService(RegionService regionService) {
        this.regionService = regionService;
    }

}
