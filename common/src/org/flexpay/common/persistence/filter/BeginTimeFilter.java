package org.flexpay.common.persistence.filter;

import java.util.Calendar;
import java.util.Date;

public class BeginTimeFilter extends TimeFilter {

    public BeginTimeFilter() {
        initDefaults();
    }

	public BeginTimeFilter(boolean withSec) {
        this.withSec = withSec;
        initDefaults();
	}

	public BeginTimeFilter(Date dt, boolean withSec) {
		super(dt, withSec);
	}

	public BeginTimeFilter(Calendar calendar, boolean withSec) {
		super(calendar, withSec);
	}

	@Override
	protected void initDefaults() {
		hours = 0;
		minutes = 0;
		seconds = 0;
	}

}
