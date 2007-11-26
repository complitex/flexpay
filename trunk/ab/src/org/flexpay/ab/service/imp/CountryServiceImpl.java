package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.CountryDao;
import org.flexpay.ab.dao.CountryNameDao;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.CountryName;
import org.flexpay.ab.persistence.CountryStatus;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CountryServiceImpl implements CountryService {

	private static Logger log = Logger.getLogger(CountryServiceImpl.class);

	private CountryDao countryDao;
	private CountryNameDao countryNameDao;

	public Country create(List<CountryName> countryNames) {
		Country country = new Country();
		country.setCountryStatus(CountryStatus.Active);

		List<CountryName> names = new ArrayList<CountryName>(countryNames.size());
		for(CountryName name : countryNames) {
			if (!StringUtils.isBlank(name.getName())) {
				name.setCountry(country);
				names.add(name);
			}
		}
		if (names.size() == 0) {
			throw new RuntimeException("No country names specified");
		}

		// Save country
		countryDao.create(country);
		for(CountryName name : names) {
			countryNameDao.create(name);
		}
		country.setCountryNames(names);

		if (log.isInfoEnabled()) {
			log.info("Country: " + country);
		}

		return country;
	}

	public List<CountryName> getCountries(Locale locale) throws FlexPayException {
		Language language = LanguageUtil.getLanguage(locale);
		Language defaultLang = ApplicationConfig.getInstance().getDefaultLanguage();
		List<Country> countries = countryDao.listCountries();
		List<CountryName> countryNameList = new ArrayList<CountryName>(countries.size());

		for (Country country : countries) {
			CountryName name = getCountryName(country, language, defaultLang);
			if ( name == null ) {
				log.error("No name for country: " + language.getLangIsoCode() + " : " +
						  defaultLang.getLangIsoCode() + ", " + country);
				continue;
			}
			name.setTranslation(LanguageUtil.getLanguageName(name.getLanguage(), locale));
			countryNameList.add(name);
		}

		return countryNameList;
	}

	private CountryName getCountryName(Country country, Language lang, Language defaultLang) {
		CountryName defaultName = null;

		List<CountryName> names = country.getCountryNames();
		for (CountryName name : names) {
			if (lang.equals(name.getLanguage())) {
				return name;
			}
			if (defaultLang.equals(name.getLanguage())) {
				defaultName = name;
			}
		}

		return defaultName;
	}

	public void setCountryDao(CountryDao countryDao) {
		this.countryDao = countryDao;
	}

	public void setCountryNameDao(CountryNameDao countryNameDao) {
		this.countryNameDao = countryNameDao;
	}
}
