package org.flexpay.common.process.sorter;

/**
* Implements process sorting by state
 */
public class ProcessSorterByState extends ProcessSorter {

	/**
	 * {@inheritDoc}
	 */
    @Override
	public void setOrderBy(StringBuilder orderByClause) {
		orderByClause.append(" pi.end ").append(getOrder()).append(", pi.start ").append(getOrder());
	}
}
