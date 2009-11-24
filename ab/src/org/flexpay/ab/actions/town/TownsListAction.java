package org.flexpay.ab.actions.town;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.sorter.TownSorterByName;
import org.flexpay.ab.persistence.sorter.TownSorterByType;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class TownsListAction extends FPActionWithPagerSupport<Town> {

	private Long regionFilter;
	private List<Town> towns = list();

	private TownSorterByName townSorterByName = new TownSorterByName();
	private TownSorterByType townSorterByType = new TownSorterByType();
	private TownService townService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (townSorterByName == null) {
			log.debug("TownSorterByName is null");
			townSorterByName = new TownSorterByName();
		}
		if (townSorterByType == null) {
			log.debug("TownSorterByType is null");
			townSorterByType = new TownSorterByType();
		}

		if (!doValidate()) {
			return SUCCESS;
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

		boolean valid = true;

		if (regionFilter == null || regionFilter <= 0) {
			log.debug("Incorrect region id in filter ({})", regionFilter);
			valid = false;
		}

		return valid;
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

}
