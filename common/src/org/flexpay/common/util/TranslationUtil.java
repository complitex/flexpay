package org.flexpay.common.util;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class TranslationUtil {

	private static Logger log = LoggerFactory.getLogger(TranslationUtil.class);

	/**
	 * Find translation object in collection for specified <code>locale</locale>
	 *
	 * @param translations Collection of object translations
	 * @param locale	   Locale to get translation in
	 * @return Translation in specified locale if found, or in defualt locale, or <code>null</code>
	 */
	@Nullable
	public static <T extends Translation> T getTranslation(
			@NotNull Collection<T> translations, @NotNull Locale locale) {

		Language language = LanguageUtil.getLanguage(locale);
		return getTranslation(translations, language);
	}

	/**
	 * Find translation object in collection for default <code>language</locale>
	 *
	 * @param translations Collection of object translations
	 * @return Translation in defualt language, or <code>null</code>
	 */
	@Nullable
	public static <T extends Translation> T getTranslation(@NotNull Collection<T> translations) {

		return getTranslation(translations, ApplicationConfig.getDefaultLanguage());
	}

	/**
	 * Find translation object in collection for specified <code>language</locale>
	 *
	 * @param translations Collection of object translations
	 * @param language	 Language to get translation in
	 * @return Translation in specified locale if found, or in defualt locale, or <code>null</code>
	 */
	@Nullable
	public static <T extends Translation> T getTranslation(
			@NotNull Collection<T> translations, @NotNull Language language) {

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

		if (translations == Collections.EMPTY_SET) {
			translations = set();
		}

		T candidate = null;
		for (T t : translations) {
			if (t.isSameLanguage(translation)) {
				candidate = t;
				log.debug("0. t.isSameLanguage(translation) = {}", t.isSameLanguage(translation));
				break;
			}
		}

		log.debug("1. translation = {}", translation);
		log.debug("2. candidate = {}", candidate);

		if (candidate != null) {
			if (StringUtils.isBlank(translation.getName())) {
				log.debug("3. StringUtils.isBlank(translation.getName()) = {}", StringUtils.isBlank(translation.getName()));
				translations.remove(candidate);
				return translations;
			}
			candidate.copyName(translation);
			log.debug("4. candidate = {}", candidate);
			return translations;
		}

		log.debug("5. StringUtils.isBlank(translation.getName()) = {}", StringUtils.isBlank(translation.getName()));

		if (StringUtils.isBlank(translation.getName())) {
			return translations;
		}

		translation.setTranslatable(translatable);
		translations.add(translation);

		log.debug("6. translations = {}", translations);

		return translations;
	}

}
