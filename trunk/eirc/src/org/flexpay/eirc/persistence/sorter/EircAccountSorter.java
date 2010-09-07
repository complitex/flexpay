package org.flexpay.eirc.persistence.sorter;

import org.flexpay.common.persistence.sorter.ObjectSorter;

public abstract class EircAccountSorter extends ObjectSorter {

	/**
	 * {@inheritDoc}
	 */
    @Override
	public void setFrom(StringBuilder query) {}

    /**
     * Add SQL addendum for ORDER BY clause
     *
     * @param orderByClause SQL query to update
     */
    public abstract void setOrderBySQL(StringBuilder orderByClause);

}
