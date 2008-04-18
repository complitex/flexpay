package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.SpRegistryType;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;

public interface SpRegistryTypeService {
	public static final Long SALDO = 1L;
	public static final Long NACHISLENIE = 2L;
	public static final Long IZVESHENIE = 3L;
	public static final Long SCHETA_NA_ZAKRITIE = 4L;
	public static final Long INFORMATSIONNY = 5L;
	public static final Long KORREKTIROVKI = 6L;
	public static final Long NALICHNIE_OPLATI = 7L;
	public static final Long BEZNALICHNIE_OPLATI = 8L;
	public static final Long VOZVRATI_PLANEZEY = 9L;
	public static final Long OSHIBKI = 10L;
	
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
