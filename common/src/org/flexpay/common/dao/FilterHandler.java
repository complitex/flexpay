package org.flexpay.common.dao;

import org.flexpay.common.persistence.filter.ObjectFilter;

import java.util.List;

/**
 * ObjectFilter handlers interface
 */
public interface FilterHandler {

	/**
	 * Check if this handler supports filter
	 *
	 * @param filter ObjectFilter to check
	 * @return <code>true</code> if filter is supported, or <code>false</false> otherwise
	 */
	boolean supports(ObjectFilter filter);

	/**
	 * Add HQL where clause based on filter
	 *
	 * @param filter	 ObjectFilter
	 * @param clause	 Where clause to update
	 * @return List of parameters
	 */
	List<?> whereClause(ObjectFilter filter, StringBuilder clause);
}
