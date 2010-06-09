package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Calendar;
import java.util.Date;

public class EndDateFilter extends DateFilterBase {

	private Date endDate;

	public EndDateFilter() {
	}

	public EndDateFilter(Date date) {
		super(date);
	}

	@Override
	public Date getDate() {
		return endDate;
	}

	@Override
	public void setDate(Date date) {

        endDate = date == null || getEmptyDate().compareTo(date) <= 0 ? getEmptyDate() : date;

        if (getEmptyDate().compareTo(date) > 0) {
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate);
            endCal.set(Calendar.HOUR, 23);
            endCal.set(Calendar.MINUTE, 59);
            endCal.set(Calendar.SECOND, 59);
            endCal.set(Calendar.MILLISECOND, 999);
            endDate = endCal.getTime();
        } else {
            endDate = date;
        }

	}

	@Override
	protected Date getEmptyDate() {
		return ApplicationConfig.getFutureInfinite();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("isReadonly", isReadOnly()).
				append("endDate", endDate).
				toString();
	}

}
