package org.flexpay.ab.persistence;

import java.util.Date;

public class HistoryRecord {

	private Date recordDate;
	private String oldValue;
	private String currentValue;
	private Long objectId;
	private FieldType fieldType;
	private ObjectType objectType;
	private SyncAction syncAction;

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public SyncAction getSyncAction() {
		return syncAction;
	}

	public void setSyncAction(SyncAction syncAction) {
		this.syncAction = syncAction;
	}
}
