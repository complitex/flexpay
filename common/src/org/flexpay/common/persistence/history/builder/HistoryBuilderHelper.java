package org.flexpay.common.persistence.history.builder;

import org.apache.commons.lang.ObjectUtils;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.JpaSetService;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.EqualsHelper;
import org.flexpay.common.util.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.List;

public class HistoryBuilderHelper implements JpaSetService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private MasterIndexService masterIndexService;
	private CorrectionsService correctionsService;

	public <T extends Translation, DO extends DomainObject>
	void buildTranslationDiff(DO obj1, DO obj2, Diff diff, TranslationExtractor<T, DO> extractor) {

		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			T tr1 = extractor.getTranslation(obj1, lang);
			T tr2 = extractor.getTranslation(obj2, lang);

			// no translation, check other languages
			if (tr1 == null && tr2 == null) {
				continue;
			}

			boolean nameDiffer = !EqualsHelper.strEquals(
					tr1 == null ? null : tr1.getName(),
					tr2 == null ? null : tr2.getName());

			if (nameDiffer) {
				HistoryRecord rec = new HistoryRecord();
				rec.setFieldType(extractor.getTranslationField());
				rec.setOldStringValue(tr1 == null ? null : tr1.getName());
				rec.setNewStringValue(tr2 == null ? null : tr2.getName());
				rec.setLanguage(lang.getLangIsoCode());
				diff.addRecord(rec);

				log.debug("Added name diff for lang {}\n{}", lang, rec);
			}
		}
	}

	public <T extends Translation, DO extends DomainObject>
	void patchTranslation(DO obj1, HistoryRecord record, TranslationPatcher<T, DO> patcher) {

		Language lang = record.getLang();
		if (lang == null) {
			log.info("No lang found for record {}", record);
			record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			return;
		}

		T tr = patcher.getNotNullTranslation(obj1, lang);
		// ensure lang is set
		tr.setLang(lang);
		patcher.setTranslation(obj1, tr, record.getNewStringValueNotNull());
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private boolean isNew(DomainObject obj) {
		Long id = obj == null ? null : obj.getId();
		return id == null || id <= 0L;
	}

	private boolean isNotNew(DomainObject obj) {
		Long id = obj == null ? null : obj.getId();
		return id != null && id > 0L;
	}

	private boolean equalsRef(DomainObject obj1, DomainObject obj2) {
		Long id1 = obj1 == null ? null : obj1.getId();
		Long id2 = obj2 == null ? null : obj2.getId();
		return ObjectUtils.equals(id1, id2);
	}

	public <Ref extends DomainObject, DO extends DomainObject>
	void buildReferenceDiff(DO obj1, DO obj2, Diff diff, ReferenceExtractor<Ref, DO> extractor) {

		Ref ref1 = extractor.getReference(obj1);
		Ref ref2 = extractor.getReference(obj2);
		boolean noRef = isNew(ref1) && isNew(ref2);

		// no references found in both objects, nothing to do
		if (noRef) {
			return;
		}

		boolean sameRef = isNotNew(ref1) && isNotNew(ref2) && equalsRef(ref1, ref2);
		// same reference found in both objects, nothing to do
		if (sameRef) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(extractor.getReferenceField());
		rec.setOldStringValue(isNew(ref1) ? null : masterIndexService.getMasterIndex(ref1));
		rec.setNewStringValue(isNew(ref2) ? null : masterIndexService.getMasterIndex(ref2));
		diff.addRecord(rec);
		log.debug("Added ref diff record: {}", rec);
	}

	public <Ref extends DomainObject, DO extends DomainObject>
	void patchReference(DO obj1, HistoryRecord record, ReferencePatcher<Ref, DO> patcher) {

		if (record.getNewStringValue() != null) {
			String externalId = record.getNewStringValue();
			Stub<Ref> stub = correctionsService.findCorrection(
					externalId, patcher.getType(), masterIndexService.getMasterSourceDescriptionStub());
			if (stub == null) {
				throw new IllegalStateException("Cannot find reference of type " + patcher.getType() +
												" by master index " + externalId);
			}
			patcher.setReference(obj1, stub);
		} else {
			patcher.setReference(obj1, null);
		}

		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        masterIndexService.setJpaTemplate(jpaTemplate);
        correctionsService.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setMasterIndexService(MasterIndexService masterIndexService) {
		this.masterIndexService = masterIndexService;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}
}
