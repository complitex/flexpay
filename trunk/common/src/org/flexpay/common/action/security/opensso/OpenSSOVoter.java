package org.flexpay.common.action.security.opensso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * It translate the action decisions to votes
 */
public class OpenSSOVoter implements AccessDecisionVoter {

    private Logger log = LoggerFactory.getLogger(getClass());
    /**
     * Possible values of action decisions
     */
    public static final String OPENSSO_ALLOW = "allow";
    public static final String OPENSSO_DENY = "deny";

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> config) {

        int r = ACCESS_ABSTAIN;

        for (ConfigAttribute configAttribute : config) {
            if (configAttribute.getAttribute().equals(OPENSSO_ALLOW)) {
                r = ACCESS_GRANTED;
            } else if (configAttribute.getAttribute().equals(OPENSSO_DENY)) {
                r = ACCESS_DENIED;
            }
        }

        return r;
    }
}
