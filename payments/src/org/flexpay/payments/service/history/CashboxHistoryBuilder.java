package org.flexpay.payments.service.history;

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
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.persistence.Cashbox;
import org.flexpay.payments.persistence.CashboxNameTranslation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CashboxHistoryBuilder extends HistoryBuilderBase<Cashbox> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final int FIELD_NAME = 1;
	public static final int FIELD_PAYMENT_POINT_ID = 2;

	/**
	 * Build necessary diff records
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
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

			public Translation getTranslation(Cashbox obj, @NotNull Language language) {
				return obj.getTranslation(language);
			}

			public int getTranslationField() {
				return FIELD_NAME;
			}
		});
	}

	private void buildPaymentPointRefDiff(Cashbox p1, Cashbox p2, Diff diff) {

		builderHelper.buildReferenceDiff(p1, p2, diff, new ReferenceExtractor<PaymentPoint, Cashbox>() {

			public PaymentPoint getReference(Cashbox obj) {
				return obj.getPaymentPoint();
			}

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
			public CashboxNameTranslation getNotNullTranslation(Cashbox obj, @NotNull Language language) {
				CashboxNameTranslation tr = obj.getTranslation(language);
				return tr == null ? new CashboxNameTranslation() : tr;
			}

			public void setTranslation(Cashbox obj, CashboxNameTranslation tr, String name) {
				tr.setName(name);
				obj.setName(tr);
			}
		});
	}

	private void patchPaymentPointReference(@NotNull Cashbox cashbox, @NotNull HistoryRecord record) {
		log.debug("Patching payment point reference {}", record);

		builderHelper.patchReference(cashbox, record, new ReferencePatcher<PaymentPoint, Cashbox>() {
			public Class<PaymentPoint> getType() {
				return PaymentPoint.class;
			}

			public void setReference(Cashbox obj, Stub<PaymentPoint> ref) {
				obj.setPaymentPoint(new PaymentPoint(ref));
			}
		});
	}
}
