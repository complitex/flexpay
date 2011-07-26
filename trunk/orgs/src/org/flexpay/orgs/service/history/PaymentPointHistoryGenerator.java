package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class PaymentPointHistoryGenerator implements HistoryGenerator<PaymentPoint> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private PaymentPointService paymentPointService;
	private PaymentPointHistoryBuilder historyBuilder;
	private PaymentPointReferencesHistoryGenerator referencesHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
    @Override
	public void generateFor(@NotNull PaymentPoint obj) {

		PaymentPoint point = paymentPointService.read(stub(obj));
		if (point == null) {
			log.warn("Requested payment point history generation, but not found: {}", obj.getId());
			return;
		}

		generateForSingle(point);
	}

	private void generateForSingle(PaymentPoint point) {
		referencesHistoryGenerator.generateReferencesHistory(point);

		if (!diffService.hasDiffs(point)) {
			Diff diff = historyBuilder.diff(null, point);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		}
	}

	@Override
	public void generateFor(@NotNull Collection<PaymentPoint> objs) {
		List<PaymentPoint> points = paymentPointService.readFull(DomainObject.collectionIds(objs), false);
		for (PaymentPoint point : points) {
			generateForSingle(point);
		}
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
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

	@Required
	public void setReferencesHistoryGenerator(PaymentPointReferencesHistoryGenerator referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}
}
