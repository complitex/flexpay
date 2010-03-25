package org.flexpay.common.dao.impl.ldap;

import org.springframework.ldap.filter.Filter;

import javax.naming.Name;

/**
 * Base interface for distinguished names builders
 */
public interface DnBuilder {
	/**
	 * Build relative to base distinguished name
	 *
	 * @return Distinguished name
	 */
	Name buildDn();

	/**
	 * Build Filter
	 *
	 * @return Filter object
	 */
	Filter getNameFilter();
}
