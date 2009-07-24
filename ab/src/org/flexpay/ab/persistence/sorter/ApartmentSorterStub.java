package org.flexpay.ab.persistence.sorter;

import org.flexpay.common.persistence.sorter.ObjectSorter;

/**
 * Sorter that does nothing
 */
public class ApartmentSorterStub extends ApartmentSorter {

	/**
	 * Add HQL addendum for FROM clause
	 *
	 * @param query HQL query to update
	 */
	@Override
	public void setFrom(StringBuilder query) {
	}

	/**
	 * Add HQL addendum for WHERE clause
	 *
	 * @param whereClause HQL query to update
	 */
	@Override
	public void setWhere(StringBuilder whereClause) {
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
