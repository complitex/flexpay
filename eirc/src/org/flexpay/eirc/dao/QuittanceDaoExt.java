package org.flexpay.eirc.dao;

import java.util.Date;

/**
 * Interface provides functionality to prepare quittances
 */
public interface QuittanceDaoExt {

	/**
	 * Generate current snapshot of details and create quittances for the following processing
	 *
	 * @param dateFrom Period begin date
	 * @param dateTill Period end date
	 * @return number of generated quittances
	 */
	long createQuittances(Date dateFrom, Date dateTill);
}