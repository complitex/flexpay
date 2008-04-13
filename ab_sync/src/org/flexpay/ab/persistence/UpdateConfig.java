package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Date;

public class UpdateConfig extends DomainObject {

	private Date lastUpdateDate;
	private Long lastDumpedRecordId;

	/**
	 * Getter for property 'lastUpdateDate'.
	 *
	 * @return Value for property 'lastUpdateDate'.
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	/**
	 * Setter for property 'lastUpdateDate'.
	 *
	 * @param lastUpdateDate Value to set for property 'lastUpdateDate'.
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Long getLastDumpedRecordId() {
		return lastDumpedRecordId;
	}

	public void setLastDumpedRecordId(Long lastDumpedRecordId) {
		this.lastDumpedRecordId = lastDumpedRecordId;
	}
}
