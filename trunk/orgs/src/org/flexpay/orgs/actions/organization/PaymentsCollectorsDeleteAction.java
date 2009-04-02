package org.flexpay.orgs.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class PaymentsCollectorsDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = CollectionUtils.set();

	private PaymentsCollectorService collectorService;

	@NotNull
	public String doExecute() throws Exception {
		collectorService.disable(objectIds);

		return REDIRECT_SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setCollectorService(PaymentsCollectorService collectorService) {
		this.collectorService = collectorService;
	}

}
