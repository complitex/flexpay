package org.flexpay.common.persistence.filter;

import java.util.Calendar;
import java.util.Date;

public class BeginTimeFilter extends TimeFilter {

	public BeginTimeFilter() {
		hours = 0;
		minutes = 0;
		seconds = 0;
	}

	public BeginTimeFilter(Date dt) {
		super(dt);
	}

	public BeginTimeFilter(Calendar calendar) {
		super(calendar);
	}

	protected void initDefaults() {
		hours = 0;
		minutes = 0;
		seconds = 0;
	}

}
