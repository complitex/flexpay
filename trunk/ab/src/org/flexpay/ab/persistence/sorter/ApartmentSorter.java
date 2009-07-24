package org.flexpay.ab.persistence.sorter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
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
	@Override
	public void setFrom(StringBuilder query) {
		query
			.append(" left join ").append(apartmentField).append(".apartmentNumbers sortApartmentNumber ");
	}

	/**
	 * Add HQL addendum for WHERE clause
	 *
	 * @param whereClause HQL query to update
	 */
	@Override
	public void setWhere(StringBuilder whereClause) {
		if (whereClause.length() > 0) {
			whereClause.append(" and ");
		}

		whereClause
				.append("(sortApartmentNumber.begin <= current_date() and sortApartmentNumber.end > current_date()) ");
	}

	/**
	 * Add HQL addendum for ORDER BY clause
	 *
	 * @param orderByClause HQL query to update
	 */
	@Override
	public void setOrderBy(StringBuilder orderByClause) {

		if (orderByClause.length() > 0) {
			orderByClause.append(",");
		}

		orderByClause.append(" lpad(convert(ifnull(sortApartmentNumber.value, '0'), UNSIGNED), 10, '0') ")
				.append(getOrder());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("order", getOrder()).
				append("active", getActive()).
				append("apartmentField", apartmentField).
				toString();
	}

}
