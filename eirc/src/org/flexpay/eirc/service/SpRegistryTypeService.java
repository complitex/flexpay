package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.SpRegistryType;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;

public interface SpRegistryTypeService {

	/**
	 * Read SpRegistryType object by its unique id
	 * 
	 * @param id
	 *            SpRegistryType key
	 * @return SpRegistryType object, or <code>null</code> if object not found
	 */
	SpRegistryType read(Long id);

	/**
	 * init filter
	 *
	 * @param registryTypeFilter filter to init
	 */
	void initFilter(RegistryTypeFilter registryTypeFilter);
}
