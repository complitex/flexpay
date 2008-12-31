package org.flexpay.common.persistence.filter;

import org.flexpay.common.util.DateUtil;

import java.util.Date;

public class CloseDateFilter extends DateFilterBase {

	private Date closeDate;

	public CloseDateFilter() {
	}

	public CloseDateFilter(Date date) {
		super(date);
	}

	public Date getDate() {
		return closeDate;
	}

	public void setDate(Date date) {
		closeDate = date;
	}

	protected Date getEmptyDate() {
		return DateUtil.now();
	}
}
