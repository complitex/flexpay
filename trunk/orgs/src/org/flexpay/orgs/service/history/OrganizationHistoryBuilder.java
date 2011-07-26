package org.flexpay.orgs.service.history;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.EqualsHelper;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.OrganizationDescription;
import org.flexpay.orgs.persistence.OrganizationName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OrganizationHistoryBuilder extends HistoryBuilderBase<Organization> {

	public static final int FIELD_NAME = 1;
	public static final int FIELD_DESCRIPTION = 2;

	public static final int FIELD_INDIVIDUAL_TAX_NUMBER = 3;
	public static final int FIELD_KPP = 4;
	public static final int FIELD_JURIDICAL_ADDRESS = 5;
	public static final int FIELD_POSTAL_ADDRESS = 6;

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Build necessary diff records
	 *
	 * @param org1   First object
	 * @param org2   Second object
	 * @param diff Diff object
	 */
    @Override
	protected void doDiff(@Nullable Organization org1, @NotNull Organization org2, @NotNull Diff diff) {

		log.debug("creating new organizations diff");
		if (!org2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		Organization old = org1 == null ? new Organization() : org1;

		buildSimpleFieldsDiff(old, org2, diff);
		buildNameDiff(old, org2, diff);
		buildDescriptionDiff(old, org2, diff);
	}

	private boolean differ(String s1, String s2) {
		return !StringUtils.trimToEmpty(s1).equals(StringUtils.trimToEmpty(s2));
	}

	private HistoryRecord newRecord(int fieldType) {
		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(fieldType);
		return rec;
	}

	private void buildNameDiff(Organization org1, Organization org2, Diff diff) {
		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			OrganizationName tr1 = org1.getNameTranslation(lang);
			OrganizationName tr2 = org2.getNameTranslation(lang);

			// no translation, check other languages
			if (tr1 == null && tr2 == null) {
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
				diff.addRecord(rec);

				log.debug("Added name diff for lang {}\n{}", lang, rec);
			}
		}
	}

	private void buildDescriptionDiff(Organization org1, Organization org2, Diff diff) {
		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			OrganizationDescription tr1 = org1.getDescriptionTranslation(lang);
			OrganizationDescription tr2 = org2.getDescriptionTranslation(lang);

			// no translation, check other languages
			if (tr1 == null && tr2 == null) {
				continue;
			}

			boolean nameDiffer = !EqualsHelper.strEquals(
					tr1 == null ? null : tr1.getName(),
					tr2 == null ? null : tr2.getName());

			if (nameDiffer) {
				HistoryRecord rec = new HistoryRecord();
				rec.setFieldType(FIELD_DESCRIPTION);
				rec.setOldStringValue(tr1 == null ? null : tr1.getName());
				rec.setNewStringValue(tr2 == null ? null : tr2.getName());
				rec.setLanguage(lang.getLangIsoCode());
				diff.addRecord(rec);

				log.debug("Added description diff for lang {}\n{}", lang, rec);
			}
		}
	}

	private void buildSimpleFieldsDiff(Organization org1, Organization org2, Diff diff) {

		if (differ(org1.getIndividualTaxNumber(), org2.getIndividualTaxNumber())) {
			HistoryRecord rec = newRecord(FIELD_INDIVIDUAL_TAX_NUMBER);
			rec.setOldStringValue(org1.getIndividualTaxNumber());
			rec.setNewStringValue(org2.getIndividualTaxNumber());
			diff.addRecord(rec);
			log.debug("Added individual tax number diff record {}", rec);
		}

		if (differ(org1.getKpp(), org2.getKpp())) {
			HistoryRecord rec = newRecord(FIELD_KPP);
			rec.setOldStringValue(org1.getKpp());
			rec.setNewStringValue(org2.getKpp());
			diff.addRecord(rec);
			log.debug("Added kpp diff record {}", rec);
		}

		if (differ(org1.getJuridicalAddress(), org2.getJuridicalAddress())) {
			HistoryRecord rec = newRecord(FIELD_JURIDICAL_ADDRESS);
			rec.setOldStringValue(org1.getJuridicalAddress());
			rec.setNewStringValue(org2.getJuridicalAddress());
			diff.addRecord(rec);
			log.debug("Added juridical address diff record {}", rec);
		}
		if (differ(org1.getPostalAddress(), org2.getPostalAddress())) {
			HistoryRecord rec = newRecord(FIELD_POSTAL_ADDRESS);
			rec.setOldStringValue(org1.getPostalAddress());
			rec.setNewStringValue(org2.getPostalAddress());
			diff.addRecord(rec);
			log.debug("Added postal address diff record {}", rec);
		}
	}

	/**
	 * Apply diff to an object
	 *
	 * @param org	Object to apply diff to
	 * @param diff Diff to apply
	 */
    @Override
	public void patch(@NotNull Organization org, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(org, record);
					break;
				case FIELD_DESCRIPTION:
					patchDescription(org, record);
					break;
				case FIELD_INDIVIDUAL_TAX_NUMBER:
					log.debug("Patching organization INN: {}", record);
					org.setIndividualTaxNumber(record.getNewStringValue());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				case FIELD_KPP:
					log.debug("Patching organization KPP: {}", record);
					org.setKpp(record.getNewStringValue());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				case FIELD_JURIDICAL_ADDRESS:
					log.debug("Patching organization Juridical address: {}", record);
					org.setJuridicalAddress(record.getNewStringValue());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				case FIELD_POSTAL_ADDRESS:
					log.debug("Patching organization Postal address: {}", record);
					org.setPostalAddress(record.getNewStringValue());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				default:
					log.info("Unsupported record: {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}
	}

	private void patchName(Organization org, HistoryRecord record) {

		log.debug("Patching organization name: {}", record);

		Language lang = record.getLang();
		if (lang == null) {
			log.info("No lang found for record {}", record);
			record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			return;
		}

		OrganizationName tr = org.getNameTranslation(lang);

		if (tr == null) {
			tr = new OrganizationName();
			tr.setLang(lang);
		}

		tr.setName(record.getNewStringValueNotNull());
		org.setName(tr);
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchDescription(Organization org, HistoryRecord record) {

		log.debug("Patching organization description {}", record);
		Language lang = record.getLang();
		if (lang == null) {
			log.info("No lang found for record {}", record);
			record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			return;
		}

		OrganizationDescription tr = org.getDescriptionTranslation(lang);

		if (tr == null) {
			tr = new OrganizationDescription();
			tr.setLang(lang);
		}

		tr.setName(record.getNewStringValueNotNull());
		org.setDescription(tr);
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}
}
