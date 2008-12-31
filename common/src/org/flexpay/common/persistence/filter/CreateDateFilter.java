package org.flexpay.common.persistence.filter;

import org.flexpay.common.util.DateUtil;

import java.util.Date;

public class CreateDateFilter extends DateFilterBase {

	private Date createDate;

	public CreateDateFilter() {
	}

	public CreateDateFilter(Date date) {
		super(date);
	}

	public Date getDate() {
		return createDate;
	}

	public void setDate(Date date) {
		createDate = date;
	}

	protected Date getEmptyDate() {
		return DateUtil.now();
	}
}