package org.flexpay.common.action.security.opensso;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.iplanet.sso.SSOTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * It is in charge of doing the logout in the application and in every application
 * where the user logged via Single sign-on.
 * @see LogoutHandler
 */
public class OpenSSOLogoutHandler implements LogoutHandler {

    private Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        request = HttpUtil.unwrapOriginalHttpServletRequest(request);
        try {
            SSOTokenManager manager = SSOTokenManager.getInstance();
            SSOToken token = manager.createSSOToken(request);
            manager.destroyToken(token);
        } catch (SSOException e) {
            log.error("Error destroying SSOToken", e);
        } catch (UnsupportedOperationException e) {
            log.error("Error destroying SSOToken", e);
        }
    }
}
