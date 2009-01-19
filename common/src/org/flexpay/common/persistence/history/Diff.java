package org.flexpay.common.persistence.history;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Diff is a set of history records applyed to a single DomainObject
 */
public class Diff extends DomainObjectWithStatus {

	private Date operationTime;
	private Integer objectType;
	private Long objectId;
	private int operationType;
	private String userName;
	private boolean processed;

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

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("id", getId()).
				append("operationTime", operationTime).
				append("operationType", operationType).
				append("objectType", objectType).
				append("objectId", objectId).
				append("userName", userName).
				toString();
	}

	/**
	 * Add a new record to this diff
	 *
	 * @param rec HistoryRecord to add
	 */
	public void addRecord(@NotNull HistoryRecord rec) {
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

	public int size() {
		return historyRecords.size();
	}
}
