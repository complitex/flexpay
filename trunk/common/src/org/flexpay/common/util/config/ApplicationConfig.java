package org.flexpay.common.util.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.service.LanguageService;
import org.flexpay.common.service.Security;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ApplicationConfig implements ResourceLoaderAware {

	private static final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);

	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	private static ApplicationConfig INSTANCE = new ApplicationConfig();

	private List<Language> languages = new ArrayList<Language>(3);

	private static final Date DATE_PAST_INFINITE = new GregorianCalendar(1900, 0, 1).getTime();
	private static final Date DATE_FUTURE_INFINITE = new GregorianCalendar(2100, 11, 31).getTime();

	private String applicationName;
	private String dataRoot;
	private int logPreviewLinesNumber;

	private Locale defaultReportLocale;

	private boolean disableSelfValidation = false;

	static {
		// ensure Security fields are initialised
		Security.touch();
	}

	/**
	 * Unique instance id
	 */
	private String instanceId;

	private String defaultCurrencyCode;
	private String testProp;

	@NotNull
	public static Date getPastInfinite() {
		return DATE_PAST_INFINITE;
	}

	@NotNull
	public static Date getFutureInfinite() {
		return DATE_FUTURE_INFINITE;
	}

	/**
	 * Getter for property 'instance'.
	 *
	 * @return Value for property 'instance'.
	 */
	@NotNull
	public static ApplicationConfig getInstance() {
		return INSTANCE;
	}

	/**
	 * Do not instantiate ApplicationConfig.
	 */
	private ApplicationConfig() {
	}

	/**
	 * Getter for property 'languages'.
	 *
	 * @return Value for property 'languages'.
	 */
	@NotNull
	public static List<Language> getLanguages() {
		return getInstance().languages;
	}

	/**
	 * Get Default Language configuration option
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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("languages", languages.toArray()).toString();
	}

	public static File getDataRoot() {
		return getDataRootInternal();
	}

	public static File getProcessLogDirectory() {
		File dataRoot = getDataRootInternal();
		return new File(dataRoot, "logs/"+getInstanceId());
	}

	private static File getDataRootInternal() {
		return new File(tmpDir(), getInstance().dataRoot);
	}

	private static File tmpDir() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	@Required
	public void setDataRoot(String dataRoot) {
		this.dataRoot = dataRoot;
		File root = getDataRootInternal();
		if (!root.exists()) {
			//noinspection ResultOfMethodCallIgnored
			root.mkdirs();
		}
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
	public static InputStream getResourceAsStream(@NotNull String name) {
		try {
			Resource resource = resourceLoader.getResource(name);
			return resource.exists() ? resource.getInputStream() : null;
		} catch (IOException e) {
			log.warn("Failed getting resource {}", name, e);
			return null;
		}
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		log.debug("Setting resource loader {}", resourceLoader);
		ApplicationConfig.resourceLoader = resourceLoader;
	}


	public static int getLogPreviewLinesNumber() {
		return getInstance().logPreviewLinesNumber;
	}

	@Required
	public void setLogPreviewLinesNumber(String logPreviewLinesNumber) {
		this.logPreviewLinesNumber = Integer.valueOf(logPreviewLinesNumber);
	}

	@NotNull
	public static String getInstanceId() {
		return getInstance().instanceId;
	}

	@Required
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public static String getApplicationName() {
		return getInstance().applicationName;
	}

	@Required
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public static String getDefaultCurrencyCode() {
		return getInstance().defaultCurrencyCode;
	}

	@Required
	public void setDefaultCurrencyCode(String defaultCurrencyCode) {
		this.defaultCurrencyCode = defaultCurrencyCode;
	}

	public static boolean isResourceAvailable(String name) {
		Resource resource = resourceLoader.getResource(name);
		return resource.exists();
	}

	@Required
	public void setDefaultReportLocale(String localeName) {
		this.defaultReportLocale = new Locale(localeName);
	}

	public static Locale getDefaultReportLocale() {
		return getInstance().defaultReportLocale;
	}

	@Required
	public void setLanguageService(LanguageService languageService) {
		languages = Collections.unmodifiableList(languageService.getLanguages());
	}

	public static boolean disableSelfValidation() {
		return getInstance().disableSelfValidation;
	}

	@Required
	public void setDisableSelfValidation(boolean disableSelfValidation) {
		this.disableSelfValidation = disableSelfValidation;
	}
}
