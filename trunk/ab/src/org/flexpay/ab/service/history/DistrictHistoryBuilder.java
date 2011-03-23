package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Pair;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.EqualsHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

public class DistrictHistoryBuilder extends HistoryBuilderBase<District> {

	public static final int FIELD_NAME = 1;
	public static final int FIELD_TOWN_ID = 2;

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Build necessary diff records
	 *
	 * @param d1   First object
	 * @param d2   Second object
	 * @param diff Diff object
	 */
	@Override
	protected void doDiff(@Nullable District d1, @NotNull District d2, @NotNull Diff diff) {

		log.debug("creating new districts diff");

		if (!d2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		District districtOld = d1 == null ? new District() : d1;

		// create name diff
		buildNameDiff(districtOld, d2, diff);

		// create town reference diff
		buildParentReferenceDiff(districtOld, d2, diff);
	}

	private void buildParentReferenceDiff(@NotNull District d1, @NotNull District d2, @NotNull Diff diff) {

		boolean noParent = (d1.getParent() == null || d1.getParent().isNew()) &&
						   (d2.getParent() == null || d2.getParent().isNew());

		// no parent found in both objects, nothing to do
		if (noParent) {
			return;
		}

		boolean sameParent = d1.getParent() != null && d1.getParent().isNotNew() &&
							 d2.getParent() != null && d2.getParent().isNotNew() &&
							 d1.getParentStub().equals(d2.getParentStub());
		// same parent found in both objects, nothing to do
		if (sameParent) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_TOWN_ID);
		rec.setOldStringValue(
				d1.getParent() == null || d1.getParent().isNew() ?
				null :
				masterIndexService.getMasterIndex(d1.getTown()));
		rec.setNewStringValue(
				d2.getParent() == null || d2.getParent().isNew() ?
				null :
				masterIndexService.getMasterIndex(d2.getTown()));
		diff.addRecord(rec);
	}

	private void buildNameDiff(@NotNull District d1, @NotNull District d2, @NotNull Diff diff) {

		List<Pair<DistrictNameTemporal, DistrictNameTemporal>> differences =
				DateIntervalUtil.diff(d1.getNamesTimeLine(), d2.getNamesTimeLine());

		for (Pair<DistrictNameTemporal, DistrictNameTemporal> pair : differences) {

			DistrictNameTemporal tmp1 = pair.getFirst();
			DistrictNameTemporal tmp2 = pair.getSecond();
			DistrictName n1 = tmp1.getValue();
			DistrictName n2 = tmp2.getValue();

			List<Language> langs = getLanguages();
			for (Language lang : langs) {
				DistrictNameTranslation tr1 = n1 != null ? n1.getTranslation(lang) : null;
				DistrictNameTranslation tr2 = n2.getTranslation(lang);

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

	/**
	 * Apply diff to an object
	 *
	 * @param district Object to apply diff to
	 * @param diff	 Diff to apply
	 */
    @Override
	public void patch(@NotNull District district, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(district, record);
					break;
				case FIELD_TOWN_ID:
					patchTownReference(district, record);
					break;
				default:
					log.warn("Unsupported field type {}", record);
			}
		}
	}

	private void patchTownReference(@NotNull District district, @NotNull HistoryRecord record) {
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
		district.setParent(parent);
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchName(@NotNull District district, @NotNull HistoryRecord record) {

		log.debug("Patching name {}", record);

		Language lang = record.getLang();
		if (lang == null) {
			log.info("No lang found for record {}", record);
			record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			return;
		}

		DistrictName name = district.getNameForDate(record.getBeginDate());
		// create a new name object
		if (name == null || name.isNotNew()) {
			name = new DistrictName(name);
		}

		DistrictNameTranslation translation = name.getTranslation(lang);
		if (translation == null) {
			translation = new DistrictNameTranslation();
			translation.setLang(lang);
		}
		translation.setName(record.getNewStringValue());
		name.addNameTranslation(translation);

		district.setNameForDates(name, record.getBeginDate(), record.getEndDate());
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}
}
