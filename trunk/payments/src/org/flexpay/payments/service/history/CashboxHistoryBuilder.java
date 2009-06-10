package org.flexpay.payments.service.history;

import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.EqualsHelper;
import org.flexpay.payments.persistence.Cashbox;
import org.flexpay.payments.persistence.CashboxNameTranslation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			CashboxNameTranslation tr1 = p1.getTranslation(lang);
			CashboxNameTranslation tr2 = p2.getTranslation(lang);

			// no translation, check other languages
			if (tr1 == null && tr2 == null) {
				continue;
			}

			boolean nameDiffer = !EqualsHelper.strEquals(
					tr1 == null ? null : tr1.getName(),
					tr2 == null ? null : tr2.getName());

			if (nameDiffer) {
				HistoryRecord rec = new HistoryRecord();
				rec.setFieldType(FIELD_NAME);
				rec.setOldStringValue(tr1 == null ? null : tr1.getName());
				rec.setNewStringValue(tr2 == null ? null : tr2.getName());
				rec.setLanguage(lang.getLangIsoCode());
				diff.addRecord(rec);

				log.debug("Added name diff for lang {}\n{}", lang, rec);
			}
		}
	}

	private void buildPaymentPointRefDiff(Cashbox p1, Cashbox p2, Diff diff) {
		PaymentPoint pp1 = p1.getPaymentPoint();
		PaymentPoint pp2 = p2.getPaymentPoint();
		boolean noOrganization = (pp1 == null || pp1.isNew()) && (pp2 == null || pp2.isNew());

		// no organization found in both objects, nothing to do
		if (noOrganization) {
			return;
		}

		boolean sameOrganization = pp1 != null && pp1.isNotNew() &&
								   pp2 != null && pp2.isNotNew() &&
								   pp1.equals(pp2);
		// same parent found in both objects, nothing to do
		if (sameOrganization) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_PAYMENT_POINT_ID);
		rec.setOldStringValue(pp1 == null || pp1.isNew() ? null : masterIndexService.getMasterIndex(pp1));
		rec.setNewStringValue(pp2 == null || pp2.isNew() ? null : masterIndexService.getMasterIndex(pp2));
		diff.addRecord(rec);
		log.debug("Added payment pint ref diff record: {}", rec);
	}

	/**
	 * Apply diff to an object
	 *
	 * @param t	Object to apply diff to
	 * @param diff Diff to apply
	 */
	public void patch(@NotNull Cashbox cashbox, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(cashbox, record);
					break;
				case FIELD_PAYMENT_POINT_ID:
					patchPaymentPointReference(cashbox, record);
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				default:
					log.info("Unsupported record: {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}
	}

	private void patchName(Cashbox cashbox, HistoryRecord record) {
		Language lang = record.getLang();
		if (lang == null) {
			log.info("No lang found for record {}", record);
			record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			return;
		}

		CashboxNameTranslation tr = cashbox.getTranslation(lang);

		if (tr == null) {
			tr = new CashboxNameTranslation();
			tr.setLang(lang);
		}

		tr.setName(record.getNewStringValueNotNull());
		cashbox.setName(tr);
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchPaymentPointReference(@NotNull Cashbox cashbox, @NotNull HistoryRecord record) {
		log.debug("Patching payment point reference {}", record);

		if (record.getNewStringValue() != null) {
			String externalId = record.getNewStringValue();
			Stub<PaymentPoint> stub = correctionsService.findCorrection(
					externalId, PaymentPoint.class, masterIndexService.getMasterSourceDescription());
			if (stub == null) {
				throw new IllegalStateException("Cannot find payment point by master index: " + externalId);
			}
			cashbox.setPaymentPoint(new PaymentPoint(stub));
		}

		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}
}
