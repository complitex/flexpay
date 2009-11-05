package org.flexpay.ab.actions.country;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.CountryTranslation;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class CountryCreateAction extends FPActionSupport {

	private Map<Long, String> names = treeMap();
	private Map<Long, String> shortNames = treeMap();
	private Country country = new Country();

	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (isNotSubmit()) {
			initData();
			return INPUT;
		}

		if (country == null) {
			country = new Country();
		}

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			CountryTranslation translation = new CountryTranslation(value, lang);
			translation.setShortName(shortNames.get(name.getKey()));
			country.setTranslation(translation);
		}

		country = countryService.create(country);

		log.debug("Country created {}", country);
		addActionError(getText("ab.country.created"));
		return REDIRECT_SUCCESS;

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
		return INPUT;
	}

	private void initData() throws FlexPayException {
		for (Language lang : ApplicationConfig.getLanguages()) {
			names.put(lang.getId(), "");
			shortNames.put(lang.getId(), "");
		}
	}

	public Country getCountry() {
		return country;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public Map<Long, String> getShortNames() {
		return shortNames;
	}

	public void setShortNames(Map<Long, String> shortNames) {
		this.shortNames = shortNames;
	}

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

}
