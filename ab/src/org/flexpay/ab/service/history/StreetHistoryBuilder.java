package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.EqualsHelper;
import org.hibernate.envers.tools.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

public class StreetHistoryBuilder extends HistoryBuilderBase<Street> {

	public static final int FIELD_NAME = 1;
	public static final int FIELD_TYPE_ID = 2;
	public static final int FIELD_TOWN_ID = 3;

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Build necessary diff records
	 *
	 * @param s1   First object
	 * @param s2   Second object
	 * @param diff Diff object
	 */
	@Override
	protected void doDiff(@Nullable Street s1, @NotNull Street s2, @NotNull Diff diff) {

		log.debug("creating new streets diff");

		if (!s2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		Street streetOld = s1 == null ? new Street() : s1;

		// create type diff
		buildTypeDiff(streetOld, s2, diff);

		// create name diff
		buildNameDiff(streetOld, s2, diff);

		// create town reference diff
		buildParentReferenceDiff(streetOld, s2, diff);
	}

	private void buildParentReferenceDiff(@NotNull Street s1, @NotNull Street s2, @NotNull Diff diff) {

		boolean noParent = (s1.getParent() == null || s1.getParent().isNew()) &&
						   (s2.getParent() == null || s2.getParent().isNew());

		// no parent found in both objects, nothing to do
		if (noParent) {
			return;
		}

		boolean sameParent = s1.getParent() != null && s1.getParent().isNotNew() &&
							 s2.getParent() != null && s2.getParent().isNotNew() &&
							 s1.getParentStub().equals(s2.getParentStub());
		// same parent found in both objects, nothing to do
		if (sameParent) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_TOWN_ID);
		rec.setOldStringValue(
				s1.getParent() == null || s1.getParent().isNew() ?
				null :
				masterIndexService.getMasterIndex(s1.getTown()));
		rec.setNewStringValue(
				s2.getParent() == null || s2.getParent().isNew() ?
				null :
				masterIndexService.getMasterIndex(s2.getTown()));
		diff.addRecord(rec);
	}

	private void buildNameDiff(@NotNull Street s1, @NotNull Street s2, @NotNull Diff diff) {

		List<Pair<StreetNameTemporal, StreetNameTemporal>> differences =
				DateIntervalUtil.diff(s1.getNamesTimeLine(), s2.getNamesTimeLine());

		for (Pair<StreetNameTemporal, StreetNameTemporal> pair : differences) {

			StreetNameTemporal tmp1 = pair.getFirst();
			StreetNameTemporal tmp2 = pair.getSecond();
			StreetName n1 = tmp1.getValue();
			StreetName n2 = tmp2.getValue();

			List<Language> langs = getLanguages();
			for (Language lang : langs) {
				StreetNameTranslation tr1 = n1 != null ? n1.getTranslation(lang) : null;
				StreetNameTranslation tr2 = n2.getTranslation(lang);

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

	private void buildTypeDiff(@NotNull Street t1, @NotNull Street t2, @NotNull Diff diff) {

		List<Pair<StreetTypeTemporal, StreetTypeTemporal>> differences =
				DateIntervalUtil.diff(t1.getTypesTimeLine(), t2.getTypesTimeLine());
		for (Pair<StreetTypeTemporal, StreetTypeTemporal> pair : differences) {

			StreetTypeTemporal tmp1 = pair.getFirst();
			StreetTypeTemporal tmp2 = pair.getSecond();

			if (tmp1.isValueEmpty() && tmp2.isValueEmpty()) {
				continue;
			}

			HistoryRecord rec = new HistoryRecord();
			rec.setFieldType(FIELD_TYPE_ID);
			rec.setOldStringValue(tmp1.isValueEmpty() ? null : masterIndexService.getMasterIndex(tmp1.getValue()));
			rec.setNewStringValue(tmp2.isValueEmpty() ? null : masterIndexService.getMasterIndex(tmp2.getValue()));
			rec.setBeginDate(tmp2.getBegin());
			rec.setEndDate(tmp2.getEnd());
			diff.addRecord(rec);

			log.debug("Added type record {}", rec);
		}
	}

	/**
	 * Apply diff to an object
	 *
	 * @param street Object to apply diff to
	 * @param diff Diff to apply
	 */
	@Override
	public void patch(@NotNull Street street, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_TYPE_ID:
					patchType(street, record);
					break;
				case FIELD_NAME:
					patchName(street, record);
					break;
				case FIELD_TOWN_ID:
					patchTownReference(street, record);
					break;
				default:
					log.warn("Unsupported field type {}", record);
			}
		}
	}

	private void patchTownReference(@NotNull Street street, @NotNull HistoryRecord record) {
		log.debug("Patching town reference {}", record);

		Town parent = null;
		if (record.getNewStringValue() != null) {
			String externalId = record.getNewStringValue();
			Stub<Town> townStub = correctionsService.findCorrection(
					externalId, Town.class, masterIndexService.getMasterSourceDescriptionStub());
			if (townStub == null) {
				throw new IllegalStateException("Cannot find town type by master index: " + externalId);
			}
			parent = new Town(townStub);

		}
		street.setParent(parent);
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchName(@NotNull Street street, @NotNull HistoryRecord record) {

		log.debug("Patching name {}", record);

		Language lang = record.getLang();
		if (lang == null) {
			log.info("No lang found for record {}", record);
			record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			return;
		}

		StreetName name = street.getNameForDate(record.getBeginDate());
		// create a new name object
		if (name == null || name.isNotNew()) {
			name = new StreetName(name);
		}

		StreetNameTranslation translation = name.getTranslation(lang);
		if (translation == null) {
			translation = new StreetNameTranslation();
			translation.setLang(lang);
		}
		translation.setName(record.getNewStringValue());
		name.addNameTranslation(translation);

		street.setNameForDates(name, record.getBeginDate(), record.getEndDate());
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchType(@NotNull Street street, @NotNull HistoryRecord record) {

		log.debug("Patching type {}", record);

		StreetType type = null;
		if (record.getNewStringValue() != null) {

			String externalId = record.getNewStringValue();
			Stub<StreetType> typeStub = correctionsService.findCorrection(
					externalId, StreetType.class, masterIndexService.getMasterSourceDescriptionStub());
			if (typeStub == null) {
				throw new IllegalStateException("Cannot find street type by master index: " + externalId);
			}
			type = new StreetType(typeStub);
		}
		street.setTypeForDates(type, record.getBeginDate(), record.getEndDate());
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}
}
