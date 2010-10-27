package org.flexpay.common.actions.security.opensso;

import com.iplanet.sso.SSOToken;
import com.sun.identity.policy.ServiceTypeManager;
import com.sun.identity.security.AdminTokenAction;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class PolicyUtils {

	private PolicyUtils() {}

    public static SSOToken getToken() throws Exception {
		AdminTokenAction action = AdminTokenAction.getInstance();
        SSOToken ssoToken = (SSOToken) AccessController.doPrivileged((PrivilegedAction<AdminTokenAction>)action);
		new ServiceTypeManager(ssoToken);

		return ssoToken;
	}
}
