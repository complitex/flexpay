package org.flexpay.eirc.service;

import java.util.Date;

public interface ReportPeriodService {

	/**
	 * Check if the <code>date</code> is in open report period
	 *
	 * @param date Date to check
	 * @return <code>true</code> if the <code>date</code> is in open period, or
	 *         <code>false</code> otherwise
	 */
	boolean isInOpenPeriod(Date date);
}
