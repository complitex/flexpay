package org.flexpay.common.util.config;

import org.apache.commons.digester.Digester;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.service.LanguageService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStreamReader;
import java.net.URL;

/**
 * Application configuration loader
 */
public class CommonConfigLoader implements ResourceLoaderAware {

	@NonNls
	private Logger log = LoggerFactory.getLogger(getClass());

	// Configuration file URL
	private URL[] configFiles;

	private LanguageService languageService;

	/**
	 * Constructor for loader
	 *
	 * @param configFiles Configuration file URL
	 */
	public CommonConfigLoader(URL[] configFiles) {
		this.configFiles = configFiles;
	}

	/**
	 * Initialize configuration
	 *
	 * @throws Exception if failure occurs
	 */
	public void loadConfig() throws Exception {
		ApplicationConfig config = getNewConfig();

		log.info("Starting loading configs");
		for (URL url : configFiles) {
			@NonNls InputStreamReader is = null;
			try {
				Digester digester = new Digester();
				digester.push(config);
				addRules(digester);
				is = new InputStreamReader(url.openStream(), "UTF-8");
				digester.parse(is);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}

		if (languageService != null) {
			log.debug("Loading languages");
			config.setLanguages(languageService.getLanguages());
		}
		log.debug("Loaded config: {}", config.toString());

		ApplicationConfig.setInstance(config);
		setApplicationProperties();
	}

	/**
	 * Get ApplicationConfig, dependent nodes should replace config instance
	 *
	 * @return ApplicationConfig
	 */
	@NotNull
	protected ApplicationConfig getNewConfig() {
		return new ApplicationConfig();
	}

	/**
	 * Set up application properties
	 *
	 * @throws FlexPayException if configuration is invalid
	 */
	protected void setApplicationProperties() throws FlexPayException {
		// nothing to do here
	}

	/**
	 * Add config loading rules
	 *
	 * @param d Digester
	 */
	protected void addRules(@NonNls Digester d) {

		d.addCallMethod("flexpay/dataRoot", "setDataRoot", 0);
		d.addCallMethod("flexpay/szDataRoot", "setSzDataRoot", 0);
		d.addCallMethod("flexpay/szDefaultDbfFileEncoding", "setSzDefaultDbfFileEncoding", 0);

		d.addCallMethod("flexpay/testprop", "setTestProp", 0);
	}

	/**
	 * Setter for property 'languageDao'.
	 *
	 * @param languageService Value to set for property 'languageDao'.
	 */
	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		ApplicationConfig.setResourceLoader(resourceLoader);
	}
}
