package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Date;

public abstract class TemporalValueObject extends ValueObject {

	private Date begin;
	private Date end;

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		if (begin.before(ApplicationConfig.getPastInfinite())) {
			begin = ApplicationConfig.getPastInfinite();
		}
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		if (end.after(ApplicationConfig.getFutureInfinite())) {
			end = ApplicationConfig.getFutureInfinite();
		}
		this.end = end;
	}

	@Override
	protected ToStringBuilder buildToString(ToStringBuilder builder) {
		return builder
				.append("begin", begin)
				.append("end", end);
	}
}
