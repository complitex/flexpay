package org.flexpay.common.dao.impl.ldap;

import org.flexpay.common.util.config.UserPreferences;
import org.springframework.ldap.filter.Filter;

import javax.naming.Name;

/**
 * Base interface for distinguished names builders
 */
public interface DnBuilder {

	/**
	 * Build relative to base distinguished name
	 *
	 * @param preferences UserPreferences
	 * @return Distinguished name
	 */
	Name buildDn(UserPreferences preferences);

	/**
	 * Build Filter from name
	 *
	 * @param  name Name
	 * @return Filter object
	 */
	Filter getNameFilter(String name);
}
