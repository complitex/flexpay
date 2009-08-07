package org.flexpay.common.persistence.history;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Diff is a set of history records applied to a single DomainObject
 */
public class Diff extends DomainObject {

	private Date operationTime;
	private Integer objectType;
	private String objectTypeName;
	private Long objectId;
	private int operationType;
	private String userName;
	private int processingStatus;
	private String masterIndex;
	private String instanceId;

	private List<HistoryRecord> historyRecords = Collections.emptyList();

	/**
	 * Constructs a new DomainObject.
	 */
	public Diff() {
	}

	public Diff(@NotNull Long id) {
		super(id);
	}

	public Diff(@NotNull Stub<Diff> stub) {
		super(stub.getId());
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public Integer getObjectType() {
		return objectType;
	}

	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<HistoryRecord> getHistoryRecords() {
		return historyRecords;
	}

	public void setHistoryRecords(List<HistoryRecord> historyRecords) {
		this.historyRecords = historyRecords;
	}

	public int getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(int processingStatus) {
		this.processingStatus = processingStatus;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	public String getMasterIndex() {
		return masterIndex;
	}

	public void setMasterIndex(String masterIndex) {
		this.masterIndex = masterIndex;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(@NotNull String instanceId) {
		this.instanceId = instanceId;
	}

	public String getObjectTypeName() {
		return objectTypeName;
	}

	public void setObjectTypeName(String objectTypeName) {
		this.objectTypeName = objectTypeName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("id", getId()).
				append("operationTime", operationTime).
				append("operationType", operationType).
				append("objectType", objectType).
				append("objectTypeName", objectTypeName).
				append("objectId", objectId).
				append("userName", userName).
				append("masterIndex", masterIndex).
				append("instanceId", instanceId).
				append("processingStatus", processingStatus).
				toString();
	}

	/**
	 * Add a new record to this diff
	 *
	 * @param rec HistoryRecord to add
	 */
	public void addRecord(@NotNull HistoryRecord rec) {
		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (historyRecords == Collections.EMPTY_LIST) {
			historyRecords = CollectionUtils.list();
		}

		rec.setOperationOrder(historyRecords.size());
		rec.setDiff(this);
		historyRecords.add(rec);
	}

	public boolean isEmpty() {
		return historyRecords.isEmpty();
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

	public int size() {
		return historyRecords.size();
	}

	/**
	 * Create a copy without history records
	 *
	 * @return Diff copy
	 */
	public Diff copy() {
		Diff copy = new Diff();
		copy.setId(getId());
		copy.setOperationTime(operationTime);
		copy.setObjectType(objectType);
		copy.setObjectTypeName(objectTypeName);
		copy.setObjectId(objectId);
		copy.setOperationType(operationType);
		copy.setUserName(userName);
		copy.setProcessingStatus(processingStatus);
		copy.setMasterIndex(masterIndex);
		copy.setInstanceId(instanceId);
		return copy;
	}
}
