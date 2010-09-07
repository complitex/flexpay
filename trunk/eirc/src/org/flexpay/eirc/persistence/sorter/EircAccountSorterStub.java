package org.flexpay.eirc.persistence.sorter;

/**
 * Sorter that does nothing
 */
public class EircAccountSorterStub extends EircAccountSorter {

	@Override
	public void setFrom(StringBuilder query) {
	}

	/**
	 * Add HQL addendum for WHERE clause
	 *
	 * @param query HQL query to update
	 */
	@Override
	public void setWhere(StringBuilder query) {
	}

	/**
	 * Add HQL addendum for ORDER BY clause
	 *
	 * @param orderByClause HQL query to update
	 */
	@Override
	public void setOrderBy(StringBuilder orderByClause) {
	}

    /**
     * Add SQL addendum for ORDER BY clause
     *
     * @param orderByClause SQL query to update
     */
    @Override
    public void setOrderBySQL(StringBuilder orderByClause) {
    }
}
