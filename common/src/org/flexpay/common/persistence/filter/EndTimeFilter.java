package org.flexpay.common.persistence.filter;

import java.util.Calendar;
import java.util.Date;

public class EndTimeFilter extends TimeFilter {

	public EndTimeFilter() {
		hours = 23;
		minutes = 59;
		seconds = 59;
	}

	public EndTimeFilter(Date dt) {
		super(dt);
	}

	public EndTimeFilter(Calendar calendar) {
		super(calendar);
	}

	protected void initDefaults() {
		hours = 23;
		minutes = 59;
		seconds = 59;
	}
}
