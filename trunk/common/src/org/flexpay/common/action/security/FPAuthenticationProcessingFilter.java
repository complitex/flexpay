package org.flexpay.common.action.security;

import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.opensymphony.xwork2.interceptor.I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE;

public class FPAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // check if
        if (!requiresAuthentication(httpRequest, httpResponse)) {
            if (httpRequest.getSession().getAttribute(DEFAULT_SESSION_ATTRIBUTE) == null) {
                Authentication auth = SecurityUtil.getAuthentication();
                if (auth != null) {
                    log.debug("Authenticated, but no locale attribute set, fixing");
                    UserPreferences preferences = (UserPreferences) auth.getPrincipal();
                    httpRequest.getSession().setAttribute(DEFAULT_SESSION_ATTRIBUTE, preferences.getLocale());
                }
            }
        }

        super.doFilter(httpRequest, httpResponse, chain);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {

        super.successfulAuthentication(request, response, authResult);

        // save Locale
        UserPreferences preferences = (UserPreferences) authResult.getPrincipal();
        request.getSession().setAttribute(DEFAULT_SESSION_ATTRIBUTE, preferences.getLocale());

        if (log.isDebugEnabled()) {
            log.debug("Setting locale to session {}", preferences.getLocale());
        }
    }

}
