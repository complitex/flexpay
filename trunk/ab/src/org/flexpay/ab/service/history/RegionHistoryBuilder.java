package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTemporal;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Pair;
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

import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultCountry;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

public class RegionHistoryBuilder extends HistoryBuilderBase<Region> {

	public static final int FIELD_NAME = 1;

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Build necessary diff records
	 *
	 * @param r1   First object
	 * @param r2   Second object
	 * @param diff Diff object
	 */
	@Override
	protected void doDiff(@Nullable Region r1, @NotNull Region r2, @NotNull Diff diff) {

		log.debug("Creating new regions diff");

		if (!r2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		Region regionOld = r1 == null ? new Region() : r1;

		// create name diff
		buildNameDiff(regionOld, r2, diff);
	}

	private void buildNameDiff(@NotNull Region r1, @NotNull Region r2, @NotNull Diff diff) {

		log.debug("Building region name diff");

		List<Pair<RegionNameTemporal, RegionNameTemporal>> differences =
				DateIntervalUtil.diff(r1.getNamesTimeLine(), r2.getNamesTimeLine());

		for (Pair<RegionNameTemporal, RegionNameTemporal> pair : differences) {

			RegionNameTemporal tmp1 = pair.getFirst();
			RegionNameTemporal tmp2 = pair.getSecond();
			RegionName n1 = tmp1.getValue();
			RegionName n2 = tmp2.getValue();

			List<Language> langs = getLanguages();
			for (Language lang : langs) {
				RegionNameTranslation tr1 = n1 != null ? n1.getTranslation(lang) : null;
				RegionNameTranslation tr2 = n2.getTranslation(lang);

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

	/**
	 * Apply diff to an object
	 *
	 * @param region Object to apply diff to
	 * @param diff   Diff to apply
	 */
	@Override
	public void patch(@NotNull Region region, @NotNull Diff diff) {

		// setup default region if not exists
		if (region.getCountry() == null) {
			region.setCountry(getDefaultCountry());
		}

		for (HistoryRecord record : diff.getHistoryRecords()) {
			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(region, record);
					break;
				default:
					log.warn("Unsupported field type {}", record);
			}
		}
	}

	private void patchName(@NotNull Region region, @NotNull HistoryRecord record) {

		log.debug("Patching name {}", record);

		Language lang = record.getLang();
		if (lang == null) {
			log.info("No lang found for record {}", record);
			record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			return;
		}

		RegionName name = region.getNameForDate(record.getBeginDate());
		// create a new name object
		if (name == null || name.isNotNew()) {
			name = new RegionName(name);
		}

		RegionNameTranslation translation = name.getTranslation(lang);
		if (translation == null) {
			translation = new RegionNameTranslation();
			translation.setLang(lang);
		}
		translation.setName(record.getNewStringValue());
		name.addNameTranslation(translation);

		region.setNameForDates(name, record.getBeginDate(), record.getEndDate());
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

}
