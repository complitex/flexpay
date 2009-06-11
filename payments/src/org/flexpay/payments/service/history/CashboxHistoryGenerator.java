package org.flexpay.payments.service.history;

import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.payments.persistence.Cashbox;
import org.flexpay.payments.service.CashboxService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CashboxHistoryGenerator implements HistoryGenerator<Cashbox> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private CashboxHistoryBuilder historyBuilder;
	private CashboxService cashboxService;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Cashbox obj) {

		if (diffService.hasDiffs(obj)) {
			log.debug("Cashbox already has history, do nothing {}", obj);
			return;
		}

		Cashbox cashbox = cashboxService.read(stub(obj));
		if (cashbox == null) {
			log.warn("Requested cashbox history generation, but not found: {}", obj);
			return;
		}

		Diff diff = historyBuilder.diff(null, cashbox);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(CashboxHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}
}
