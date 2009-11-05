package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Country;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface CountryDaoExt {

	/**
	 * Find and sort countries
	 *
	 * @param sorters Collection of sorters
	 * @param pager   Pager
	 * @return List of countries
	 */
	@NotNull
	List<Country> findCountries(Collection<ObjectSorter> sorters, Page<Country> pager);

}
