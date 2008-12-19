package org.flexpay.common.util.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ApplicationConfig {

	private static final Logger log = Logger.getLogger(ApplicationConfig.class);

	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	private static ApplicationConfig instance;

	private List<Language> languages = new ArrayList<Language>(3);
	@NonNls
	public static final String USER_PREFERENCES_SESSION_ATTRIBUTE = "FLEXPAY_USER_PREFERENCES_SESSION_ATTRIBUTE";

	private static final Date DATE_PAST_INFINITE = new GregorianCalendar(1900, 0, 1).getTime();
	private static final Date DATE_FUTURE_INFINITE = new GregorianCalendar(2100, 11, 31).getTime();

	private String dataRoot;

	private String szDataRoot;
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
	 * @throws RuntimeException if Default language is not configured
	 */
	@NotNull
	public static Language getDefaultLanguage() throws RuntimeException {
		for (Language language : getInstance().languages) {
			if (language.isDefault()) {
				return language;
			}
		}
		throw new RuntimeException("No default language defined");
	}

	/**
	 * Get Default Locale
	 *
	 * @return Locale
	 */
	@NotNull
	public static Locale getDefaultLocale() {
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
		return getInstance().getDataRootInternal();
	}

	public static File getProcessLogDirectory() {
		File dataRoot = getInstance().getDataRootInternal();
		return new File(dataRoot, "logs");
	}

	protected File getDataRootInternal() {
		return new File(tmpDir(), dataRoot);
	}

	private static File tmpDir() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	public void setDataRoot(String dataRoot) {
		this.dataRoot = dataRoot;
		File root = getDataRootInternal();
		if (!root.exists()) {
			root.mkdirs();
		}
	}

	public static File getSzDataRoot() {
		return getInstance().getSzDataRootInternal();
	}

	private File getSzDataRootInternal() {
		return new File(getDataRootInternal(), szDataRoot);
	}

	public void setSzDataRoot(String szDataRoot) {
		this.szDataRoot = szDataRoot;
		File szRoot = getSzDataRootInternal();
		if (!szRoot.exists()) {
			szRoot.mkdirs();
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
		try {
			Resource resource = resourceLoader.getResource(name);
			if (resource.exists()) {
				return resource.getInputStream();
			} else {
				return null;
			}
		} catch (IOException e) {
			log.warn("Failed getting resource " + name, e);
			return null;
		}
	}

	public static void setResourceLoader(ResourceLoader resourceLoader) {
		log.debug("Setting resource loader");
		ApplicationConfig.resourceLoader = resourceLoader;
	}
}
