package org.flexpay.ab.actions.region;

import org.flexpay.ab.service.RegionService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class RegionDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = set();

	private RegionService regionService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		regionService.disableByIds(objectIds);

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

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

}
