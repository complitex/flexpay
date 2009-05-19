package org.flexpay.common.persistence;

import org.flexpay.common.persistence.morphology.Gender;
import org.flexpay.common.util.TranslationUtil;

import java.util.Collections;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

/**
 * Currency details the system is working with
 */
public class CurrencyInfo extends DomainObject {

	private String currencyIsoCode;
	private Gender gender;
	private Set<CurrencyName> names = Collections.emptySet();

	public Currency getCurrency() {
		return Currency.getInstance(currencyIsoCode);
	}

	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}

	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Set<CurrencyName> getNames() {
		return names;
	}

	public void setNames(Set<CurrencyName> names) {
		this.names = names;
	}

	public CurrencyName getName(Locale locale) {
		return locale != null ?
			   TranslationUtil.getTranslation(names, locale) :
			   TranslationUtil.getTranslation(names);
	}
}
