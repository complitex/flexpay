package org.flexpay.common.actions.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.I18nInterceptor;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.LanguageUtil;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class FlexpayI18nInterceptor extends I18nInterceptor {

	private static Logger log = Logger.getLogger(FlexpayI18nInterceptor.class);

	/**
	 * Request attribute to hold the current LocaleResolver, retrievable by views.
	 *
	 * @see org.springframework.web.servlet.support.RequestContextUtils#getLocaleResolver
	 */
	public static final String LOCALE_RESOLVER_ATTRIBUTE =
			DispatcherServlet.class.getName() + ".LOCALE_RESOLVER";

	@Override
	protected void saveLocale(ActionInvocation actionInvocation, Locale locale) {
		// Allow Spring to resolve locale
		LocaleResolver localeResolver = new FlexpaySessionLocaleResolver(attributeName);
		HttpServletRequest request = ServletActionContext.getRequest();

		try {
			Language lang = LanguageUtil.getLanguage(locale);
			locale = lang.getLocale();

			log.info("Setting locale: " + locale.getLanguage());
			request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
			WebUtils.setSessionAttribute(request, "current_locale", lang);

			super.saveLocale(actionInvocation, locale);
		} catch (FlexPayException e) {
			log.error("Failed getting language for locale", e);
		}
	}
}
