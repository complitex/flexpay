package org.flexpay.common.persistence.morphology.currency;

import org.flexpay.common.persistence.CurrencyInfo;

import java.math.BigDecimal;
import java.util.Locale;

public interface CurrencyToTextConverter {

	/**
	 * Convert decimal <code>amount</code> of <code>currency</code> to text in specified <code>locale</code>
	 *
	 * @param amount Decimal amount
	 * @param currency Currency to get text for
	 * @param locale Locale to get text in
	 * @return text representation of amount
	 * @throws IllegalArgumentException if amount 
	 */
	String toText(BigDecimal amount, CurrencyInfo currency, Locale locale) throws IllegalArgumentException;

	/**
	 * Convert decimal <code>amount</code> of <code>currency</code> to text in defualt locale
	 *
	 * @param amount Decimal amount
	 * @param currency Currency to get text for
	 * @return text representation of amount
	 * @throws IllegalArgumentException if amount
	 */
	String toText(BigDecimal amount, CurrencyInfo currency) throws IllegalArgumentException;
}
