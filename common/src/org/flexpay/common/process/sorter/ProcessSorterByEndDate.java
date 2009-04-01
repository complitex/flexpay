package org.flexpay.common.process.sorter;

/**
 * Implements process sorting by end date
 */
public class ProcessSorterByEndDate extends ProcessSorter {

	/**
	 * {@inheritDoc}
	 */
	public void setOrderBy(StringBuilder orderByClause) {
		orderByClause.append(" pi.end ").append(getOrder());
	}
}
