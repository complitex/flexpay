package org.flexpay.common.util;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;

public class TranslationUtil {

	/**
	 * Find translation object in collection for specified <code>locale</locale>
	 *
	 * @param translations Collection of object translations
	 * @param locale	   Locale to get translation in
	 * @return Translation in specified locale if found, or in defualt locale, or <code>null</code>
	 * @throws FlexPayException if languages configuration is invalid
	 */
	@Nullable
	public static <T extends Translation> T getTranslation(
			Collection<T> translations, Locale locale)
			throws FlexPayException {

		Language language = LanguageUtil.getLanguage(locale);
		return getTranslation(translations, language);
	}

	/**
	 * Find translation object in collection for default <code>language</locale>
	 *
	 * @param translations Collection of object translations
	 * @return Translation in defualt language, or <code>null</code>
	 * @throws FlexPayException if languages configuration is invalid
	 */
	@Nullable
	public static <T extends Translation> T getTranslation(Collection<T> translations)
			throws FlexPayException {

		return getTranslation(translations, ApplicationConfig.getInstance().getDefaultLanguage());
	}

	/**
	 * Find translation object in collection for specified <code>language</locale>
	 *
	 * @param translations Collection of object translations
	 * @param language	 Language to get translation in
	 * @return Translation in specified locale if found, or in defualt locale, or <code>null</code>
	 * @throws FlexPayException if languages configuration is invalid
	 */
	@Nullable
	public static <T extends Translation> T getTranslation(
			Collection<T> translations, Language language)
			throws FlexPayException {

		Language defaultLang = ApplicationConfig.getInstance().getDefaultLanguage();
		T defaultTranslation = null;

		for (T translation : translations) {
			// Check if translation language is the same as required
			if (language.equals(translation.getLang())) {
				return translation;
			}
			// Check if translation language is the same as default and save it for a while
			if (defaultLang.equals(translation.getLang())) {
				defaultTranslation = translation;
			}
		}

		// Translation was not found, return in default language if any
		return defaultTranslation;
	}
}
