package org.flexpay.bti.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.history.BuildingHistoryBuilder;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.persistence.building.BuildingAttribute;
import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.bti.service.importexport.impl.ClassToTypeRegistryBti;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.TemporalObjectsHistoryBuildHelper;
import org.flexpay.common.persistence.history.TemporalObjectsHistoryBuildHelper.TemporalDataExtractor;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.Set;
import java.util.SortedSet;

import static org.flexpay.common.persistence.ValueObject.*;

public class BtiBuildingHistoryBuilder extends BuildingHistoryBuilder {

	public static final int FIELD_ATTRIBUTE = ClassToTypeRegistryBti.MODULE_BASE + 1;

	private BuildingAttributeTypeService attributeTypeService;

	/**
	 * Do any module specific diff building.
	 * <p/>
	 * Point to extension.
	 *
	 * @param b1   First building
	 * @param b2   Second building
	 * @param diff Diff to store history records in
	 */
	@Override
	protected void doInstanceDiff(@NotNull Building b1, @NotNull Building b2, @NotNull Diff diff) {
		super.doInstanceDiff(b1, b2, diff);

		buildAttributesDiff((BtiBuilding) b1, (BtiBuilding) b2, diff);
	}

	private void buildAttributesDiff(BtiBuilding b1, BtiBuilding b2, Diff diff) {
		Set<BuildingAttributeType> types = CollectionUtils.set(b1.attributeTypes(), b2.attributeTypes());
		for (BuildingAttributeType type : types) {
			buildTypedAttributesDiff(b1, b2, type, diff);
		}
	}

	private void buildTypedAttributesDiff(BtiBuilding b1, BtiBuilding b2, BuildingAttributeType type, Diff diff) {

		SortedSet<BuildingAttribute> attrs1 = b1.attributesOfType(type);
		SortedSet<BuildingAttribute> attrs2 = b2.attributesOfType(type);
		TemporalObjectsHistoryBuildHelper.buildDiff(new TemporalDataExtractor<BuildingAttribute>() {
            @Override
			public Date getBeginDate(BuildingAttribute obj) {
				return obj.getBegin();
			}

            @Override
			public Date getEndDate(BuildingAttribute obj) {
				return obj.getEnd();
			}

            @Override
			public void buildDiff(Date begin, Date end, BuildingAttribute t1, BuildingAttribute t2, Diff df) {
				addAttributeDiff(begin, end, t1, t2, df);
			}
		}, attrs1, attrs2, diff);
	}

	private void addAttributeDiff(Date begin, Date end, BuildingAttribute n1, BuildingAttribute n2, Diff diff) {

		assert n1 != null || n2 != null;

		if (n1 != null && n2 != null && n1.sameValue(n2)) {
			return;
		}

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_ATTRIBUTE);
		rec.setBeginDate(begin);
		rec.setEndDate(end);

		BuildingAttributeType type = n1 == null ? n2.getAttributeType() : n1.getAttributeType();
		rec.setFieldKey(masterIndexService.getMasterIndex(type));
		rec.setFieldKey2(n1 == null ? "0" : String.valueOf(n1.getValueType()));
		rec.setFieldKey3(n2 == null ? "0" : String.valueOf(n2.getValueType()));

