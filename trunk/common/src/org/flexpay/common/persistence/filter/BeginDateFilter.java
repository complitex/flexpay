package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Date;

public class BeginDateFilter extends DateFilterBase {

	private Date beginDate;

	public BeginDateFilter() {
	}

	public BeginDateFilter(Date date) {
		super(date);
	}

	@Override
	public Date getDate() {
		return beginDate;
	}

	@Override
	public void setDate(Date date) {
        beginDate = date == null || date.compareTo(getEmptyDate()) <= 0 ? getEmptyDate() : date;
	}

	@Override
	protected Date getEmptyDate() {
		return ApplicationConfig.getPastInfinite();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("isReadonly", isReadOnly()).
				append("beginDate", beginDate).
				toString();
	}

}
