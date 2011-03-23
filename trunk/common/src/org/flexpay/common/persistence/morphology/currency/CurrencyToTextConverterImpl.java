package org.flexpay.common.persistence.morphology.currency;

import org.flexpay.common.persistence.CurrencyInfo;
import org.flexpay.common.persistence.CurrencyName;
import org.flexpay.common.persistence.morphology.number.DefaultNumberConverterFactory;
import org.flexpay.common.persistence.morphology.number.NumberConverterFactory;
import org.flexpay.common.persistence.morphology.number.NumberToTextConverter;

import java.math.BigDecimal;
import java.util.Locale;

public class CurrencyToTextConverterImpl implements CurrencyToTextConverter {

	private static final BigDecimal MAX_LONG = BigDecimal.valueOf(Long.MAX_VALUE).add(BigDecimal.ONE);
	private static final BigDecimal MIN_LONG = BigDecimal.valueOf(Long.MIN_VALUE).subtract(BigDecimal.ONE);

	private NumberConverterFactory converterFactory = new DefaultNumberConverterFactory();

	/**
	 * Convert decimal <code>amount</code> of <code>currency</code> to text in specified <code>locale</code>
	 *
	 * @param amount   Decimal amount
	 * @param currency Currency to get text for
	 * @param locale   Locale to get text in
	 * @return text representation of amount
	 */
	public String toText(BigDecimal amount, CurrencyInfo currency, Locale locale) {

		if (amount.compareTo(MIN_LONG) <= 0 || MAX_LONG.compareTo(amount) <= 0) {
			throw new IllegalArgumentException("Amount is too big: " + amount);
		}

		// integral is a fraction cut off
		long integral = amount.setScale(0, BigDecimal.ROUND_DOWN).longValueExact();

		// scale decimal to currency default number of fraction digits
		// fraction is (scaled - integral) * 10 ^ scale
		BigDecimal scaled = amount.setScale(currency.getCurrency().getDefaultFractionDigits(), BigDecimal.ROUND_DOWN);
		long fraction = scaled.subtract(BigDecimal.valueOf(integral)).unscaledValue().longValue();

		CurrencyName currencyName = currency.getName(locale);
		NumberToTextConverter converter = converterFactory.getInstance(locale);
		return converter.toText(integral, currency.getGender()) + " " + currencyName.getShortName() + " " +
			   converter.toText(fraction, currency.getGender()) + " " + currencyName.getShortFractionName();
	}

	/**
	 * Convert decimal <code>amount</code> of <code>currency</code> to text in defualt locale
	 *
	 * @param amount   Decimal amount
	 * @param currency Currency to get text for
	 * @return text representation of amount
	 * @throws IllegalArgumentException if amount
	 */
	public String toText(BigDecimal amount, CurrencyInfo currency) throws IllegalArgumentException {
		return toText(amount, currency, null);
	}

	public void setConverterFactory(NumberConverterFactory converterFactory) {
		this.converterFactory = converterFactory;
	}
}
