package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.Date;

public class HistoryRecord {

	private Date recordDate;
	private String oldValue;
	private String currentValue;
	private Long objectId;
	private FieldType fieldType;
	private ObjectType objectType;
	private SyncAction syncAction;
	private int processed;

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

	public int getProcessed() {
		return processed;
	}

	public void setProcessed(int processed) {
		this.processed = processed;
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object.
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("Date", recordDate)
				.append("Old", oldValue)
				.append("Current", currentValue)
				.append("Type", objectType)
				.append("Id", objectId)
				.append("Field", fieldType)
				.append("Action", syncAction)
				.append("Processed", processed)
				.toString();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof HistoryRecord)) {
			return false;
		}

		HistoryRecord that = (HistoryRecord) obj;

		return new EqualsBuilder()
				.append(recordDate, that.recordDate)
				.append(oldValue, that.oldValue)
				.append(currentValue, that.currentValue)
				.append(objectType, that.objectType)
				.append(objectId, that.objectId)
				.append(fieldType, that.fieldType)
				.append(syncAction, that.syncAction)
				.isEquals();
	}
}
