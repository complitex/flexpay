package org.flexpay.common.persistence.filter;

import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Date;

public class BeginDateFilter extends DateFilterBase {

	private Date beginDate;

	public Date getDate() {
		return beginDate;
	}

	public void setDate(Date date) {
		if (date == null || date.compareTo(getEmptyDate()) < 0) {
			beginDate = getEmptyDate();
		}
		beginDate = date;
	}

	protected Date getEmptyDate() {
		return ApplicationConfig.getPastInfinite();
	}
}
