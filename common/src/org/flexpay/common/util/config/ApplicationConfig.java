package org.flexpay.common.util.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationConfig {

	private static ApplicationConfig instance;

	private List<Language> languages = new ArrayList<Language>(3);
	public static final String USER_PREFERENCES_SESSION_ATTRIBUTE =
			"FLEXPAY_USER_PREFERENCES_SESSION_ATTRIBUTE";

	/**
	 * Getter for property 'instance'.
	 *
	 * @return Value for property 'instance'.
	 */
	public static ApplicationConfig getInstance() {
		return instance;
	}

	/**
	 * Do not instantiate ApplicationConfig.
	 */
	public ApplicationConfig() {
	}

	/**
	 * Getter for property 'languages'.
	 *
	 * @return Value for property 'languages'.
	 */
	public List<Language> getLanguages() {
		return Collections.unmodifiableList(languages);
	}

	public void addLanguage(Language language) {
		languages.add(language);
	}

	/**
	 * Setter for property 'languages'.
	 *
	 * @param languages Value to set for property 'languages'.
	 */
	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}

	/**
	 * Get Default Language configuaration option
	 *
	 * @return Language
	 * @throws FlexPayException if Default language is not configured
	 */
	public Language getDefaultLanguage() throws FlexPayException {
		for (Language language : languages) {
			if (language.isDefault()) {
				return language;
			}
		}
		throw new FlexPayException("No default language defined");
	}

	/**
	 * Setter for property 'instance'.
	 *
	 * @param config Value to set for property 'instance'.
	 */
	static void setInstance(ApplicationConfig config) {
		instance = config;
	}

	/** {@inheritDoc} */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("languages", languages.toArray())
				.toString();
	}
}
