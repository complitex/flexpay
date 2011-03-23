package org.flexpay.ab.action.country;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.CountryTranslation;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

public class CountryCreateAction extends FPActionSupport {

	private Map<Long, String> names = treeMap();
	private Map<Long, String> shortNames = treeMap();
	private Country country = new Country();

	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		correctNames();

		if (isNotSubmit()) {
			return INPUT;
		}

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			CountryTranslation translation = new CountryTranslation(value, lang);
			translation.setShortName(shortNames.get(name.getKey()));
			country.setTranslation(translation);
		}

		country = countryService.create(country);

		addActionMessage(getText("ab.country.saved"));

		return REDIRECT_SUCCESS;

	}

	private void correctNames() {
		if (names == null) {
			log.warn("Names parameter is null");
			names = treeMap();
		}
		if (shortNames == null) {
			log.warn("Short names parameter is null");
			shortNames = treeMap();
		}
		Map<Long, String> newNames = treeMap();
		Map<Long, String> newShortNames = treeMap();
		for (Language lang : getLanguages()) {
			newNames.put(lang.getId(), names.containsKey(lang.getId()) ? names.get(lang.getId()) : "");
			newShortNames.put(lang.getId(), shortNames.containsKey(lang.getId()) ? shortNames.get(lang.getId()) : "");
		}
		names = newNames;
		shortNames = newShortNames;
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
