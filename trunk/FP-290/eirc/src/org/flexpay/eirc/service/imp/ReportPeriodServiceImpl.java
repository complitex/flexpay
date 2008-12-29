package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.service.ReportPeriodService;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.Calendar;

public class ReportPeriodServiceImpl implements ReportPeriodService {

	/**
	 * Check if the <code>date</code> is in open report period
	 *
	 * @param date Date to check
	 * @return <code>true</code> if the <code>date</code> is in open period, or
	 *         <code>false</code> otherwise
	 */
	public boolean isInOpenPeriod(Date date) {
		Date periodBegin = DateUtils.truncate(new Date(), Calendar.MONTH);
		return periodBegin.before(date);
	}
}
