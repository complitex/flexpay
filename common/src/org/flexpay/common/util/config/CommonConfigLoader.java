package org.flexpay.common.util.config;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.flexpay.common.util.IOUtils;
import org.flexpay.common.util.locale.Language;
import org.flexpay.common.util.locale.LanguageName;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class CommonConfigLoader implements ServletContextAware {

	private static Logger log = Logger.getLogger(CommonConfigLoader.class);

	private ServletContext context;

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
		InputStreamReader is = null;
		try {
			is = new InputStreamReader(configFile.openStream(), "UTF-8");
			digester.parse(is);

			ApplicationConfig config = (ApplicationConfig) digester.getRoot();
			if (log.isInfoEnabled()) {
				log.info("Loaded config: " + config.toString());
			}

			ApplicationConfig.setInstance(config);

			setApplicationProperties();
		} finally {
			IOUtils.close(is);
		}
	}

	/**
	 * Get ApplicationConfig class, dependent nodes should re
	 *
	 * @return ApplicationConfig class
	 */
	protected Class getConfigClass() {
		return ApplicationConfig.class;
	}

	protected void setApplicationProperties() {
		ApplicationConfig config = ApplicationConfig.getInstance();
		if (context != null && config != null) {
			Collection<Language> langs = config.getLanguages();
			List<LanguageName> translations = new ArrayList<LanguageName>(langs.size());
			for (Language lang : langs) {
				Locale locale = lang.getLocale();
				Collection<LanguageName> names = lang.getTranslations();
				for (LanguageName langName : names) {
					if (langName.getLocale().equals(locale)) {
						if (lang.isDefault()) {
							translations.add(0, langName);
						} else {
							translations.add(langName);
						}
					}
				}
			}
			context.setAttribute("languages", translations);
		} else {
			log.info("ServletContext or ApplicationConfig was not set up");
		}
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
		d.addSetProperties("flexpay/languages/language", "locale", "localeName");
		d.addSetNext("flexpay/languages/language", "addLanguage");

		// Load language translations
		String[] langProps = {"name", "locale"};
		String[] confProps = {"name", "localeName"};
		d.addObjectCreate("flexpay/languages/language/translation", LanguageName.class);
		d.addSetProperties("flexpay/languages/language/translation", langProps, confProps);
		d.addSetNext("flexpay/languages/language/translation", "addTranslation");
	}

	/**
	 * Set the ServletContext that this object runs in. <p>Invoked after population of normal
	 * bean properties but before an init callback like InitializingBean's
	 * <code>afterPropertiesSet</code> or a custom init-method. Invoked after
	 * ApplicationContextAware's <code>setApplicationContext</code>.
	 *
	 * @param servletContext ServletContext object to be used by this object
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext
	 */
	public void setServletContext(ServletContext servletContext) {
		log.debug("Setting ServletConfig");
		this.context = servletContext;
	}
}
