package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Country;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

import java.util.Collection;
import java.util.List;

public interface CountryService {

	/**
	 * Read Country object by its unique id
	 *
	 * @param countryStub Country stub
	 * @return Country object, or <code>null</code> if object not found
	 */
	@Secured (Roles.COUNTRY_READ)
	@Nullable
	Country readFull(@NotNull Stub<Country> countryStub);

	/**
	 * Read countries collection by theirs ids
	 *
 	 * @param countryIds Country ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found countries
	 */
	@Secured ({Roles.COUNTRY_READ})
	@NotNull
	List<Country> readFull(@NotNull Collection<Long> countryIds, boolean preserveOrder);

	/**
	 * Disable countries
	 *
	 * @param countryIds IDs of countries to disable
	 */
	@Secured (Roles.COUNTRY_DELETE)
	void disable(@NotNull Collection<Long> countryIds);

	/**
	 * Create country
	 *
	 * @param country Country to save
	 * @return Saved instance of country
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.COUNTRY_ADD)
	@NotNull
	Country create(@NotNull Country country) throws FlexPayExceptionContainer;

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@Secured (Roles.COUNTRY_READ)
	List<Country> find(@NotNull ArrayStack filters, @NotNull List<? extends ObjectSorter> sorters, @NotNull Page<Country> pager);

	/**
	 * Lookup countries by query. Query is a string which may contains in country name:
	 *
	 * @param query searching string
	 * @return List of founded countries
	 */
	@Secured (Roles.COUNTRY_READ)
	@NotNull
	List<Country> findByQuery(@NotNull String query);

}
