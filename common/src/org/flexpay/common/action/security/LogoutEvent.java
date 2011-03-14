/*
 * Created on Sep 13, 2006
 *
 */
package org.flexpay.common.action.security;

import org.springframework.security.event.authentication.AbstractAuthenticationEvent;
import org.springframework.security.Authentication;

/**
 * @author pea1
 */
public class LogoutEvent extends AbstractAuthenticationEvent {

    //~ Constructors ===================================================================================================

    public LogoutEvent(Authentication authentication) {
        super(authentication);
    }
}
