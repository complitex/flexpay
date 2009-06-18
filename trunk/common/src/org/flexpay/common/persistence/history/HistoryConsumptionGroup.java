package org.flexpay.common.persistence.history;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.util.DateUtil;

import java.util.Date;

/**
 * Consumption group is a set of history records packed in single file
 */
public class HistoryConsumptionGroup extends DomainObject {

	/**
	 * Group is being creating
	 */
	public static final int STATUS_BUILDING = 0;

	/**
	 * Group has being cancelled
	 */
	public static final int STATUS_BUILDING_CANCELLED = -1;

	/**
	 * Group was created but was not send to consumer
	 */
	public static final int STATUS_CREATED = 1;

	/**
	 * Group was created and failed sending to consumer
	 */
	public static final int STATUS_SEND_FAILED = 2;

	/**
	 * Group was postponed
	 */
	public static final int STATUS_POSTPONED = 4;

	/**
	 * Group was created and was sent to consumer
	 */
	public static final int STATUS_SENT = 3;

	private Date creationDate = DateUtil.now();
	private String userName;
	private HistoryConsumer consumer;
	private FPFile file;
	private int sendTries;
	private int groupStatus = STATUS_BUILDING;
	private Date postponeTime;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public HistoryConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(HistoryConsumer consumer) {
		this.consumer = consumer;
	}

	public FPFile getFile() {
		return file;
	}

	public void setFile(FPFile file) {
		this.file = file;
	}

	public int getSendTries() {
		return sendTries;
	}

	public void setSendTries(int sendTries) {
		this.sendTries = sendTries;
	}

	public int getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(int groupStatus) {
		this.groupStatus = groupStatus;
	}

	public Date getPostponeTime() {
		return postponeTime;
	}

	public void setPostponeTime(Date postponeTime) {
		this.postponeTime = postponeTime;
	}

	public void created() {
		groupStatus = STATUS_CREATED;
	}

	public void nextTry() {
		++sendTries;
	}

	public void failSending() {
		groupStatus = STATUS_SEND_FAILED;
	}

	public void sent() {
		groupStatus = STATUS_SENT;
	}

	public void cancelBuilding() {
		file = null;
		groupStatus = STATUS_BUILDING_CANCELLED;
	}

	public void postpone() {
		groupStatus = STATUS_POSTPONED;
		postponeTime = new Date();
	}

	public boolean wasPostponed() {
		return groupStatus == STATUS_POSTPONED;
	}

	public void depostpone() {
		groupStatus = STATUS_SEND_FAILED;
		postponeTime = null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("creationDate", creationDate).
				append("userName", userName).
				append("consumer", consumer).
				append("file", file).
				append("sendTries", sendTries).
				append("groupStatus", groupStatus).
				toString();
	}
}
