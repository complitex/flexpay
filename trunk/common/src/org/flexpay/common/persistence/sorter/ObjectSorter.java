package org.flexpay.common.persistence.sorter;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Base class for HQL sorters
 */
public abstract class ObjectSorter implements Serializable {

	public static final String ORDER_ASC = "ASC";
	public static final String ORDER_DESC = "DESC";

	public static final int ACTIVE = 1;
	public static final int INACTIVE = 0;

	private String order = ORDER_ASC;
	private int active = INACTIVE;

	/**
	 * Add HQL addendum for FROM clause
	 *
	 * @param query HQL query to update
	 */
	public abstract void setFrom(StringBuilder query);

	/**
	 * Add HQL addendum for WHERE clause
	 *
	 * @param whereClause HQL query to update
	 */
	public void setWhere(StringBuilder whereClause) {

	}

	/**
	 * Add HQL addendum for ORDER BY clause
	 *
	 * @param orderByClause HQL query to update
	 */
	public abstract void setOrderBy(StringBuilder orderByClause);

	public String getOrder() {
		return order;
	}

	public String getOppositeOrder() {

		if (ORDER_ASC.equals(order)) {
			return ORDER_DESC;
		}

		return ORDER_ASC;
	}

	public boolean isAsc() {
		return ORDER_ASC.equals(order);
	}

	public boolean isDesc() {
		return ORDER_DESC.equals(order);
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public boolean isActivated() {
		return active == ACTIVE;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public void activate() {
		this.active = ACTIVE;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("order", order).
				append("active", isActivated()).
				toString();
	}
}