		// set old and new value based on their types
		switch (n1 == null ? -1 : n1.getValueType()) {
			case TYPE_BOOLEAN:
				rec.setOldBoolValue(n1 != null ? n1.isBoolValue() : null); break;
			case TYPE_INT:
				rec.setOldIntValue(n1 != null ? n1.getIntValue() : null); break;
			case TYPE_LONG:
				rec.setOldLongValue(n1 != null ? n1.getLongValue() : null); break;
			case TYPE_STRING:
				rec.setOldStringValue(n1 != null ? n1.getStringValue() : null); break;
			case TYPE_DATE:
				rec.setOldDateValue(n1 != null ? n1.getDateValue() : null); break;
			case TYPE_DOUBLE:
				rec.setOldDoubleValue(n1 != null ? n1.getDoubleValue() : null); break;
			case TYPE_DECIMAL:
				rec.setOldDecimalValue(n1 != null ? n1.getDecimalValue() : null); break;
		}
		switch (n2 == null ? -1 : n2.getValueType()) {
			case TYPE_BOOLEAN:
				rec.setNewBoolValue(n2 != null ? n2.isBoolValue() : null); break;
			case TYPE_INT:
				rec.setNewIntValue(n2 != null ? n2.getIntValue() : null); break;
			case TYPE_LONG:
				rec.setNewLongValue(n2 != null ? n2.getLongValue() : null); break;
			case TYPE_STRING:
				rec.setNewStringValue(n2 != null ? n2.getStringValue() : null); break;
			case TYPE_DATE:
				rec.setNewDateValue(n2 != null ? n2.getDateValue() : null); break;
			case TYPE_DOUBLE:
				rec.setNewDoubleValue(n2 != null ? n2.getDoubleValue() : null); break;
			case TYPE_DECIMAL:
				rec.setNewDecimalValue(n2 != null ? n2.getDecimalValue() : null); break;
		}
		diff.addRecord(rec);

		log.debug("Attribute diff {}", rec);
	}

	/**
	 * Do any module specific patch operations.
	 * <p/>
	 * Point to extension.
	 *
	 * @param building Building to patch
	 * @param record   History record to process
	 */
	@Override
	protected void doInstancePatch(@NotNull Building building, HistoryRecord record) {
		super.doInstancePatch(building, record);

		if (record.getFieldType() == FIELD_ATTRIBUTE) {
			patchAttribute((BtiBuilding) building, record);
		}
	}

	private void patchAttribute(BtiBuilding building, HistoryRecord record) {
		log.debug("Patching attribute {}", record);
		Stub<BuildingAttributeType> typeStub = correctionsService.findCorrection(
				record.getFieldKey(), BuildingAttributeType.class, masterIndexService.getMasterSourceDescriptionStub());
		if (typeStub == null) {
			throw new IllegalStateException("Cannot find type by master index " + record.getFieldKey());
		}
		BuildingAttributeType type = attributeTypeService.readFull(typeStub);
		if (type == null) {
			throw new IllegalStateException("Cannot find type by existing correction " + typeStub);
		}
		BuildingAttribute attribute = new BuildingAttribute();
		attribute.setAttributeType(type);
		switch (Integer.valueOf(record.getFieldKey3())) {
			case TYPE_BOOLEAN:
				attribute.setBoolValue(record.getNewBoolValue());
				break;
			case TYPE_INT:
				attribute.setIntValue(record.getNewIntValue()); break;
			case TYPE_LONG:
				attribute.setLongValue(record.getNewLongValue()); break;
			case TYPE_STRING:
				attribute.setStringValue(record.getNewStringValue()); break;
			case TYPE_DATE:
				attribute.setDateValue(record.getNewDateValue()); break;
			case TYPE_DOUBLE:
				attribute.setDoubleValue(record.getNewDoubleValue()); break;
			case TYPE_DECIMAL:
				attribute.setDecimalValue(record.getNewDecimalValue()); break;
			default:
				record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
				return;
		}
		// try to guess attribute temporal type
		boolean temporal = type.isTemp() ||
						   !ApplicationConfig.getPastInfinite().equals(record.getBeginDate()) ||
						   !ApplicationConfig.getFutureInfinite().equals(record.getEndDate());
		if (temporal) {
			building.setTmpAttributeForDates(attribute, record.getBeginDate(), record.getEndDate());
		} else {
			building.setNormalAttribute(attribute);
		}
		record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
	}

	/**
	 * Check if module can handle field processing
	 * <p/>
	 * Point to extension.
	 *
	 * @param fieldType History record to process
	 * @return <code>true</code>
	 */
	@Override
	protected boolean supportsField(int fieldType) {
		return fieldType == FIELD_ATTRIBUTE || super.supportsField(fieldType);
	}

	@Required
	public void setAttributeTypeService(BuildingAttributeTypeService attributeTypeService) {
		this.attributeTypeService = attributeTypeService;
	}
}
