package org.flexpay.ab.action.district;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.persistence.sorter.DistrictSorter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;

public class DistrictsListAction extends FPActionWithPagerSupport<District> {

	private Long townFilter;
	private DistrictSorter districtSorter = new DistrictSorter();
	private List<District> districts = list();

	private DistrictService districtService;
	private TownService townService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (!doValidate()) {
			return SUCCESS;
		}

		if (districtSorter == null) {
			log.warn("DistrictSorter is null");
			districtSorter = new DistrictSorter();
		}

		districtSorter.setLang(getLanguage());

		districts = districtService.find(arrayStack(new TownFilter(townFilter)), list(districtSorter), getPager());
		if (log.isDebugEnabled()) {
			log.debug("Total districts found: {}", districts.size());
		}

		districts = districtService.readFull(collectionIds(districts), true);
		if (log.isDebugEnabled()) {
			log.debug("Total full districts found: {}", districts.size());
		}

		return SUCCESS;
	}

	private boolean doValidate() {

		if (townFilter == null || townFilter <= 0) {
			log.warn("Incorrect town id in filter ({})", townFilter);
			addActionError(getText("ab.error.town.incorrect_town_id"));
			townFilter = 0L;
		} else {
			Town town = townService.readFull(new Stub<Town>(townFilter));
			if (town == null) {
				log.warn("Can't get town with id {} from DB", townFilter);
				addActionError(getText("ab.error.town.cant_get_town"));
				townFilter = 0L;
			} else if (town.isNotActive()) {
				log.warn("Town with id {} is disabled", townFilter);
				addActionError(getText("ab.error.town.cant_get_town"));
				townFilter = 0L;
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
		return SUCCESS;
	}

	public void setTownFilter(Long townFilter) {
		this.townFilter = townFilter;
	}

	public List<District> getDistricts() {
		return districts;
	}

	public DistrictSorter getDistrictSorter() {
		return districtSorter;
	}

	public void setDistrictSorter(DistrictSorter districtSorter) {
		this.districtSorter = districtSorter;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}
}
