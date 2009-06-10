package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.ProcessingStatus;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.DiffService;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.PaymentPointService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PaymentPointHistoryGenerator implements HistoryGenerator<PaymentPoint> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private PaymentPointService paymentPointService;
	private PaymentPointHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull PaymentPoint obj) {

		if (diffService.hasDiffs(obj)) {
			log.debug("Identity type already has history, do nothing {}", obj);
			return;
		}

		PaymentPoint org = paymentPointService.read(stub(obj));
		if (org == null) {
			log.warn("Requested payment point history generation, but not found: {}", obj);
			return;
		}

		Diff diff = historyBuilder.diff(null, org);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);

	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setHistoryBuilder(PaymentPointHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
