package org.flexpay.orgs.actions.paymentscollector;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class PaymentsCollectorDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = set();

	private PaymentsCollectorService collectorService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		collectorService.disable(objectIds);

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

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setCollectorService(PaymentsCollectorService collectorService) {
		this.collectorService = collectorService;
	}

}
