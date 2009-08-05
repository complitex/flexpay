package org.flexpay.ab.persistence.sorter;

/**
 * Sorter that does nothing
 */
public class StreetSorterStub extends StreetSorter {

	/**
	 * Add HQL addendum for FROM clause
	 *
	 * @param query HQL query to update
	 */
	@Override
	public void setFrom(StringBuilder query) {
	}

	/**
	 * Add HQL addendum for ORDER BY clause
	 *
	 * @param query HQL query to update
	 */
	@Override
	public void setOrderBy(StringBuilder query) {
	}

	/**
	 * Add HQL addendum for WHERE clause
	 *
	 * @param query HQL query to update
	 */
	@Override
	public void setWhere(StringBuilder query) {
	}

}
