package org.flexpay.common.action.security;

import com.opensymphony.xwork2.interceptor.I18nInterceptor;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FPAuthenticationProcessingFilter extends AuthenticationProcessingFilter {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// check if
		if (!requiresAuthentication(request, response)) {
			if (request.getSession().getAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE) == null) {
				Authentication auth = SecurityUtil.getAuthentication();
				if (auth != null) {
					log.debug("Authenticated, but no locale attribute set, fixing");
					UserPreferences preferences = (UserPreferences) auth.getPrincipal();
					request.getSession().setAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, preferences.getLocale());
				}
			}
		}

		super.doFilterHttp(request, response, chain);
	}

	@Override
	protected void onSuccessfulAuthentication(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authResult) throws IOException {

		super.onSuccessfulAuthentication(request, response, authResult);

		// save Locale
		UserPreferences preferences = (UserPreferences) authResult.getPrincipal();
		request.getSession().setAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, preferences.getLocale());

		if (log.isDebugEnabled()) {
			log.debug("Setting locale to session {}", preferences.getLocale());
		}
	}
}
