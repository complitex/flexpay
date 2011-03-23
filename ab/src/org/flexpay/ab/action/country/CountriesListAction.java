package org.flexpay.ab.action.country;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.sorter.CountrySorter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;

public class CountriesListAction extends FPActionSupport {

	private List<Country> countries = list();
	private CountrySorter countrySorter = new CountrySorter();

	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (countrySorter == null) {
			log.warn("CountrySorter is null");
			countrySorter = new CountrySorter();
		}

		countrySorter.setLang(getLanguage());

		countries = countryService.find(arrayStack(), list(countrySorter), new Page<Country>(1000));
		if (log.isDebugEnabled()) {
			log.debug("Total countries found: {}", countries.size());
		}

		countries = countryService.readFull(collectionIds(countries), true);
		if (log.isDebugEnabled()) {
			log.debug("Total full countries found: {}", countries.size());
		}

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
