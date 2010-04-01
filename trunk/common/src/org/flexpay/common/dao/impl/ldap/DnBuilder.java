package org.flexpay.common.dao.impl.ldap;

import org.springframework.ldap.filter.Filter;

import javax.naming.Name;
import java.util.List;

/**
 * Base interface for distinguished names builders
 */
public interface DnBuilder {
	/**
	 * Build relative to base distinguished name

	 * @param names Search names
	 * @return Distinguished name
	 */
	Name buildDn(List<String> names);

	/**
	 * Build Filter
	 *
	 * @param names Search names
	 * @return Filter object
	 */
	Filter getNameFilter(List<String> names);
}
