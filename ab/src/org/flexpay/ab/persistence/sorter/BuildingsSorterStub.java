package org.flexpay.ab.persistence.sorter;

/**
 * Sorter that does nothing
 */
public class BuildingsSorterStub extends BuildingsSorter {

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
	 * @param orderByClause HQL query to update
	 */
	@Override
	public void setOrderBy(StringBuilder orderByClause) {
	}
}
