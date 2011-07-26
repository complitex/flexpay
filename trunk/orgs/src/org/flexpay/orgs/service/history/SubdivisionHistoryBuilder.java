package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.builder.ReferenceExtractor;
import org.flexpay.common.persistence.history.builder.ReferencePatcher;
import org.flexpay.common.persistence.history.builder.TranslationExtractor;
import org.flexpay.common.persistence.history.builder.TranslationPatcher;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.EqualsHelper;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.Subdivision;
import org.flexpay.orgs.persistence.SubdivisionDescription;
import org.flexpay.orgs.persistence.SubdivisionName;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.SubdivisionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SubdivisionHistoryBuilder extends HistoryBuilderBase<Subdivision> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final int FIELD_NAME = 1;
	public static final int FIELD_DESCRIPTION = 2;
	public static final int FIELD_HEAD_ORG_ID = 3;
	public static final int FIELD_PARENT_SUBDIVISION_ID = 4;
	public static final int FIELD_JURIDICAL_PERSON_ID = 5;
	public static final int FIELD_REAL_ADDRESS = 6;
	public static final int FIELD_TREE_PATH = 7;

	private OrganizationService organizationService;
	private SubdivisionService subdivisionService;

	/**
	 * Build necessary diff records
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
	@Override
	protected void doDiff(@Nullable Subdivision t1, @NotNull Subdivision t2, @NotNull Diff diff) {
		log.debug("creating new subdivisions diff");
		if (!t2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		Subdivision old = t1 == null ? new Subdivision() : t1;

		buildNormalFieldsDiff(old, t2, diff);
		buildNameDiff(old, t2, diff);
		buildDescriptionDiff(old, t2, diff);
		buildHeadOrganizationRefDiff(old, t2, diff);
		buildJuridicalPersonRefDiff(old, t2, diff);
		buildParentSubdivisionRefDiff(old, t2, diff);
	}

	private void buildNormalFieldsDiff(Subdivision p1, Subdivision p2, Diff diff) {
		if (!EqualsHelper.strEquals(p1.getRealAddress(), p2.getRealAddress())) {
			HistoryRecord rec = new HistoryRecord();
			rec.setFieldType(FIELD_REAL_ADDRESS);
			rec.setOldStringValue(p1.getRealAddress());
			rec.setNewStringValue(p2.getRealAddress());
			diff.addRecord(rec);
			log.debug("Added real address diff: {}", rec);
		}
		if (!EqualsHelper.strEquals(p1.getTreePath(), p2.getTreePath())) {
			HistoryRecord rec = new HistoryRecord();
			rec.setFieldType(FIELD_TREE_PATH);
			rec.setOldStringValue(p1.getTreePath());
			rec.setNewStringValue(p2.getTreePath());
			diff.addRecord(rec);
			log.debug("Added tree path diff: {}", rec);
		}
	}

	private void buildNameDiff(Subdivision p1, Subdivision p2, Diff diff) {

		builderHelper.buildTranslationDiff(p1, p2, diff, new TranslationExtractor<Translation, Subdivision>() {
            @Override
			public Translation getTranslation(Subdivision obj, @NotNull Language language) {
				return obj.getNameTranslation(language);
			}

            @Override
			public int getTranslationField() {
				return FIELD_NAME;
			}
		});
	}

	private void buildDescriptionDiff(Subdivision p1, Subdivision p2, Diff diff) {

		builderHelper.buildTranslationDiff(p1, p2, diff, new TranslationExtractor<Translation, Subdivision>() {
            @Override
			public Translation getTranslation(Subdivision obj, @NotNull Language language) {
				return obj.getDescriptionTranslation(language);
			}

            @Override
			public int getTranslationField() {
				return FIELD_DESCRIPTION;
			}
		});
	}

	private void buildHeadOrganizationRefDiff(Subdivision p1, Subdivision p2, Diff diff) {

		builderHelper.buildReferenceDiff(p1, p2, diff, new ReferenceExtractor<Organization, Subdivision>() {
            @Override
			public Organization getReference(Subdivision obj) {
				return obj.getHeadOrganization();
			}

            @Override
			public int getReferenceField() {
				return FIELD_HEAD_ORG_ID;
			}
		});
	}

	private void buildJuridicalPersonRefDiff(Subdivision p1, Subdivision p2, Diff diff) {

		builderHelper.buildReferenceDiff(p1, p2, diff, new ReferenceExtractor<Organization, Subdivision>() {
            @Override
			public Organization getReference(Subdivision obj) {
				return obj.getJuridicalPerson();
			}

            @Override
			public int getReferenceField() {
				return FIELD_JURIDICAL_PERSON_ID;
			}
		});
	}

	private void buildParentSubdivisionRefDiff(Subdivision p1, Subdivision p2, Diff diff) {

		builderHelper.buildReferenceDiff(p1, p2, diff, new ReferenceExtractor<Subdivision, Subdivision>() {
            @Override
			public Subdivision getReference(Subdivision obj) {
				return obj.getParentSubdivision();
			}

            @Override
			public int getReferenceField() {
				return FIELD_PARENT_SUBDIVISION_ID;
			}
		});
	}

	/**
	 * Apply diff to an object
	 *
	 * @param subdivision Object to apply diff to
	 * @param diff		Diff to apply
	 */
	@Override
	public void patch(@NotNull Subdivision subdivision, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(subdivision, record);
					break;
				case FIELD_DESCRIPTION:
					patchDescription(subdivision, record);
					break;
				case FIELD_HEAD_ORG_ID:
					patchHeadOrganizationReference(subdivision, record);
					break;
				case FIELD_PARENT_SUBDIVISION_ID:
					patchParentSubdivisionReference(subdivision, record);
					break;
				case FIELD_JURIDICAL_PERSON_ID:
					patchJuridicalPersonReference(subdivision, record);
					break;
				case FIELD_REAL_ADDRESS:
					log.debug("Patching real address: {}", record);
					subdivision.setRealAddress(record.getNewStringValueNotNull());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				case FIELD_TREE_PATH:
					log.debug("Patching tree path: {}", record);
					subdivision.setTreePath(record.getNewStringValueNotNull());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				default:
					log.info("Unsupported record: {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}
	}

	private void patchName(Subdivision obj, HistoryRecord record) {

		builderHelper.patchTranslation(obj, record, new TranslationPatcher<SubdivisionName, Subdivision>() {
            @Override
			public SubdivisionName getNotNullTranslation(Subdivision obj, @NotNull Language language) {
				SubdivisionName tr = obj.getNameTranslation(language);
				return tr == null ? new SubdivisionName() : tr;
			}

            @Override
			public void setTranslation(Subdivision obj, SubdivisionName tr, String name) {
				tr.setName(name);
				obj.setName(tr);
			}
		});
	}

	private void patchDescription(Subdivision obj, HistoryRecord record) {

		builderHelper.patchTranslation(obj, record, new TranslationPatcher<SubdivisionDescription, Subdivision>() {
            @Override
			public SubdivisionDescription getNotNullTranslation(Subdivision obj, @NotNull Language language) {
				SubdivisionDescription tr = obj.getDescriptionTranslation(language);
				return tr == null ? new SubdivisionDescription() : tr;
			}

            @Override
			public void setTranslation(Subdivision obj, SubdivisionDescription tr, String name) {
				tr.setName(name);
				obj.setDescription(tr);
			}
		});
	}

	private void patchHeadOrganizationReference(@NotNull Subdivision obj, @NotNull HistoryRecord record) {
		log.debug("Patching head organization reference {}", record);

		builderHelper.patchReference(obj, record, new ReferencePatcher<Organization, Subdivision>() {
            @Override
			public Class<Organization> getType() {
				return Organization.class;
			}

            @Override
			public void setReference(Subdivision obj, Stub<Organization> ref) {
				Organization org = organizationService.readFull(ref);
				obj.setHeadOrganization(org);
			}
		});
	}

	private void patchJuridicalPersonReference(@NotNull Subdivision obj, @NotNull HistoryRecord record) {
		log.debug("Patching juridical person reference {}", record);

		builderHelper.patchReference(obj, record, new ReferencePatcher<Organization, Subdivision>() {
            @Override
			public Class<Organization> getType() {
				return Organization.class;
			}

            @Override
			public void setReference(Subdivision obj, Stub<Organization> ref) {
				Organization org = ref == null ? null : organizationService.readFull(ref);
				obj.setJuridicalPerson(org);
			}
		});
	}

	private void patchParentSubdivisionReference(@NotNull Subdivision obj, @NotNull HistoryRecord record) {
		log.debug("Patching parent subdivision reference {}", record);

		builderHelper.patchReference(obj, record, new ReferencePatcher<Subdivision, Subdivision>() {
            @Override
			public Class<Subdivision> getType() {
				return Subdivision.class;
			}

            @Override
			public void setReference(Subdivision obj, Stub<Subdivision> ref) {
				Subdivision parent = ref == null ? null : subdivisionService.read(ref);
				obj.setParentSubdivision(parent);
			}
		});
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setSubdivisionService(SubdivisionService subdivisionService) {
		this.subdivisionService = subdivisionService;
	}
}
