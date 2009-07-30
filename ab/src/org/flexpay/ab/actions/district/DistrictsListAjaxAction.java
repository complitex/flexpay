package org.flexpay.ab.actions.district;

import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DistrictsListAjaxAction extends FPActionWithPagerSupport<DistrictName> {

	private String townId;
	private List<DistrictName> districtsNames = list();

	private DistrictService districtService;

	@Override
	@NotNull
	public String doExecute() throws Exception {

		Long townIdLong;

		try {
			townIdLong = Long.parseLong(townId);
		} catch (Exception e) {
			log.warn("Incorrect town id in filter ({})", townId);
			return SUCCESS;
		}

		districtsNames = districtService.findNames(new Stub<Town>(townIdLong), getPager());
		log.info("Total districts found: {}", districtsNames);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@Override
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setTownId(String townId) {
		this.townId = townId;
	}

	public List<DistrictName> getDistrictsNames() {
		return districtsNames;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

}
