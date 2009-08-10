package org.flexpay.ab.actions.country;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.sorter.CountrySorter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class CountriesListAction extends FPActionSupport {

	private List<Country> countries;

	private CountrySorter countrySorter = new CountrySorter();
	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		ArrayStack filters = CollectionUtils.arrayStack();
		countrySorter.setLang(getLanguage());
		List<ObjectSorter> sorters = CollectionUtils.<ObjectSorter>list(countrySorter);
		List<Country> countriesStubs = countryService.find(filters, sorters, new Page<Country>());

		log.debug("Total countries found: ", countriesStubs);
		log.debug("Country sorter: {}", countrySorter);

		countries = countryService.readFull(DomainObject.collectionIds(countriesStubs), true);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public CountrySorter getCountrySorter() {
		return countrySorter;
	}

	public void setCountrySorter(CountrySorter countrySorter) {
		this.countrySorter = countrySorter;
	}

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

}
