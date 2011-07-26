package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.builder.ReferenceExtractor;
import org.flexpay.common.persistence.history.builder.ReferencePatcher;
import org.flexpay.common.persistence.history.builder.TranslationExtractor;
import org.flexpay.common.persistence.history.builder.TranslationPatcher;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.CashboxNameTranslation;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CashboxHistoryBuilder extends HistoryBuilderBase<Cashbox> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final int FIELD_NAME = 1;
	public static final int FIELD_PAYMENT_POINT_ID = 2;

	private PaymentPointService pointService;

	/**
	 * Build necessary diff records
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
    @Override
	protected void doDiff(@Nullable Cashbox t1, @NotNull Cashbox t2, @NotNull Diff diff) {

		log.debug("creating new cashboxes diff");
		if (!t2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		Cashbox old = t1 == null ? new Cashbox() : t1;

		buildNameDiff(old, t2, diff);
		buildPaymentPointRefDiff(old, t2, diff);
	}

	private void buildNameDiff(Cashbox p1, Cashbox p2, Diff diff) {

		builderHelper.buildTranslationDiff(p1, p2, diff, new TranslationExtractor<Translation, Cashbox>() {

            @Override
			public Translation getTranslation(Cashbox obj, @NotNull Language language) {
				return obj.getTranslation(language);
			}

            @Override
			public int getTranslationField() {
				return FIELD_NAME;
			}
		});
	}

	private void buildPaymentPointRefDiff(Cashbox p1, Cashbox p2, Diff diff) {

		builderHelper.buildReferenceDiff(p1, p2, diff, new ReferenceExtractor<PaymentPoint, Cashbox>() {

            @Override
			public PaymentPoint getReference(Cashbox obj) {
				return obj.getPaymentPoint();
			}

            @Override
			public int getReferenceField() {
				return FIELD_PAYMENT_POINT_ID;
			}
		});
	}

	/**
	 * Apply diff to an object
	 *
	 * @param cashbox Object to apply diff to
	 * @param diff	Diff to apply
	 */
    @Override
	public void patch(@NotNull Cashbox cashbox, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(cashbox, record);
					break;
				case FIELD_PAYMENT_POINT_ID:
					patchPaymentPointReference(cashbox, record);
					break;
				default:
					log.info("Unsupported record: {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}
	}

	private void patchName(Cashbox cashbox, HistoryRecord record) {

		builderHelper.patchTranslation(cashbox, record, new TranslationPatcher<CashboxNameTranslation, Cashbox>() {
            @Override
			public CashboxNameTranslation getNotNullTranslation(Cashbox obj, @NotNull Language language) {
				CashboxNameTranslation tr = obj.getTranslation(language);
				return tr == null ? new CashboxNameTranslation() : tr;
			}

            @Override
			public void setTranslation(Cashbox obj, CashboxNameTranslation tr, String name) {
				tr.setName(name);
				obj.setName(tr);
			}
		});
	}

	private void patchPaymentPointReference(@NotNull Cashbox cashbox, @NotNull HistoryRecord record) {
		log.debug("Patching payment point reference {}", record);

		builderHelper.patchReference(cashbox, record, new ReferencePatcher<PaymentPoint, Cashbox>() {
            @Override
			public Class<PaymentPoint> getType() {
				return PaymentPoint.class;
			}

            @Override
			public void setReference(Cashbox obj, Stub<PaymentPoint> ref) {
				PaymentPoint point = pointService.read(ref);
				obj.setPaymentPoint(point);
			}
		});
	}

	@Required
	public void setPointService(PaymentPointService pointService) {
		this.pointService = pointService;
	}
}
