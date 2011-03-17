package org.flexpay.common.action.security.opensso;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.sun.identity.idm.AMIdentity;
import com.sun.identity.idm.IdRepoException;
import com.sun.identity.idm.IdType;
import com.sun.identity.idm.IdUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.list;

/**
 * Strategy to convert OpenSSO group memberships for a principal
 * into an array of Spring GrantedAuthorities[].
 *
 * This implemenentation is very simple:
 * It converts the OpenSSO group name to upper case, and then
 * prepends the prefix "ROLE_". For example, an OpenSSO group
 * membership of staff becomes "ROLE_STAFF"
 *
 * In the future we might want to extract an interface from this class
 * and create other strategy mechanisims. For example - using
 * a map to map OpenSSO groups to Spring roles.
 *
 * Note that this implementation always adds ROLE_AUTHENTICATED
 * to the granted authorities. if the SSOToken is valid, the user
 * has authenticated.
 *
 * @author warrenstrange
 */
public class OpenSSOSimpleAuthoritiesPopulator {

    /**
     * Lookup the users group memberships and return as an array of GrantedAuthority
     * @param ssoToken users SSOTOken
     * @return Arrayo of GrantedAuthority representing the userss OpenSSO groups.
     *
     * @throws com.iplanet.sso.SSOException exception
     * @throws com.sun.identity.idm.IdRepoException exception
     */
    public List<GrantedAuthority> getGrantedAuthorities(SSOToken ssoToken) throws IdRepoException, SSOException {
        List<GrantedAuthority> ga = list();
        AMIdentity id = IdUtils.getIdentity(ssoToken);
      
        Set<?> groups = id.getMemberships(IdType.GROUP);

        if (groups != null && !groups.isEmpty()) {
            //leave one extra spot for ROLE_AUTHENTICATED
            for (Object group1 : groups) {
                AMIdentity group = (AMIdentity) group1;
                String role = "ROLE_" + group.getName().toUpperCase();
                ga.add(new GrantedAuthorityImpl(role));
            }
        }

        // if we are at this point the user must have authenticated to OpenSSO
        ga.add(new GrantedAuthorityImpl("ROLE_AUTHENTICATED"));

        return ga;
    }
}
