package org.flexpay.common.util.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.*;

public class ApplicationConfig {

	private static ApplicationConfig instance;

	private List<Language> languages = new ArrayList<Language>(3);
	@NonNls
	public static final String USER_PREFERENCES_SESSION_ATTRIBUTE = "FLEXPAY_USER_PREFERENCES_SESSION_ATTRIBUTE";

	private static final Date DATE_PAST_INFINITE = new GregorianCalendar(1900, 0, 1).getTime();
	private static final Date DATE_FUTURE_INFINITE = new GregorianCalendar(2100, 11, 31).getTime();

	private File dataRoot;

	private File szDataRoot;
	private String szDefaultDbfFileEncoding;

	private String testProp;

	public static Date getPastInfinite() {
		return DATE_PAST_INFINITE;
	}

	public static Date getFutureInfinite() {
		return DATE_FUTURE_INFINITE;
	}

	/**
	 * Getter for property 'instance'.
	 *
	 * @return Value for property 'instance'.
	 */
	protected static ApplicationConfig getInstance() {
		if (instance == null) {
			return new ApplicationConfig();
		}
		return instance;
	}

	/**
	 * Do not instantiate ApplicationConfig.
	 */
	protected ApplicationConfig() {
	}

	/**
	 * Getter for property 'languages'.
	 *
	 * @return Value for property 'languages'.
	 */
	@NotNull
	public static List<Language> getLanguages() {
		return Collections.unmodifiableList(getInstance().languages);
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
	@NotNull
	public static Language getDefaultLanguage() throws FlexPayException {
		for (Language language : getInstance().languages) {
			if (language.isDefault()) {
				return language;
			}
		}
		throw new FlexPayException("No default language defined");
	}

	/**
	 * Get Default Locale
	 *
	 * @return Locale
	 * @throws FlexPayException if Default language is not configured
	 */
	@NotNull
	public static Locale getDefaultLocale() throws FlexPayException {
		return getDefaultLanguage().getLocale();
	}

	/**
	 * Setter for property 'instance'.
	 *
	 * @param config Value to set for property 'instance'.
	 */
	static void setInstance(ApplicationConfig config) {
		instance = config;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("languages", languages.toArray()).toString();
	}

	public static File getDataRoot() {
		return getInstance().dataRoot;
	}

	public void setDataRoot(String dataRoot) {
		this.dataRoot = new File(dataRoot);
		if (!this.dataRoot.exists()) {
			this.dataRoot.mkdirs();
		}
	}

	public static File getSzDataRoot() {
		return getInstance().szDataRoot;
	}

	public void setSzDataRoot(String szDataRoot) {
		this.szDataRoot = new File(dataRoot, szDataRoot);
		if (!this.szDataRoot.exists()) {
			this.szDataRoot.mkdirs();
		}
	}

	public static String getSzDefaultDbfFileEncoding() {
		return getInstance().szDefaultDbfFileEncoding;
	}

	public void setSzDefaultDbfFileEncoding(String szDefaultDbfFileEncoding) {
		this.szDefaultDbfFileEncoding = szDefaultDbfFileEncoding;
	}


	public static String getTestProp() {
		return getInstance().testProp;
	}

	public void setTestProp(String prop) {
		this.testProp = prop;
	}

	/**
	 * Get resource stream by name. Caller is responsible for stream closing.
	 *
	 * @param name Resource name
	 * @return resource stream or <code>null</code> if not found
	 * @see java.lang.Class#getResourceAsStream(String)
	 */
	@Nullable
	public static InputStream getResourceAsStream(@NotNull @NonNls String name) {
		return getInstance().getClass().getResourceAsStream(name);
	}
}
