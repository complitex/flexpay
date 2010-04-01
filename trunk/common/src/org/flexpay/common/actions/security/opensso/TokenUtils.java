package org.flexpay.common.actions.security.opensso;

import com.iplanet.sso.SSOToken;
import com.sun.identity.authentication.AuthContext;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import java.security.Principal;
import java.util.Iterator;

public class TokenUtils {

    public static SSOToken getToken(String orgName, String userId,
            String password) throws Exception {
        return getSessionToken(orgName, userId, password);
    }

    public static SSOToken getSessionToken(String orgName, String userId,
            String password) throws Exception {
        return getSessionToken(orgName,userId, password, null, -1);
    }

    public static SSOToken getSessionToken(String orgName, String userId,
            String password, String module, int level)
            throws Exception
    {
        AuthContext ac = null;
        try {
            //System.out.println("TokenUtils:orgName=" + orgName);
            ac = new AuthContext(orgName);
            if (module != null) {
                ac.login(AuthContext.IndexType.MODULE_INSTANCE, module);
            } else if (level != -1) {
                ac.login(AuthContext.IndexType.LEVEL, String.valueOf(level));
            } else {
		//System.out.println("TokenUtils:calling login()");
                ac.login();
            }
            //System.out.println("TokenUtils:after ac.login()");
        } catch (LoginException le) {
            le.printStackTrace();
            return null;
        }

        try {
            Callback[] callbacks = null;
            // Get the information requested by the plug-ins
            if (ac.hasMoreRequirements()) {
                callbacks = ac.getRequirements();

                if (callbacks != null) {
                    addLoginCallbackMessage(callbacks, userId, password);
                    ac.submitRequirements(callbacks);

                    if (ac.getStatus() == AuthContext.Status.SUCCESS) {
                        //System.out.println("Auth success");
                        Subject authSubject = ac.getSubject();
                        if ( authSubject != null) {
                            Iterator principals =
                            (authSubject.getPrincipals()).iterator();
                            Principal principal;
                            while (principals.hasNext()) {
                                principal = (Principal) principals.next();
                            }
                        }
                    } else if (ac.getStatus() == AuthContext.Status.FAILED) {
                        //System.out.println("Authentication has FAILED");
                    } else {
                    }
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //SSOTokenManager.getInstance().validateToken(ac.getSSOToken());
        //System.out.println(ac.getSSOToken().getPrincipal().getName());
        return ac.getSSOToken();
    }


    static void addLoginCallbackMessage(Callback[] callbacks, String userId,
        String password)
         throws UnsupportedCallbackException
    {
        int i = 0;
        try {
            for (i = 0; i < callbacks.length; i++) {
                if (callbacks[i] instanceof NameCallback) {

                    // prompt the user for a username
                    NameCallback nc = (NameCallback) callbacks[i];

                    //System.out.println("userName=" + userId);
                    nc.setName(userId);

                } else if (callbacks[i] instanceof PasswordCallback) {

                    // prompt the user for sensitive information
                    PasswordCallback pc = (PasswordCallback) callbacks[i];

                    //System.out.println("password=" + password);
                    pc.setPassword(password.toCharArray());

                } else {
                }
            }
        } catch (Exception e) {
                    //throw new UnsupportedCallbackException(callbacks[i],
                    //"Callback exception: " + e);
        }
    }

}
