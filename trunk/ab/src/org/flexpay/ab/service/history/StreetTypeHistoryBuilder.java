package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
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

public class StreetTypeHistoryBuilder extends HistoryBuilderBase<StreetType> {

	public static final int FIELD_NAME = 1;
	public static final int FIELD_SHORT_NAME = 2;

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Build necessary diff records, diff is a set of translation differences
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
	@Override
	protected void doDiff(@Nullable StreetType t1, @NotNull StreetType t2, @NotNull Diff diff) {

		if (!t2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			StreetTypeTranslation tr1 = t1 != null ? t1.getTranslation(lang) : null;
			StreetTypeTranslation tr2 = t2.getTranslation(lang);

			// no translation, check other languages
			if (tr1 == null && tr2 == null) {
				log.debug("No translations for lang {}", lang);
				continue;
			}

			boolean nameDiffer = !EqualsHelper.strEquals(
					tr1 == null ? null : tr1.getName(),
					tr2 == null ? null : tr2.getName());

			if (nameDiffer) {
				log.debug("Name differ");
				HistoryRecord rec = new HistoryRecord();
				rec.setFieldType(FIELD_NAME);
				rec.setOldStringValue(tr1 == null ? null : tr1.getName());
				rec.setNewStringValue(tr2 == null ? null : tr2.getName());
				rec.setLanguage(lang.getLangIsoCode());
				diff.addRecord(rec);
			}

			boolean shortNameDiffer = !EqualsHelper.strEquals(
					tr1 == null ? null : tr1.getShortName(),
					tr2 == null ? null : tr2.getShortName());

			if (shortNameDiffer) {
				log.debug("Short name differ");
				HistoryRecord rec = new HistoryRecord();
				rec.setFieldType(FIELD_SHORT_NAME);
				rec.setOldStringValue(tr1 == null ? null : tr1.getShortName());
				rec.setNewStringValue(tr2 == null ? null : tr2.getShortName());
				rec.setLanguage(lang.getLangIsoCode());
				diff.addRecord(rec);
			}

			log.debug("Completed Diff for lang {}", lang);
		}
	}

	/**
	 * Apply diff to an object
	 *
	 * @param t	Object to apply diff to
	 * @param diff Diff to apply
	 */
	@Override
	public void patch(@NotNull StreetType t, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			Language lang = record.getLang();
			if (lang == null) {
				log.info("No lang found for record {}", record);
				record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
				continue;
			}

			StreetTypeTranslation tr = t.getTranslation(lang);

			if (tr == null) {
				tr = new StreetTypeTranslation();
				tr.setLang(lang);
			}

			switch (record.getFieldType()) {
				case FIELD_NAME:
					tr.setName(record.getNewStringValue());
					break;
				case FIELD_SHORT_NAME:
					tr.setShortName(record.getNewStringValue());
					break;
				default:
					log.info("Unsupported record: {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
					continue;
			}

			t.setTranslation(tr);
			record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		}
	}
}
