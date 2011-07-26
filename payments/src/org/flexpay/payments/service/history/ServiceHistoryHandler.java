package org.flexpay.payments.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ServiceHistoryHandler extends HistoryHandlerBase<Service> {

	private SPService spService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
    @Override
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(Service.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 * @throws Exception if failure occurs
	 */
    @Override
	public void process(@NotNull Diff diff) throws Exception {
		historyHandlerHelper.process(diff, historyBuilder, spService, correctionsService);
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}
}
