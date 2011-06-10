package org.flexpay.bti.service.importexport;

import java.util.Date;

public interface BuildingAttributeDataProcessor {

	/**
	 * ProcessInstance attribute data for specified time interval
	 *
	 * @param begin Interval begin date
	 * @param end Interval end date
	 * @param data Attributes data
	 */
	void processData(Date begin, Date end, BuildingAttributeData data);
}
