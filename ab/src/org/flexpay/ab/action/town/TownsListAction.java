package org.flexpay.ab.action.town;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.sorter.TownSorterByName;
import org.flexpay.ab.persistence.sorter.TownSorterByType;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;

public class TownsListAction extends FPActionWithPagerSupport<Town> {

	private Long regionFilter;
	private List<Town> towns = list();
	private TownSorterByName townSorterByName = new TownSorterByName();
	private TownSorterByType townSorterByType = new TownSorterByType();

	private TownService townService;
	private RegionService regionService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (!doValidate()) {
			return SUCCESS;
		}

		if (townSorterByName == null) {
			log.warn("TownSorterByName is null");
			townSorterByName = new TownSorterByName();
		}
		if (townSorterByType == null) {
			log.warn("TownSorterByType is null");
			townSorterByType = new TownSorterByType();
		}

		townSorterByName.setLang(getLanguage());
		townSorterByType.setLang(getLanguage());

		towns = townService.find(arrayStack(new RegionFilter(regionFilter)), list(townSorterByName, townSorterByType), getPager());
		if (log.isDebugEnabled()) {
			log.debug("Total towns found: {}", towns.size());
		}

		towns = townService.readFull(collectionIds(towns), true);
		if (log.isDebugEnabled()) {
			log.debug("Total full towns found: {}", towns.size());
		}

		return SUCCESS;
	}

	private boolean doValidate() {

		if (regionFilter == null || regionFilter <= 0) {
			log.warn("Incorrect region id in filter ({})", regionFilter);
			addActionError(getText("ab.error.region.incorrect_region_id"));
			regionFilter = 0L;
		} else {
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

		return !hasActionErrors();
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
		return ERROR;
	}

	public void setRegionFilter(Long regionFilter) {
		this.regionFilter = regionFilter;
	}

	public List<Town> getTowns() {
		return towns;
	}

	public TownSorterByName getTownSorterByName() {
		return townSorterByName;
	}

	public void setTownSorterByName(TownSorterByName townSorterByName) {
		this.townSorterByName = townSorterByName;
	}

	public TownSorterByType getTownSorterByType() {
		return townSorterByType;
	}

	public void setTownSorterByType(TownSorterByType townSorterByType) {
		this.townSorterByType = townSorterByType;
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
