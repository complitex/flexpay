package org.flexpay.bti.service.history;

import org.flexpay.bti.persistence.building.*;
import org.flexpay.bti.service.BuildingAttributeGroupService;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Iterator;
import java.util.Map;

public class BuildingAttributeTypeHistoryBuilder extends HistoryBuilderBase<BuildingAttributeType> {

	public static final int FIELD_NAME = 1;
	public static final int FIELD_GROUP_ID = 2;
	public static final int FIELD_UNIQUE_CODE = 3;
	public static final int FIELD_TEMPORAL_FLAG = 4;
	public static final int FIELD_ENUM_VALUE = 5;

	protected Logger log = LoggerFactory.getLogger(getClass());

	private BuildingAttributeGroupService attributeGroupService;

	/**
	 * Build necessary diff records, diff is a set of translation differences
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
    @Override
	protected void doDiff(@Nullable BuildingAttributeType t1, @NotNull BuildingAttributeType t2, @NotNull Diff diff) {

		log.debug("creating new building attribute types diff");
		if (!t2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		BuildingAttributeType old = t1 == null ? new BuildingAttributeTypeSimple() : t1;

		buildAttributesDiff(old, t2, diff);
		buildNameDiff(old, t2, diff);
		buildGroupRefDiff(old, t2, diff);
		buildEnumValuesDiff(old, t2, diff);
	}

	private boolean sameEnumValues(BuildingAttributeTypeEnumValue v1, BuildingAttributeTypeEnumValue v2) {
		return (v1 == null && v2 == null) ||
			   (v1 != null && v2 != null) &&
			   v1.getOrder() == v2.getOrder() &&
			   EqualsHelper.strEquals(v1.getValue(), v2.getValue());

	}

	private void buildEnumValuesDiff(@NotNull BuildingAttributeType t1, @NotNull BuildingAttributeType t2, @NotNull Diff diff) {

		boolean t1IsEnum = t1 instanceof BuildingAttributeTypeEnum;
		boolean t2IsEnum = t2 instanceof BuildingAttributeTypeEnum;
		if (!t1IsEnum && !t2IsEnum) {
			return;
		}
		BuildingAttributeTypeEnum e1 = t1IsEnum ?
									   (BuildingAttributeTypeEnum) t1 : new BuildingAttributeTypeEnum();
		BuildingAttributeTypeEnum e2 = t2IsEnum ?
									   (BuildingAttributeTypeEnum) t2 : new BuildingAttributeTypeEnum();

		Iterator<BuildingAttributeTypeEnumValue> it1 = e1.getSortedValues().iterator();
		Iterator<BuildingAttributeTypeEnumValue> it2 = e2.getSortedValues().iterator();
		while (it1.hasNext() || it2.hasNext()) {
			BuildingAttributeTypeEnumValue v1 = it1.hasNext() ? it1.next() : null;
			BuildingAttributeTypeEnumValue v2 = it2.hasNext() ? it2.next() : null;

			if (!sameEnumValues(v1, v2)) {
				HistoryRecord rec = new HistoryRecord();
				rec.setFieldType(FIELD_ENUM_VALUE);
				rec.setOldStringValue(v1 == null ? null : v1.getValue());
				rec.setNewStringValue(v2 == null ? null : v2.getValue());
				rec.setOldIntValue(v1 == null ? null : v1.getOrder());
				rec.setNewIntValue(v2 == null ? null : v2.getOrder());
				diff.addRecord(rec);
				log.debug("Added enum diff: {}", rec);
			}
		}
	}

	private void buildAttributesDiff(@NotNull BuildingAttributeType t1, @NotNull BuildingAttributeType t2, @NotNull Diff diff) {

		if (!EqualsHelper.strEquals(t1.getUniqueCode(), t2.getUniqueCode())) {
			HistoryRecord rec = new HistoryRecord();
			rec.setFieldType(FIELD_UNIQUE_CODE);
			rec.setOldStringValue(t1.getUniqueCode());
			rec.setNewStringValue(t2.getUniqueCode());
			diff.addRecord(rec);
			log.debug("Added unique code diff: {}", rec);
		}

		if (t1.isTemp() != t2.isTemp()) {
			HistoryRecord rec = new HistoryRecord();
			rec.setFieldType(FIELD_TEMPORAL_FLAG);
			rec.setOldBoolValue(t1.isTemp());
			rec.setNewBoolValue(t2.isTemp());
			diff.addRecord(rec);
			log.debug("Added temporal flag diff: {}", rec);
		}
	}

	private void buildNameDiff(BuildingAttributeType t1, BuildingAttributeType t2, Diff diff) {

		builderHelper.buildTranslationDiff(t1, t2, diff, new TranslationExtractor<Translation, BuildingAttributeType>() {
            @Override
			public Translation getTranslation(BuildingAttributeType obj, @NotNull Language language) {
				return obj.getTranslation(language);
			}

            @Override
			public int getTranslationField() {
				return FIELD_NAME;
			}
		});
	}

	private void buildGroupRefDiff(BuildingAttributeType t1, BuildingAttributeType t2, Diff diff) {

		builderHelper.buildReferenceDiff(t1, t2, diff, new ReferenceExtractor<BuildingAttributeGroup, BuildingAttributeType>() {
            @Override
			public BuildingAttributeGroup getReference(BuildingAttributeType obj) {
				return obj.getGroup();
			}

            @Override
			public int getReferenceField() {
				return FIELD_GROUP_ID;
			}
		});
	}

	/**
	 * Apply diff to an object
	 *
	 * @param t	Object to apply diff to
	 * @param diff Diff to apply
	 */
    @Override
	public void patch(@NotNull BuildingAttributeType t, @NotNull Diff diff) {

		PatchContext context = new PatchContext();
		for (HistoryRecord record : diff.getHistoryRecords()) {

			switch (record.getFieldType()) {
				case FIELD_NAME:
					patchName(t, record);
					break;
				case FIELD_GROUP_ID:
					patchGroupReference(t, record);
					break;
				case FIELD_TEMPORAL_FLAG:
					Boolean bValue = record.getNewBoolValue();
					t.setTemp(bValue == null ? false : bValue);
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				case FIELD_UNIQUE_CODE:
					t.setUniqueCode(record.getNewStringValue());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				case FIELD_ENUM_VALUE:
					patchEnumValue(t, record, context);
					break;
				default:
					log.info("Unsupported record: {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}

		postPatch(t, context);
	}

	private void postPatch(BuildingAttributeType type, PatchContext context) {
		if (context.values != null) {
			BuildingAttributeTypeEnum typeEnum = (BuildingAttributeTypeEnum) type;
			typeEnum.rawValues(context.values);
		}
	}

	private void patchEnumValue(BuildingAttributeType type, HistoryRecord record, PatchContext context) {

		BuildingAttributeTypeEnum typeEnum = (BuildingAttributeTypeEnum) type;
		if (context.values == null) {
			context.values = typeEnum.rawValues();
		}
		context.values.put(record.getNewIntValueNotNull(), record.getNewStringValue());
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	private void patchName(BuildingAttributeType type, HistoryRecord record) {

		builderHelper.patchTranslation(type, record, new TranslationPatcher<BuildingAttributeTypeName, BuildingAttributeType>() {
            @Override
			public BuildingAttributeTypeName getNotNullTranslation(BuildingAttributeType obj, @NotNull Language language) {
				BuildingAttributeTypeName tr = obj.getTranslation(language);
				return tr == null ? new BuildingAttributeTypeName() : tr;
			}

            @Override
			public void setTranslation(BuildingAttributeType obj, BuildingAttributeTypeName tr, String name) {
				tr.setName(name);
				obj.setTranslation(tr);
			}
		});
	}

	private void patchGroupReference(@NotNull BuildingAttributeType type, @NotNull HistoryRecord record) {
		log.debug("Patching attribute group reference {}", record);

		builderHelper.patchReference(type, record, new ReferencePatcher<BuildingAttributeGroup, BuildingAttributeType>() {
            @Override
			public Class<BuildingAttributeGroup> getType() {
				return BuildingAttributeGroup.class;
			}

            @Override
			public void setReference(BuildingAttributeType obj, Stub<BuildingAttributeGroup> ref) {
				if (ref != null) {
					BuildingAttributeGroup group = attributeGroupService.readFull(ref);
					obj.setGroup(group);
				} else {
					obj.setGroup(null);
				}
			}
		});
	}

	private static final class PatchContext {

		private Map<Integer, String> values;
	}

	@Required
	public void setAttributeGroupService(BuildingAttributeGroupService attributeGroupService) {
		this.attributeGroupService = attributeGroupService;
	}
}
