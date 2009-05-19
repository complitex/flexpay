package org.flexpay.common.persistence.morphology.number;

import java.util.Locale;

/**
 * Factory of NumberToTextConverter objects
 */
public interface NumberConverterFactory {

	/**
	 * Create converter by locale
	 *
	 * @param locale Locale to get number converter for
	 * @return NumberToTextConverter instance
	 */
	NumberToTextConverter getInstance(Locale locale);
}
