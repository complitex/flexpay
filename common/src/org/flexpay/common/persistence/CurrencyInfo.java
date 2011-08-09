package org.flexpay.common.persistence;

import org.flexpay.common.util.TranslationUtil;

import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

/**
 * Currency details the system is working with
 */
public class CurrencyInfo extends DomainObject {

	private String currencyIsoCode;
	private Integer gender;
	private Set<CurrencyName> names = set();

	public Currency getCurrency() {
		return Currency.getInstance(currencyIsoCode);
	}

	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}

	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
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
