package org.flexpay.payments.service.history;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.EqualsHelper;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceTypeNameTranslation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServiceTypeHistoryBuilder extends HistoryBuilderBase<ServiceType> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final int FIELD_NAME = 1;
	public static final int FIELD_CODE = 2;

	/**
	 * Build necessary diff records
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
	protected void doDiff(@Nullable ServiceType t1, @NotNull ServiceType t2, @NotNull Diff diff) {

		log.debug("creating new service types diff");
		if (!t2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		ServiceType old = t1 == null ? new ServiceType() : t1;

		buildNameDiff(old, t2, diff);
		buildCodeDiff(old, t2, diff);
	}

	private void buildCodeDiff(ServiceType t1, ServiceType t2, Diff diff) {

		if (t1.getCode() == t2.getCode()) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_CODE);
		rec.setOldIntValue(t1.getCode());
		rec.setNewIntValue(t2.getCode());
		diff.addRecord(rec);

		log.debug("Code differ: {}", rec);
	}


	private void buildNameDiff(ServiceType t1, ServiceType t2, Diff diff) {
		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			ServiceTypeNameTranslation tr1 = t1.getTranslation(lang);
			ServiceTypeNameTranslation tr2 = t2.getTranslation(lang);

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

	/**
	 * Apply diff to an object
	 *
	 * @param type Object to apply diff to
	 * @param diff Diff to apply
	 */
	public void patch(@NotNull ServiceType type, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(type, record);
					break;
				case FIELD_CODE:
					type.setCode(record.getNewIntValueNotNull());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				default:
					log.info("Unsupported record: {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}
	}

	private void patchName(ServiceType type, HistoryRecord record) {
		Language lang = record.getLang();
		if (lang == null) {
			log.info("No lang found for record {}", record);
			record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			return;
		}

		ServiceTypeNameTranslation tr = type.getTranslation(lang);

		if (tr == null) {
			tr = new ServiceTypeNameTranslation();
			tr.setLang(lang);
		}

		tr.setName(record.getNewStringValueNotNull());
		type.setTypeName(tr);
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}
}
