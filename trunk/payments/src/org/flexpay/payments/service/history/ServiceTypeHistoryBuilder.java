package org.flexpay.payments.service.history;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.builder.TranslationExtractor;
import org.flexpay.common.persistence.history.builder.TranslationPatcher;
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
	public static final int FIELD_DESCRIPTION = 3;

	/**
	 * Build necessary diff records
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
    @Override
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

		TranslationExtractor<ServiceTypeNameTranslation, ServiceType> extractor =
				new TranslationExtractor<ServiceTypeNameTranslation, ServiceType>() {
                    @Override
					public ServiceTypeNameTranslation getTranslation(ServiceType obj, @NotNull Language language) {
						return obj.getTranslation(language);
					}

                    @Override
					public int getTranslationField() {
						return FIELD_NAME;
					}
				};
		builderHelper.buildTranslationDiff(t1, t2, diff, extractor);

		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			ServiceTypeNameTranslation tr1 = extractor.getTranslation(t1, lang);
			ServiceTypeNameTranslation tr2 = extractor.getTranslation(t2, lang);

			// no translation, check other languages
			if (tr1 == null && tr2 == null) {
				continue;
			}

			boolean descrDiffer = !EqualsHelper.strEquals(
					tr1 == null ? null : tr1.getDescription(),
					tr2 == null ? null : tr2.getDescription());

			if (descrDiffer) {
				HistoryRecord rec = new HistoryRecord();
				rec.setFieldType(FIELD_DESCRIPTION);
				rec.setOldStringValue(tr1 == null ? null : tr1.getDescription());
				rec.setNewStringValue(tr2 == null ? null : tr2.getDescription());
				rec.setLanguage(lang.getLangIsoCode());
				diff.addRecord(rec);

				log.debug("Added description diff for lang {}\n{}", lang, rec);
			}
		}

	}

	/**
	 * Apply diff to an object
	 *
	 * @param type Object to apply diff to
	 * @param diff Diff to apply
	 */
    @Override
	public void patch(@NotNull ServiceType type, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(type, record);
					break;
				case FIELD_DESCRIPTION:
					patchDescription(type, record);
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

		log.debug("Patching type name {}", record);

		builderHelper.patchTranslation(type, record, new TranslationPatcher<ServiceTypeNameTranslation, ServiceType>() {
            @Override
			public ServiceTypeNameTranslation getNotNullTranslation(ServiceType obj, @NotNull Language language) {
				ServiceTypeNameTranslation tr = obj.getTranslation(language);
				return tr == null ? new ServiceTypeNameTranslation() : tr;
			}

            @Override
			public void setTranslation(ServiceType obj, ServiceTypeNameTranslation tr, String name) {
				tr.setName(name);
				obj.setTypeName(tr);
			}
		});
	}

	private void patchDescription(ServiceType type, HistoryRecord record) {

		log.debug("Patching type description {}", record);

		builderHelper.patchTranslation(type, record, new TranslationPatcher<ServiceTypeNameTranslation, ServiceType>() {
            @Override
			public ServiceTypeNameTranslation getNotNullTranslation(ServiceType obj, @NotNull Language language) {
				ServiceTypeNameTranslation tr = obj.getTranslation(language);
				return tr == null ? new ServiceTypeNameTranslation() : tr;
			}

            @Override
			public void setTranslation(ServiceType obj, ServiceTypeNameTranslation tr, String name) {
				tr.setDescription(name);
				obj.setTypeName(tr);
			}
		});
	}
}
