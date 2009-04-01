package org.flexpay.common.process.sorter;

/**
 * Implements process sorting by name 
 */
public class ProcessSorterByName extends ProcessSorter {

	/**
	 * {@inheritDoc}
	 */
	public void setOrderBy(StringBuilder orderByClause) {
		orderByClause.append(" pd.name ").append(getOrder());
	}
}
