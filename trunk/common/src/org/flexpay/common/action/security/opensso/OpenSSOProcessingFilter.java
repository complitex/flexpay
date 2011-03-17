package org.flexpay.common.action.security.opensso;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.iplanet.sso.SSOTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of filter which is responsible for processing authentication requests.
 * @see AbstractAuthenticationProcessingFilter
 */
public class OpenSSOProcessingFilter extends UsernamePasswordAuthenticationFilter {

    private static Logger log = LoggerFactory.getLogger(OpenSSOProcessingFilter.class);

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        SSOToken token = obtainSSOToken(request);
        String username = obtainUsername(token);
        log.debug("username: {}", username);

        if (username == null) {
            throw new BadCredentialsException("User not logged in via Portal! SSO user cannot be validated!");
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, token);

        // Place the last username attempted into HttpSession for views
        request.getSession().setAttribute(SPRING_SECURITY_LAST_USERNAME_KEY, username);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    public String getDefaultFilterProcessesUrl() {
        return "/ssologin";
    }

    public static SSOToken getToken(HttpServletRequest request) {
        SSOToken token = null;
        try {
            SSOTokenManager manager = SSOTokenManager.getInstance();
            token = manager.createSSOToken(request);
        } catch (Exception e) {
            log.error("Error creating SSOToken", e);
        }
        return token;
    }

    private boolean isTokenValid(SSOToken token) {
        if (token == null) {
            throw new IllegalArgumentException("SSOToken is null");
        }

        boolean result = false;
        try {
            SSOTokenManager manager = SSOTokenManager.getInstance();
            result = manager.isValidToken(token);
        } catch (Exception e) {
            log.error("Error validating SSOToken", e);
        }
        return result;
    }

    protected SSOToken obtainSSOToken(HttpServletRequest request) {
        request = HttpUtil.unwrapOriginalHttpServletRequest(request);
        HttpUtil.printCookies(request);
        SSOToken token = getToken(request);
        if (token != null && isTokenValid(token)) {
            return token;
        }
        return null;
    }

    protected String obtainUsername(SSOToken token) {
        String result = null;
        if (token != null) {
            try {
                result = token.getProperty("UserId");
            } catch (SSOException e) {
                log.error("Error getting UserId from SSOToken", e);
            }
        }
        return result;
    }

    // this sets details of the authentication, NOT the user details
    // default is WebAuthenticationDetails (e.g. IP address, etc.)
    // todo: We should overide with the OpenSSO information.
    @Override
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        Object o = authenticationDetailsSource.buildDetails(request);
        authRequest.setDetails(o);
    }

}
