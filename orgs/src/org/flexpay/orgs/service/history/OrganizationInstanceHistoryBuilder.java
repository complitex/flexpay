package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.EqualsHelper;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.OrganizationInstance;
import org.flexpay.orgs.persistence.OrganizationInstanceDescription;
import org.flexpay.orgs.service.OrganizationService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public abstract class OrganizationInstanceHistoryBuilder<
		D extends OrganizationInstanceDescription,
		T extends OrganizationInstance<D, T>> extends HistoryBuilderBase<T> {

	public static final int FIELD_ORGANIZATION_ID = 1;
	public static final int FIELD_DESCRIPTION = 2;

	protected Logger log = LoggerFactory.getLogger(getClass());

	private OrganizationService organizationService;

	protected abstract T newInstance();

	protected abstract D newDescriptionInstance();

	/**
	 * Build necessary diff records
	 *
	 * @param org1 First object
	 * @param org2 Second object
	 * @param diff Diff object
	 */
    @Override
	protected void doDiff(@Nullable T org1, @NotNull T org2, @NotNull Diff diff) {

		log.debug("creating new service providers diff");
		if (!org2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		T old = org1 == null ? newInstance() : org1;

		buildOrganizationRefDiff(old, org2, diff);
		buildDescriptionDiff(old, org2, diff);

		doInstanceDiff(old, org2, diff);
	}

	// redefine in child instance
	protected void doInstanceDiff(@NotNull T org1, @NotNull T org2, @NotNull Diff diff) {

	}

	private void buildOrganizationRefDiff(T t1, T t2, Diff diff) {
		Organization org1 = t1.getOrganization();
		Organization org2 = t2.getOrganization();
		boolean noOrganization = (org1 == null || org1.isNew()) && (org2 == null || org2.isNew());

		// no organization found in both objects, nothing to do
		if (noOrganization) {
			return;
		}

		boolean sameOrganization = org1 != null && org1.isNotNew() &&
								   org2 != null && org2.isNotNew() &&
								   org1.equals(org2);
		// same parent found in both objects, nothing to do
		if (sameOrganization) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_ORGANIZATION_ID);
		rec.setOldStringValue(org1 == null || org1.isNew() ? null : masterIndexService.getMasterIndex(org1));
		rec.setNewStringValue(org2 == null || org2.isNew() ? null : masterIndexService.getMasterIndex(org2));
		diff.addRecord(rec);
		log.debug("Added organization ref diff record: {}", rec);
	}

	private void buildDescriptionDiff(T org1, T org2, Diff diff) {
		List<Language> langs = ApplicationConfig.getLanguages();
		for (Language lang : langs) {
			D tr1 = org1.getDescription(lang);
			D tr2 = org2.getDescription(lang);

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

	/**
	 * Apply diff to an object
	 *
	 * @param org  Object to apply diff to
	 * @param diff Diff to apply
	 */
    @Override
	public void patch(@NotNull T org, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_DESCRIPTION:
					patchDescription(org, record);
					break;
				case FIELD_ORGANIZATION_ID:
					patchOrganizationReference(org, record);
					break;
				default:
					if (!doInstancePatch(org, record)) {
						log.info("Unsupported record: {}", record);
						record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
					}
			}
		}
	}

	private void patchDescription(T org, HistoryRecord record) {
		Language lang = record.getLang();
		if (lang == null) {
			log.info("No lang found for record {}", record);
			record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			return;
		}

		D tr = org.getDescription(lang);

		if (tr == null) {
			tr = newDescriptionInstance();
			tr.setLang(lang);
		}

		tr.setName(record.getNewStringValueNotNull());
		org.setDescription(tr);
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchOrganizationReference(@NotNull T org, @NotNull HistoryRecord record) {
		log.debug("Patching organization reference {}", record);

		if (record.getNewStringValue() != null) {
			String externalId = record.getNewStringValue();
			Stub<Organization> stub = correctionsService.findCorrection(
					externalId, Organization.class, masterIndexService.getMasterSourceDescriptionStub());
			if (stub == null) {
				throw new IllegalStateException("Cannot find organization by master index: " + externalId);
			}
			Organization organization = organizationService.readFull(stub);
			if (organization == null) {
				throw new IllegalStateException("Cannot find organization by stub: " + stub);
			}
			org.setOrganization(organization);
		}

		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	// redefine in child instance
	protected boolean doInstancePatch(@NotNull T org1, @NotNull HistoryRecord record) {
		return false;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
