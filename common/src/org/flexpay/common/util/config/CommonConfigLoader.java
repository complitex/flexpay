package org.flexpay.common.util.config;

import org.apache.commons.digester.Digester;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.service.LanguageService;
import org.flexpay.common.util.LanguageUtil;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Application configuration loader
 */
public class CommonConfigLoader implements ServletContextAware {

	private Log log = LogFactory.getLog(getClass());

	private ServletContext context;

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
			InputStreamReader is = null;
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
		if (log.isDebugEnabled()) {
			log.debug("Loaded config: " + config.toString());
		}

		ApplicationConfig.setInstance(config);
		setApplicationProperties();
	}

	/**
	 * Get ApplicationConfig, dependent nodes should replace config instance
	 *
	 * @return ApplicationConfig
	 */
	protected ApplicationConfig getNewConfig() {
		return new ApplicationConfig();
	}

	/**
	 * Set up application properties
	 *
	 * @throws FlexPayException if configuration is invalid
	 */
	protected void setApplicationProperties() throws FlexPayException {
		ApplicationConfig config = ApplicationConfig.getInstance();
		setupLanguageNames(config);
	}

	/**
	 * Set up language names in their language
	 *
	 * @param config ApplicationConfig
	 * @throws FlexPayException if languages configuration is invalid
	 */
	private void setupLanguageNames(ApplicationConfig config) throws FlexPayException {
		if (context != null && config != null) {
			Collection<Language> langs = config.getLanguages();
			List<LangNameTranslation> translations = new ArrayList<LangNameTranslation>(langs.size());
			for (Language lang : langs) {
				LangNameTranslation translation = LanguageUtil.getLanguageName(lang, lang.getLocale());
				if (lang.isDefault()) {
					translations.add(0, translation);
				} else {
					translations.add(translation);
				}
			}
			context.setAttribute("languages", translations);
		} else {
			log.debug("ServletContext or ApplicationConfig was not set up");
		}
	}

	/**
	 * Add config loading rules
	 *
	 * @param d Digester
	 */
	protected void addRules(Digester d) {

		d.addCallMethod("flexpay/dataRoot", "setDataRoot", 0);
		d.addCallMethod("flexpay/eircDataRoot", "setEircDataRoot", 0);
		d.addCallMethod("flexpay/eircId", "setEircId", 0);
		d.addCallMethod("flexpay/szDataRoot", "setSzDataRoot", 0);
		d.addCallMethod("flexpay/szDefaultDbfFileEncoding", "setSzDefaultDbfFileEncoding", 0);

		d.addCallMethod("flexpay/testprop", "setTestProp", 0);
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

	/**
	 * Setter for property 'languageDao'.
	 *
	 * @param languageService Value to set for property 'languageDao'.
	 */
	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}
}
