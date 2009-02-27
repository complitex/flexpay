package org.flexpay.common.persistence.history;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.util.DateUtil;

import java.util.Date;

/**
 * Consumption group is a set of history records packed in single file
 */
public class HistoryConsumptionGroup extends DomainObject {

	private Date creationDate = DateUtil.now();
	private String userName;
	private HistoryConsumer consumer;

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

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("id", getId()).
				append("creationDate", creationDate).
				append("userName", userName).
				append("consumer", consumer).
				toString();
	}
}
