package org.flexpay.common.util.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.util.locale.Language;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ApplicationConfig {

	private static ApplicationConfig instance;

	private Collection<Language> languages = new ArrayList<Language>(3);

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
	public Collection<Language> getLanguages() {
		return Collections.unmodifiableCollection(languages);
	}

	public void addLanguage(Language language) {
		languages.add(language);
	}

	/**
	 * Setter for property 'instance'.
	 *
	 * @param config Value to set for property 'instance'.
	 */
	static void setInstance(ApplicationConfig config) {
		instance = config;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("languages", languages.toArray())
				.toString();
	}
}
