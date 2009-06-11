package org.flexpay.payments.service.history;

import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.ProcessingStatus;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.DiffService;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ServiceHistoryGenerator implements HistoryGenerator<Service> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private SPService spService;
	private ServiceHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Service obj) {

		if (diffService.hasDiffs(obj)) {
			log.debug("Cashbox already has history, do nothing {}", obj);
			return;
		}

		Service service = spService.readFull(stub(obj));
		if (service == null) {
			log.warn("Requested service history generation, but not found: {}", obj);
			return;
		}

		Diff diff = historyBuilder.diff(null, service);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setHistoryBuilder(ServiceHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
