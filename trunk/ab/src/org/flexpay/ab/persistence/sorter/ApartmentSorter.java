package org.flexpay.ab.persistence.sorter;

import org.flexpay.common.persistence.sorter.ObjectSorter;

public class ApartmentSorter extends ObjectSorter {

	private String apartmentField;

	public void setApartmentField(String apartmentField) {
		this.apartmentField = apartmentField;
	}

	/**
	 * Add HQL addendum for FROM clause
	 *
	 * @param query HQL query to update
	 */
	public void setFrom(StringBuilder query) {
		query
			.append(" left join ").append(apartmentField).append(".apartmentNumbers sortApartmentNumber ");
	}

	/**
	 * Add HQL addendum for WHERE clause
	 *
	 * @param whereClause HQL query to update
	 */
	public void setWhere(StringBuilder whereClause) {
		if (whereClause.length() > 0) {
			whereClause.append(" and ");
		}

		whereClause.append(" (")
				.append("sortApartmentNumber.begin <= current_date() and sortApartmentNumber.end > current_date()");
	}

	/**
	 * Add HQL addendum for ORDER BY clause
	 *
	 * @param orderByClause HQL query to update
	 */
	public void setOrderBy(StringBuilder orderByClause) {

		if (orderByClause.length() > 0) {
			orderByClause.append(",");
		}

		orderByClause.append(" convert(ifnull(sortApartmentNumber.value, '0'), UNSIGNED) ")
				.append(getOrder());
	}
}
