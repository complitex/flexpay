package org.flexpay.common.persistence.filter;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Pavel Sknar
 *         Date: 02.11.11 18:16
 */
public class BeginTimeWithLimitMinTimeFilter extends BeginTimeFilter {

	private BeginTimeFilter minTime = new BeginTimeFilter();

	public BeginTimeWithLimitMinTimeFilter() {
	}

	public BeginTimeWithLimitMinTimeFilter(boolean withSec) {
		super(withSec);
	}

	public BeginTimeWithLimitMinTimeFilter(Date dt, boolean withSec) {
		super(dt, withSec);
	}

	public BeginTimeWithLimitMinTimeFilter(Calendar calendar, boolean withSec) {
		super(calendar, withSec);
	}

	public void setMinTime(BeginTimeFilter minTime) {
		this.minTime = minTime;
	}

	public BeginTimeFilter getMinTime() {
		return minTime;
	}
}
