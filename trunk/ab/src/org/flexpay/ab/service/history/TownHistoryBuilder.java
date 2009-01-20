package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Pair;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class TownHistoryBuilder extends HistoryBuilderBase<Town> {

	public static final int FIELD_NAME = 1;
	public static final int FIELD_TYPE_ID = 2;

	protected Logger log = LoggerFactory.getLogger(getClass());

	private CorrectionsService correctionsService;

	/**
	 * Build necessary diff records
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
	protected void doDiff(@Nullable Town t1, @NotNull Town t2, @NotNull Diff diff) {

		log.debug("creating new towns diff");

		if (!t2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		Town townOld = t1 == null ? new Town() : t1;

		// create type diff
		buildTypeDiff(townOld, t2, diff);

		// create name diff
		buildNameDiff(townOld, t2, diff);
	}

	private void buildNameDiff(@NotNull Town t1, @NotNull Town t2, @NotNull Diff diff) {

		List<Pair<TownNameTemporal, TownNameTemporal>> differences =
				DateIntervalUtil.diff(t1.getNamesTimeLine(), t2.getNamesTimeLine());

		for (Pair<TownNameTemporal, TownNameTemporal> pair : differences) {

			TownNameTemporal tmp1 = pair.getFirst();
			TownNameTemporal tmp2 = pair.getSecond();
			TownName n1 = tmp1.getValue();
			TownName n2 = tmp2.getValue();

			List<Language> langs = ApplicationConfig.getLanguages();
			for (Language lang : langs) {
				TownNameTranslation tr1 = n1 != null ? n1.getTranslation(lang) : null;
				TownNameTranslation tr2 = n2.getTranslation(lang);

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
					rec.setLanguage(lang);
					rec.setBeginDate(tmp2.getBegin());
					rec.setEndDate(tmp2.getEnd());
					diff.addRecord(rec);

					log.debug("Added name record {}", rec);
				}
			}
		}
	}

	private void buildTypeDiff(@NotNull Town t1, @NotNull Town t2, @NotNull Diff diff) {

		List<Pair<TownTypeTemporal, TownTypeTemporal>> differences =
				DateIntervalUtil.diff(t1.getTypesTimeLine(), t2.getTypesTimeLine());
		for (Pair<TownTypeTemporal, TownTypeTemporal> pair : differences) {

			TownTypeTemporal tmp1 = pair.getFirst();
			TownTypeTemporal tmp2 = pair.getSecond();

			if (tmp1.isValueEmpty() && tmp2.isValueEmpty()) {
				continue;
			}

			HistoryRecord rec = new HistoryRecord();
			rec.setFieldType(FIELD_TYPE_ID);
			rec.setOldLongValue(tmp1.getValue() != null ? tmp1.getValue().getId() : null);
			rec.setNewLongValue(tmp2.getValue() != null ? tmp2.getValue().getId() : null);
			rec.setBeginDate(tmp2.getBegin());
			rec.setEndDate(tmp2.getEnd());
			diff.addRecord(rec);

			log.debug("Added type record {}", rec);
		}
	}

	/**
	 * Apply diff to an object
	 *
	 * @param town Object to apply diff to
	 * @param diff Diff to apply
	 */
	public void patch(@NotNull Town town, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_TYPE_ID:
					patchType(town, record);
					break;
				case FIELD_NAME:
					patchName(town, record);
					break;
				default:
					log.warn("Unsupported field type {}", record);
			}
		}
	}

	private void patchName(@NotNull Town town, @NotNull HistoryRecord record) {

		log.debug("Patching name {}", record);

		TownName name = town.getNameForDate(record.getBeginDate());
		// create a new name object
		if (name == null || name.isNotNew()) {
			name = new TownName(name);
		}

		TownNameTranslation translation = name.getTranslation(record.getLanguage());
		if (translation == null) {
			translation = new TownNameTranslation();
			translation.setLang(record.getLanguage());
		}
		translation.setName(record.getNewStringValue());
		name.addNameTranslation(translation);

		town.setNameForDates(name, record.getBeginDate(), record.getEndDate());
	}

	private void patchType(@NotNull Town town, @NotNull HistoryRecord record) {

		log.debug("Patching type {}", record);

		TownType type = null;
		if (record.getNewLongValue() != null) {

			String externalId = record.getNewLongValue().toString();
			Stub<TownType> typeStub = correctionsService.findCorrection(
					externalId, TownType.class, record.getDiff().getDataSourceDescription());
			if (typeStub == null) {
				log.warn("Cannot find town type by id {}", record);
				return;
			}
			type = new TownType(typeStub);
		}
		town.setTypeForDates(type, record.getBeginDate(), record.getEndDate());
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}
}
