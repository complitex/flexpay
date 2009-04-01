package org.flexpay.common.process.sorter;

/**
 * Implements process sorting by start date
 */
public class ProcessSorterByStartDate extends ProcessSorter {

	/**
	 * {@inheritDoc}
	 */
	public void setOrderBy(StringBuilder orderByClause) {
		orderByClause.append(" pi.start ").append(getOrder());
	}
}
