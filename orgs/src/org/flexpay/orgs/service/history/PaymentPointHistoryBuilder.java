package org.flexpay.orgs.service.history;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.EqualsHelper;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentPointName;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class PaymentPointHistoryBuilder extends HistoryBuilderBase<PaymentPoint> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final int FIELD_NAME = 1;
	public static final int FIELD_PAYMENT_COLLECTOR_ID = 2;
	public static final int FIELD_ADDRESS = 3;
	public static final int FIELD_EMAIL = 4;

	private PaymentCollectorService collectorService;

	/**
	 * Build necessary diff records
	 *
	 * @param p1   First object
	 * @param p2   Second object
	 * @param diff Diff object
	 */
    @Override
	protected void doDiff(@Nullable PaymentPoint p1, @NotNull PaymentPoint p2, @NotNull Diff diff) {

		log.debug("creating new payment points diff");
		if (!p2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		PaymentPoint old = p1 == null ? new PaymentPoint() : p1;

		buildSimpleFieldsDiff(old, p2, diff);
		buildNameDiff(old, p2, diff);
		buildCollectorRefDiff(old, p2, diff);
	}

	private boolean differ(String s1, String s2) {
		return !StringUtils.trimToEmpty(s1).equals(StringUtils.trimToEmpty(s2));
	}

	private HistoryRecord newRecord(int fieldType) {
		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(fieldType);
		return rec;
	}

	private void buildSimpleFieldsDiff(PaymentPoint p1, PaymentPoint p2, Diff diff) {

		if (differ(p1.getAddress(), p2.getAddress())) {
			HistoryRecord rec = newRecord(FIELD_ADDRESS);
			rec.setOldStringValue(p1.getAddress());
			rec.setNewStringValue(p2.getAddress());
			diff.addRecord(rec);
			log.debug("Added address diff record {}", rec);
		}
	}
	
	private void buildNameDiff(PaymentPoint p1, PaymentPoint p2, Diff diff) {
		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			PaymentPointName tr1 = p1.getTranslation(lang);
			PaymentPointName tr2 = p2.getTranslation(lang);

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

	private void buildCollectorRefDiff(PaymentPoint p1, PaymentPoint p2, Diff diff) {
		PaymentCollector org1 = p1.getCollector();
		PaymentCollector org2 = p2.getCollector();
		boolean noOrganization = (org1 == null || org1.isNew()) && (org2 == null || org2.isNew());

		// no organization found in both objects, nothing to do
		if (noOrganization) {
			return;
		}

		boolean sameOrganization = org1 != null && org1.isNotNew() &&
								   org2 != null && org2.isNotNew() &&
								   org1.equals(org2);
		// same parent found in both objects, nothing to do
		if (sameOrganization) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_PAYMENT_COLLECTOR_ID);
		rec.setOldStringValue(org1 == null || org1.isNew() ? null : masterIndexService.getMasterIndex(org1));
		rec.setNewStringValue(org2 == null || org2.isNew() ? null : masterIndexService.getMasterIndex(org2));
		diff.addRecord(rec);
		log.debug("Added payment collector ref diff record: {}", rec);
	}

	/**
	 * Apply diff to an object
	 *
	 * @param paymentPoint Payment point object to apply diff to
	 * @param diff Diff to apply
	 */
    @Override
	public void patch(@NotNull PaymentPoint paymentPoint, @NotNull Diff diff) {
		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(paymentPoint, record);
					break;
				case FIELD_EMAIL:
					paymentPoint.setAddress(record.getNewStringValue());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				case FIELD_ADDRESS:
					paymentPoint.setAddress(record.getNewStringValue());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				case FIELD_PAYMENT_COLLECTOR_ID:
					patchCollectorReference(paymentPoint, record);
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				default:
					log.info("Unsupported record: {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}
	}

	private void patchName(PaymentPoint pp, HistoryRecord record) {
		Language lang = record.getLang();
		if (lang == null) {
			log.info("No lang found for record {}", record);
			record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			return;
		}

		PaymentPointName tr = pp.getTranslation(lang);

		if (tr == null) {
			tr = new PaymentPointName();
			tr.setLang(lang);
		}

		tr.setName(record.getNewStringValueNotNull());
		pp.setName(tr);
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchCollectorReference(@NotNull PaymentPoint pp, @NotNull HistoryRecord record) {
		log.debug("Patching collector reference {}", record);

		if (record.getNewStringValue() != null) {
			String externalId = record.getNewStringValue();
			Stub<PaymentCollector> stub = correctionsService.findCorrection(
					externalId, PaymentCollector.class, masterIndexService.getMasterSourceDescriptionStub());
			if (stub == null) {
				throw new IllegalStateException("Cannot find collector by master index: " + externalId);
			}
			PaymentCollector collector = collectorService.read(stub);
			pp.setCollector(collector);
		}

		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	@Required
	public void setCollectorService(PaymentCollectorService collectorService) {
		this.collectorService = collectorService;
	}
}
