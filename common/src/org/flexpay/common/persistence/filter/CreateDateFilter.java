package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.util.DateUtil;

import java.util.Date;

public class CreateDateFilter extends DateFilterBase {

	private Date createDate;

	public CreateDateFilter() {
	}

	public CreateDateFilter(Date date) {
		super(date);
	}

	@Override
	public Date getDate() {
		return createDate;
	}

	@Override
	public void setDate(Date date) {
		createDate = date;
	}

	@Override
	protected Date getEmptyDate() {
		return DateUtil.now();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("isReadonly", isReadOnly()).
				append("createDate", createDate).
				toString();
	}

}
