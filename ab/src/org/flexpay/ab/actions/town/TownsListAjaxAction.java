package org.flexpay.ab.actions.town;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class TownsListAjaxAction extends FPActionWithPagerSupport<Town> {

	private String regionId;
	private List<Town> towns = list();

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

		towns = townService.findSimple(new Stub<Region>(regionIdLong), getPager());
		log.info("Total towns found: {}", towns);

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

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

}
