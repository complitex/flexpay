package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class HistoryRec {

	private Long id;
	private Long recordId;
	private Date recordDate;
	private String oldValue;
	private String currentValue;
	private Long objectId;
	private FieldType fieldType;
	private ObjectType objectType;
	private SyncAction syncAction;
	private int processed;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

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

	@Nullable
	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(@Nullable String currentValue) {
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
    @Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", id)
				.append("RecordId", recordId)
				.append("Date", recordDate)
				.append("Old", oldValue)
				.append("Current", currentValue)
				.append("Type", objectType)
				.append("ObjectId", objectId)
				.append("Field", fieldType)
				.append("Action", syncAction)
				.append("Processed", processed)
				.toString();
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof HistoryRec)) {
			return false;
		}

		HistoryRec that = (HistoryRec) obj;

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
