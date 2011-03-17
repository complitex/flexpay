package org.flexpay.common.action.security.opensso;

import com.iplanet.sso.SSOToken;
import com.sun.identity.policy.ActionDecision;
import com.sun.identity.policy.PolicyDecision;
import com.sun.identity.policy.client.PolicyEvaluator;
import com.sun.identity.policy.client.PolicyEvaluatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.*;

/**
 * It is in charge of getting the security policies, <code>PolicyDecision</code>, 
 * defined for a resource and an user by web service of opensso.war application.
 */
public class OpenSSOObjectDefinitionSource implements FilterInvocationSecurityMetadataSource, InitializingBean {

    private Logger log = LoggerFactory.getLogger(getClass());
    /**
     * Environment params. Not used
     */
    private Map<?, ?> envParams = map();
    /**
     * Matcher to compile URL patterns
     */
    private UrlMatcher urlMatcher = new AntUrlPathMatcher();
    /**
     * compiled patterns of URLs which are out of authentication policies
     */
    private Collection<String> anonymousPatterns = list();
    /**
     * URL patterns defined in spring configuration which are out of authentication policies
     */
    private Collection<String> anonymousUrls = list();

    /**
     * Set the URLs defined in spring configuration which are out of authentication policies
     * @param anonymousUrls anonymous URLs
     */
    public void setAnonymousUrls(Collection<String> anonymousUrls) {
        this.anonymousUrls = anonymousUrls;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (String url : anonymousUrls) {
            anonymousPatterns.add((String) urlMatcher.compile(url));
        }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        String resource = filterInvocation.getRequestUrl();
        if (isAnonymousUrl(resource)) {
            return null;
        }

        SSOToken token = OpenSSOProcessingFilter.getToken(filterInvocation.getHttpRequest());
        if (token == null) {
            throw new InsufficientAuthenticationException("SSOToken does not exist");
        }

        Set<String> actions = set();
        actions.add(filterInvocation.getHttpRequest().getMethod());
        String fullResourceUrl = filterInvocation.getFullRequestUrl();

        try {
            PolicyEvaluator policyEvaluator = PolicyEvaluatorFactory.getInstance().getPolicyEvaluator("iPlanetAMWebAgentService");
            log.debug("getPolicy for resource = {}, actions = {}", fullResourceUrl, actions);
            PolicyDecision policyDecision = policyEvaluator.getPolicyDecision(token, fullResourceUrl, actions, envParams);
            Map<?, ?> actionDecisions = policyDecision.getActionDecisions();

            List<ConfigAttribute> configAtributes = list();

            // If OpenSSO has a NULL policy decision we return
            // and Empty list. This results in a Spring "ABSTAIN" vote
            if (actionDecisions != null && !actionDecisions.isEmpty()) {
                ActionDecision actionDecision = (ActionDecision) actionDecisions.values().iterator().next();
                for (Object o : actionDecision.getValues()) {
                    String s = (String) o;
                    log.debug("configAttributes.add({})", s);
                    configAtributes.add(new SecurityConfig(s));
                }
            }

            return configAtributes;

        } catch (Exception e) {
            log.error("Exception while evaling policy", e);
            throw new AccessDeniedException("Error accessing to Opensso", e);
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    private boolean isAnonymousUrl(String requestUrl) {
        for (Object anonymousPattern : anonymousPatterns) {
            if (urlMatcher.pathMatchesUrl(anonymousPattern, requestUrl)) {
                return true;
            }
        }
        return false;
    }
}
