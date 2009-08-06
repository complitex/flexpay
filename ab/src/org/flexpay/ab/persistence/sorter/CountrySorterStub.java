package org.flexpay.ab.persistence.sorter;

/**
 * Sorter that does nothing
 */
public class CountrySorterStub extends CountrySorter {

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
}
