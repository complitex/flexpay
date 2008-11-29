package org.flexpay.common.util;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

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
			@NotNull Collection<T> translations, @NotNull Locale locale)
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
	public static <T extends Translation> T getTranslation(@NotNull Collection<T> translations)
			throws FlexPayException {

		return getTranslation(translations, ApplicationConfig.getDefaultLanguage());
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
			@NotNull Collection<T> translations, @NotNull Language language)
			throws FlexPayException {

		Language defaultLang = ApplicationConfig.getDefaultLanguage();
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

	/**
	 * Set object translation value
	 *
	 * @param translations <code>translatable</code> translations
	 * @param translatable Translatable object to set translation for
	 * @param translation  Translation
	 * @param <T>          Translation type
	 * @return translations
	 */
	@NotNull
	public static <T extends Translation> Set<T> setTranslation(
			@NotNull Set<T> translations, @NotNull DomainObject translatable, @NotNull T translation) {
		if (Collections.emptySet().equals(translations)) {
			translations = set();
		}

		T candidate = null;
		for (T t : translations) {
			if (t.isSameLanguage(translation)) {
				candidate = t;
				break;
			}
		}

		if (candidate != null) {
			if (StringUtils.isBlank(translation.getName())) {
				translations.remove(candidate);
				return translations;
			}
			candidate.copyName(translation);
			return translations;
		}

		if (StringUtils.isBlank(translation.getName())) {
			return translations;
		}

		translation.setTranslatable(translatable);
		translations.add(translation);
		return translations;
	}
}
