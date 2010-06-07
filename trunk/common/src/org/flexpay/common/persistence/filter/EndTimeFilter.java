package org.flexpay.common.persistence.filter;

import java.util.Calendar;
import java.util.Date;

public class EndTimeFilter extends TimeFilter {

    public EndTimeFilter() {
        initDefaults();
    }

	public EndTimeFilter(boolean withSec) {
        this.withSec = withSec;
        initDefaults();
	}

	public EndTimeFilter(Date dt, boolean withSec) {
		super(dt, withSec);
	}

	public EndTimeFilter(Calendar calendar, boolean withSec) {
		super(calendar, withSec);
	}

	@Override
	protected void initDefaults() {
		hours = 23;
		minutes = 59;
		seconds = 59;
	}
}
