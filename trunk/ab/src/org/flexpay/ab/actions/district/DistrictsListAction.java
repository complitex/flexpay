package org.flexpay.ab.actions.district;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.persistence.sorter.DistrictSorter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DistrictsListAction extends FPActionWithPagerSupport<District> {

	private Long townFilter;
	private DistrictSorter districtSorter = new DistrictSorter();
	private List<District> districts = list();

	private DistrictService districtService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (districtSorter == null) {
			log.debug("DistrictSorter is null");
			districtSorter = new DistrictSorter();
		}

		if (!doValidate()) {
			return SUCCESS;
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

		boolean valid = true;

		if (townFilter == null || townFilter <= 0) {
			log.warn("Incorrect town id in filter ({})", townFilter);
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

}
