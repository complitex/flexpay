package org.flexpay.common.process.sorter;

/**
 * Implements process sorting by name 
 */
public class ProcessSorterByName extends ProcessSorter {

	/**
	 * {@inheritDoc}
	 */
    @Override
	public void setOrderBy(StringBuilder orderByClause) {
		orderByClause.append(" pi.processDefinition.name ").append(getOrder());
	}
}
