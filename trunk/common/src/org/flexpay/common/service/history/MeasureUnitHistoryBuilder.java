package org.flexpay.common.service.history;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.MeasureUnitName;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.builder.TranslationExtractor;
import org.flexpay.common.persistence.history.builder.TranslationPatcher;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeasureUnitHistoryBuilder extends HistoryBuilderBase<MeasureUnit> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final int FIELD_NAME = 1;

	/**
	 * Build necessary diff records
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
	@Override
	protected void doDiff(@Nullable MeasureUnit t1, @NotNull MeasureUnit t2, @NotNull Diff diff) {

		log.debug("creating new measure units diff");
		if (!t2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		MeasureUnit old = t1 == null ? new MeasureUnit() : t1;

		buildNameDiff(old, t2, diff);
	}

	private void buildNameDiff(MeasureUnit t1, MeasureUnit t2, Diff diff) {

		builderHelper.buildTranslationDiff(t1, t2, diff,
				new TranslationExtractor<Translation, MeasureUnit>() {

					public Translation getTranslation(MeasureUnit obj, @NotNull Language language) {
						return obj.getTranslation(language);
					}

					public int getTranslationField() {
						return FIELD_NAME;
					}
				});
	}


	/**
	 * Apply diff to an object
	 *
	 * @param measureUnit Object to apply diff to
	 * @param diff		Diff to apply
	 */
	@Override
	public void patch(@NotNull MeasureUnit measureUnit, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(measureUnit, record);
					break;
				default:
					log.info("Unsupported record: {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}
	}

	private void patchName(MeasureUnit type, HistoryRecord record) {

		builderHelper.patchTranslation(type, record, new TranslationPatcher<MeasureUnitName, MeasureUnit>() {
			public MeasureUnitName getNotNullTranslation(MeasureUnit obj, @NotNull Language language) {
				MeasureUnitName tr = obj.getTranslation(language);
				return tr == null ? new MeasureUnitName() : tr;
			}

			public void setTranslation(MeasureUnit obj, MeasureUnitName tr, String name) {
				tr.setName(name);
				obj.setName(tr);
			}
		});
	}
}
