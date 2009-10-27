package org.flexpay.ab.actions.country;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.sorter.CountrySorter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class CountriesListAction extends FPActionSupport {

	private List<Country> countries = list();

	private CountrySorter countrySorter = new CountrySorter();
	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		countrySorter.setLang(getLanguage());
		List<ObjectSorter> sorters = CollectionUtils.<ObjectSorter>list(countrySorter);
		countries = countryService.find(arrayStack(), sorters, new Page<Country>());

		log.debug("Total countries found: ", countries);
		log.debug("Country sorter: {}", countrySorter);

		countries = countryService.readFull(DomainObject.collectionIds(countries), true);

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
