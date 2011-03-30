package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.*;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.EqualsHelper;
import org.hibernate.envers.tools.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultRegion;
import static org.flexpay.common.persistence.history.TemporalObjectsHistoryBuildHelper.TemporalDataExtractor;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

public class TownHistoryBuilder extends HistoryBuilderBase<Town> {

	public static final int FIELD_NAME = 1;
	public static final int FIELD_TYPE_ID = 2;

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Build necessary diff records
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
	@Override
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

		log.debug("Building town name diff");

		List<Pair<TownNameTemporal, TownNameTemporal>> differences =
				DateIntervalUtil.diff(t1.getNamesTimeLine(), t2.getNamesTimeLine());

		for (Pair<TownNameTemporal, TownNameTemporal> pair : differences) {

			TownNameTemporal tmp1 = pair.getFirst();
			TownNameTemporal tmp2 = pair.getSecond();
			TownName n1 = tmp1.getValue();
			TownName n2 = tmp2.getValue();

			List<Language> langs = getLanguages();
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
					rec.setLanguage(lang.getLangIsoCode());
					rec.setBeginDate(tmp2.getBegin());
					rec.setEndDate(tmp2.getEnd());
					diff.addRecord(rec);

					log.debug("Added name record {}", rec);
				}
			}
		}
	}

	private static boolean objsEquals(Object o1, Object o2) {
		return (o1 == null && o2 == null) || (o1 != null && o1.equals(o2));
	}

	private void buildTypeDiff(@NotNull Town t1, @NotNull Town t2, @NotNull Diff diff) {

		log.debug("Building town type diff");

		List<TownTypeTemporal> types1 = t1.getTypesTimeLine().getIntervals();
		List<TownTypeTemporal> types2 = t2.getTypesTimeLine().getIntervals();

		log.debug("Town types sizes: {}, {}", types1.size(), types2.size());

		TemporalObjectsHistoryBuildHelper.buildDiff(new TemporalDataExtractor<TownTypeTemporal>() {
			@Override
			public Date getBeginDate(TownTypeTemporal obj) {
				return obj.getBegin();
			}

			@Override
			public Date getEndDate(TownTypeTemporal obj) {
				return obj.getEnd();
			}

			@Override
			public void buildDiff(Date begin, Date end, TownTypeTemporal tmp1, TownTypeTemporal tmp2, Diff dff) {

				if ((tmp1 == null || tmp1.isValueEmpty()) && (tmp2 == null || tmp2.isValueEmpty())) {
					return;
				}

				TownType type1 = tmp1 == null ? null : tmp1.getValue();
				TownType type2 = tmp2 == null ? null : tmp2.getValue();
				if (objsEquals(type1, type2)) {
					return;
				}

				HistoryRecord rec = new HistoryRecord();
				rec.setFieldType(FIELD_TYPE_ID);
				rec.setOldStringValue(type1 == null || type1.isNew() ? null : masterIndexService.getMasterIndex(type1));
				rec.setNewStringValue(type2 == null || type2.isNew() ? null : masterIndexService.getMasterIndex(type2));
				rec.setBeginDate(begin);
				rec.setEndDate(end);
				dff.addRecord(rec);

				log.debug("Added type record {}", rec);
			}
		}, types1, types2, diff);

	}

	/**
	 * Apply diff to an object
	 *
	 * @param town Object to apply diff to
	 * @param diff Diff to apply
	 */
	@Override
	public void patch(@NotNull Town town, @NotNull Diff diff) {

		// setup default region if not exists
		if (town.getRegion() == null) {
			town.setRegion(getDefaultRegion());
		}

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

		Language lang = record.getLang();
		if (lang == null) {
			log.info("No lang found for record {}", record);
			record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			return;
		}

		TownName name = town.getNameForDate(record.getBeginDate());
		// create a new name object
		if (name == null || name.isNotNew()) {
			name = new TownName(name);
		}

		TownNameTranslation translation = name.getTranslation(lang);
		if (translation == null) {
			translation = new TownNameTranslation();
			translation.setLang(lang);
		}
		translation.setName(record.getNewStringValue());
		name.addNameTranslation(translation);

		town.setNameForDates(name, record.getBeginDate(), record.getEndDate());
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchType(@NotNull Town town, @NotNull HistoryRecord record) {

		log.debug("Patching type {}", record);

		TownType type = null;
		if (record.getNewStringValue() != null) {

			String externalId = record.getNewStringValue();
			Stub<TownType> typeStub = correctionsService.findCorrection(
					externalId, TownType.class, masterIndexService.getMasterSourceDescriptionStub());
			if (typeStub == null) {
				throw new IllegalStateException("Cannot find town type by master index: " + externalId);
			}
			type = new TownType(typeStub);
		}
		town.setTypeForDates(type, record.getBeginDate(), record.getEndDate());
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}
}
