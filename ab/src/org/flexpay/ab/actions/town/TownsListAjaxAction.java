package org.flexpay.ab.actions.town;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.sorter.TownSorterByName;
import org.flexpay.ab.persistence.sorter.TownSorterByType;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class TownsListAjaxAction extends FPActionWithPagerSupport<Town> {

	private String regionId;
	private List<Town> towns = list();

	private TownSorterByName townSorterByName = new TownSorterByName();
	private TownSorterByType townSorterByType = new TownSorterByType();
	private TownService townService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		Long regionIdLong = null;
		if (StringUtils.isNotBlank(regionId)) {
			try {
				regionIdLong = Long.parseLong(regionId);
			} catch (Exception e) {
				log.warn("Incorrect region id in filter ({})", regionId);
				return SUCCESS;
			}
		}

		townSorterByName.setLang(getLanguage());
		townSorterByType.setLang(getLanguage());

		ArrayStack filters = CollectionUtils.arrayStack(new RegionFilter(regionIdLong));
		List<ObjectSorter> sorters = CollectionUtils.<ObjectSorter>list(townSorterByName, townSorterByType);
		List<Town> townStubs = townService.find(filters, sorters, getPager());

		if (log.isDebugEnabled()) {
			log.info("Total towns found: {}", towns.size());
		}

		towns = townService.readFull(DomainObject.collectionIds(townStubs), true);


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
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
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
