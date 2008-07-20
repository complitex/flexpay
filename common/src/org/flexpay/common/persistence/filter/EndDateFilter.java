package org.flexpay.common.persistence.filter;

import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Date;

public class EndDateFilter extends DateFilterBase {

	private Date endDate;

	public Date getDate() {
		return endDate;
	}

	public void setDate(Date date) {
		if (date == null || getEmptyDate().compareTo(date) < 0) {
			endDate = getEmptyDate();
		}

		endDate = date;
	}

	protected Date getEmptyDate() {
		return ApplicationConfig.getFutureInfinite();
	}
}