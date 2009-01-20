package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TownTypeHistoryBuilder extends HistoryBuilderBase<TownType> {

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
	protected void doDiff(@Nullable TownType t1, @NotNull TownType t2, @NotNull Diff diff) {

		if (!t2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			TownTypeTranslation tr1 = t1 != null ? t1.getTranslation(lang) : null;
			TownTypeTranslation tr2 = t2.getTranslation(lang);

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
				rec.setLanguage(lang);
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
				rec.setLanguage(lang);
				diff.addRecord(rec);
			}

			log.debug("Colpleted Diff for lang {}", lang);
		}
	}

	/**
	 * Apply diff to an object
	 *
	 * @param t	Object to apply diff to
	 * @param diff Diff to apply
	 */
	public void patch(@NotNull TownType t, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {
			TownTypeTranslation tr = t.getTranslation(record.getLanguage());

			if (tr == null) {
				tr = new TownTypeTranslation();
				tr.setLang(record.getLanguage());
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
					continue;
			}

			t.setTranslation(tr);
		}
	}
}
