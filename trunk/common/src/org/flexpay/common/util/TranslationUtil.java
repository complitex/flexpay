package org.flexpay.common.util;

import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Collection;
import java.util.Locale;

public class TranslationUtil {

	private static Logger log = Logger.getLogger(TranslationUtil.class);

	/**
	 * Find translation object in collection for specified <code>locale</locale>
	 *
	 * @param translations Collection of object translations
	 * @param locale	   Locale to get translation in
	 * @return Translation in specified locale if found, or in defualt locale, or
	 *         <code>null</code>
	 * @throws FlexPayException if languages configuration is invalid
	 */
	public static <T extends Translation> T getTranslation(
			Collection<T> translations, Locale locale)
			throws FlexPayException {

		Language language = LanguageUtil.getLanguage(locale);
		Language defaultLang = ApplicationConfig.getInstance().getDefaultLanguage();

		T defaultTranslation = null;

		for (T translation : translations) {
			// Check if translation language is the same as required
			if (language.equals(translation.getLang())) {
				log.debug("Found translation: " + translation);
				return translation;
			}
			// Check if translation language is the same as default and save it for a while
			if (defaultLang.equals(translation.getLang())) {
				log.debug("Found default translation: " + translation);
				defaultTranslation = translation;
			}

			if (log.isInfoEnabled()) {
				log.info("Translation is invalid: " + translation);
			}
		}

		// Translatio was not found, return in default language if any 
		return defaultTranslation;
	}
}
