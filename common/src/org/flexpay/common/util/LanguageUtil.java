package org.flexpay.common.util;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import static org.flexpay.common.util.CollectionUtils.list;

public class LanguageUtil {

	/**
	 * Get languages name translations for given locale
	 *
	 * @param locale Locale
	 * @return Language names in given locale
	 * @throws FlexPayException if languages
	 */
	public static List<LangNameTranslation> getLanguageNames(Locale locale)
			throws FlexPayException {
		List<Language> langs = ApplicationConfig.getLanguages();
		List<LangNameTranslation> translations = list();
		for (Language lang : langs) {
			translations.add(getLanguageName(lang, locale));
		}

		return translations;
	}

	/**
	 * Get language name for locale
	 *
	 * @param languageStub Language
	 * @param locale	   Locale to get name for
	 * @return LanguageName in specified locale
	 * @throws FlexPayException if no locale specified for
	 */
	public static LangNameTranslation getLanguageName(Language languageStub, Locale locale)
			throws FlexPayException {
		Language from = getLanguage(locale);

		Language languageToTranslate = null;
		List<Language> languages = ApplicationConfig.getLanguages();
		for (Language lang : languages) {
			if (lang.getId().equals(languageStub.getId())) {
				languageToTranslate = lang;
			}
		}

		if (languageToTranslate != null) {
			for (LangNameTranslation name : languageToTranslate.getTranslations()) {
				if (name.getTranslationFrom().equals(from)) {
					return name;
				}
			}
		}

		throw new FlexPayException("No language specified for locale: " + locale);
	}

	/**
	 * Get language corresponding to specified locale
	 *
	 * @param locale java.util.Locale
	 * @return Language with this locale, or default one
	 */
	@NotNull
	public static Language getLanguage(@NotNull Locale locale) {
		for (Language language : ApplicationConfig.getLanguages()) {
			if (equals(language, locale)) {
				return language;
			}
		}

		return ApplicationConfig.getDefaultLanguage();
	}

	/**
	 * Check if two locales are equals
	 *
	 * @param l1 First locale
	 * @param l2 Second locale
	 * @return <code>true</code> if locales considered equals, or <code>false</code> otherwise
	 */
	public static boolean equals(@NotNull Language l1, @NotNull Locale l2) {
		return l2.getLanguage().equals(new Locale(l1.getLangIsoCode(), "", "").getLanguage());
	}
}
