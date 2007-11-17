package org.flexpay.common.util.config;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.flexpay.common.util.locale.Language;
import org.flexpay.common.util.locale.LanguageName;

import java.net.URL;

public class CommonConfigLoader {

	private static Logger log = Logger.getLogger(CommonConfigLoader.class);

	// Configuration file URL
	private URL configFile;

	/**
	 * Constructor for loader
	 *
	 * @param configFile Configuration file URL
	 */
	public CommonConfigLoader(URL configFile) {
		this.configFile = configFile;
	}

	/**
	 * Initialize configuration
	 *
	 * @throws Exception if failure occurs
	 */
	public void loadConfig() throws Exception {
		Digester digester = new Digester();

		addRules(digester);
		digester.parse(configFile);

		ApplicationConfig config = (ApplicationConfig) digester.getRoot();
		if (log.isInfoEnabled()) {
			log.info("Loaded config: " + config.toString());
		}

		ApplicationConfig.setInstance(config);
	}

	/**
	 * Get ApplicationConfig class, dependent nodes should re
	 *
	 * @return ApplicationConfig class
	 */
	protected Class getConfigClass() {
		return ApplicationConfig.class;
	}

	/**
	 * Add config loading rules
	 *
	 * @param d Digester
	 */
	protected void addRules(Digester d) {
		// Set flexpay root to create Config instance
		d.addObjectCreate("flexpay", getConfigClass());
		d.addSetProperties("flexpay");

		// Load languages
		d.addObjectCreate("flexpay/languages/language", Language.class);
		d.addSetProperties("flexpay/languages/language");
		d.addSetNext("flexpay/languages/language", "addLanguage");

		// Load language translations
		String[] langProps = {"name", "locale"};
		String[] confProps = {"name", "localeName"};
		d.addObjectCreate("flexpay/languages/language/translation", LanguageName.class);
		d.addSetProperties("flexpay/languages/language/translation", langProps, confProps);
		d.addSetNext("flexpay/languages/language/translation", "addTranslation");
	}
}
