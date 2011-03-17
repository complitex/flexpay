package org.flexpay.common.action.security;

import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.core.Authentication;

/**
 * @author pea1
 */
public class LogoutEvent extends AbstractAuthenticationEvent {

    //~ Constructors ===================================================================================================

    public LogoutEvent(Authentication authentication) {
        super(authentication);
    }
}
