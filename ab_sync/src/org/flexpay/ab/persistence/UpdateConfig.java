package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Date;

public class UpdateConfig extends DomainObject {

	private Date lastUpdateDate;
	private Date lastRecordUpdateTime;

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

	/**
	 * Getter for property 'lastRecordUpdateTime'.
	 *
	 * @return Value for property 'lastRecordUpdateTime'.
	 */
	public Date getLastRecordUpdateTime() {
		return lastRecordUpdateTime;
	}

	/**
	 * Setter for property 'lastRecordUpdateTime'.
	 *
	 * @param lastRecordUpdateTime Value to set for property 'lastRecordUpdateTime'.
	 */
	public void setLastRecordUpdateTime(Date lastRecordUpdateTime) {
		this.lastRecordUpdateTime = lastRecordUpdateTime;
	}
}
