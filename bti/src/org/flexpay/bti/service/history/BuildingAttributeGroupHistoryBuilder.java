package org.flexpay.bti.service.history;

import org.flexpay.bti.persistence.building.BuildingAttributeGroup;
import org.flexpay.bti.persistence.building.BuildingAttributeGroupName;
import org.flexpay.common.persistence.Language;
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

public class BuildingAttributeGroupHistoryBuilder extends HistoryBuilderBase<BuildingAttributeGroup> {

	private Logger log = LoggerFactory.getLogger(getClass());
	public static final int FIELD_NAME = 1;

	/**
	 * Build necessary diff records
	 *
	 * @param g1   First object
	 * @param g2   Second object
	 * @param diff Diff object
	 */
    @Override
	protected void doDiff(@Nullable BuildingAttributeGroup g1, @NotNull BuildingAttributeGroup g2, @NotNull Diff diff) {

		log.debug("creating new building attribute type groups diff");
		if (!g2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		BuildingAttributeGroup old = g1 == null ? new BuildingAttributeGroup() : g1;

		buildNameDiff(old, g2, diff);
	}

	private void buildNameDiff(BuildingAttributeGroup g1, BuildingAttributeGroup g2, Diff diff) {

		builderHelper.buildTranslationDiff(g1, g2, diff, new TranslationExtractor<Translation, BuildingAttributeGroup>() {
            @Override
			public Translation getTranslation(BuildingAttributeGroup obj, @NotNull Language language) {
				return obj.getTranslation(language);
			}

            @Override
			public int getTranslationField() {
				return FIELD_NAME;
			}
		});
	}


	/**
	 * Apply diff to an object
	 *
	 * @param group	Object to apply diff to
	 * @param diff Diff to apply
	 */
    @Override
	public void patch(@NotNull BuildingAttributeGroup group, @NotNull Diff diff) {
		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(group, record);
					break;
				default:
					log.info("Unsupported record: {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}
	}

	private void patchName(BuildingAttributeGroup group, HistoryRecord record) {

		builderHelper.patchTranslation(group, record, new TranslationPatcher<BuildingAttributeGroupName, BuildingAttributeGroup>() {
            @Override
			public BuildingAttributeGroupName getNotNullTranslation(BuildingAttributeGroup obj, @NotNull Language language) {
				BuildingAttributeGroupName tr = obj.getTranslation(language);
				return tr == null ? new BuildingAttributeGroupName() : tr;
			}

            @Override
			public void setTranslation(BuildingAttributeGroup obj, BuildingAttributeGroupName tr, String name) {
				tr.setName(name);
				obj.setTranslation(tr);
			}
		});
	}
}
