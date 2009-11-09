package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.EqualsHelper;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class IdentityTypeHistoryBuilder extends HistoryBuilderBase<IdentityType> {

	public static final int FIELD_NAME = 1;
	public static final int FIELD_CODE = 2;

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Build necessary diff records, diff is a set of translation differences
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
	@Override
	protected void doDiff(@Nullable IdentityType t1, @NotNull IdentityType t2, @NotNull Diff diff) {

		if (!t2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			IdentityTypeTranslation tr1 = t1 != null ? t1.getTranslation(lang) : null;
			IdentityTypeTranslation tr2 = t2.getTranslation(lang);

			// no translation, check other languages
			if (tr1 == null && tr2 == null) {
				log.debug("No translations for lang {}", lang);
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
				log.debug("Name differ: {}", rec);
			}

			log.debug("Completed Diff for lang {}", lang);
		}

		buildCodeDiff(t1, t2, diff);
	}

	private void buildCodeDiff(@Nullable IdentityType t1, @NotNull IdentityType t2, @NotNull Diff diff) {

		int c1 = t1 == null ? IdentityType.TYPE_UNKNOWN : t1.getTypeId();
		int c2 = t2.getTypeId();
		if (c1 == c2) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_CODE);
		rec.setOldIntValue(c1);
		rec.setNewIntValue(c2);
		diff.addRecord(rec);

		log.debug("Code differ: {}", rec);
	}

	/**
	 * Apply diff to an object
	 *
	 * @param t	Object to apply diff to
	 * @param diff Diff to apply
	 */
	@Override
	public void patch(@NotNull IdentityType t, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(t, record);
					break;
				case FIELD_CODE:
					Integer code = record.getNewIntValue();
					t.setTypeId(code == null ? IdentityType.TYPE_UNKNOWN : code);
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				default:
					log.info("Unsupported record: {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}
	}

	private void patchName(IdentityType t, HistoryRecord record) {
		Language lang = record.getLang();
		if (lang == null) {
			log.info("No lang found for record {}", record);
			record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			return;
		}

		IdentityTypeTranslation tr = t.getTranslation(lang);

		if (tr == null) {
			tr = new IdentityTypeTranslation();
			tr.setLang(lang);
		}

		tr.setName(record.getNewStringValue());
		t.setTranslation(tr);
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}
}
