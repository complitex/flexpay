package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Date;

public class CloseDateFilter extends DateFilterBase {

	private Date closeDate;

	public CloseDateFilter() {
	}

	public CloseDateFilter(Date date) {
		super(date);
	}

	@Override
	public Date getDate() {
		return closeDate;
	}

	@Override
	public void setDate(Date date) {
		closeDate = date;
	}

	@Override
	protected Date getEmptyDate() {
		return ApplicationConfig.getFutureInfinite();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("isReadonly", isReadOnly()).
				append("closeDate", closeDate).
				toString();
	}

}
