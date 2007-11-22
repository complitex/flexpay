package org.flexpay.common.actions.interceptor;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Spring session locale resolver using custom session variable
 */
public class FlexpaySessionLocaleResolver extends SessionLocaleResolver {

	private static Logger log = Logger.getLogger(FlexpaySessionLocaleResolver.class);

	private String attributeName;

	public FlexpaySessionLocaleResolver(String attributeName) {
		this.attributeName = attributeName;
	}

	public Locale resolveLocale(HttpServletRequest request) {

		log.info("Resolving locale");

		Locale locale = (Locale) WebUtils.getSessionAttribute(request, attributeName);
		if (locale == null) {
			log.info("Falling to default");
			locale = determineDefaultLocale(request);
		}
		log.info("Found Locale: " + locale.getLanguage());
		return locale;
	}


	/**
	 * Do nothing, locale saving is managed by Struts2 I18nInterceptor
	 *
	 * @param request  Servlet request
	 * @param response Servlet response
	 * @param locale   Locale
	 */
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
	}
}
