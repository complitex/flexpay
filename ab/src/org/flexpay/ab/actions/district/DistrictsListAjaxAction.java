package org.flexpay.ab.actions.district;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.persistence.sorter.DistrictSorter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DistrictsListAjaxAction extends FPActionWithPagerSupport<District> {

	private String townId;
	private List<District> districts = list();

	private DistrictSorter districtSorter = new DistrictSorter();
	private DistrictService districtService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		Long townIdLong;

		try {
			townIdLong = Long.parseLong(townId);
		} catch (Exception e) {
			log.warn("Incorrect town id in filter ({})", townId);
			return SUCCESS;
		}

		ArrayStack filters = CollectionUtils.arrayStack(new TownFilter(townIdLong));
		districtSorter.setLang(getLanguage());
		List<ObjectSorter> sorters = CollectionUtils.<ObjectSorter>list(districtSorter);
		List<District> districtStubs = districtService.find(filters, sorters, getPager());

		if (log.isDebugEnabled()) {
			log.debug("Total districts found: {}", districts.size());
		}

		districts = districtService.readFull(DomainObject.collectionIds(districtStubs), true);

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

	public void setTownId(String townId) {
		this.townId = townId;
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
