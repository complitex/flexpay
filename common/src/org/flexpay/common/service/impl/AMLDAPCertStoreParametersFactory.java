package org.flexpay.common.service.impl;

import com.sun.identity.security.cert.AMCertStore;
import com.sun.identity.security.cert.AMLDAPCertStoreParameters;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class AMLDAPCertStoreParametersFactory {

	private static final Logger log = LoggerFactory.getLogger(AMLDAPCertStoreParametersFactory.class);

	private String serverHost;
	private Integer serverPort;
	private String principleUser;
	private String principlePassword;
	private String startSearchLoc;

	public AMLDAPCertStoreParameters getLDAPStoreParameters() throws Exception {
		log.debug("getLDAPStoreParameters by host={},port={},user={},password={},loc={}",
				new Object[]{serverHost, serverPort, principleUser, principlePassword, startSearchLoc});
		return AMCertStore.setLdapStoreParam(serverHost, serverPort, principleUser, principlePassword, startSearchLoc, null, false);
	}

	@Required
	public void setServerUrl(String serverUrl) {
		String[] splitResult = StringUtils.removeStart(serverUrl, "ldap://").split(":");
		if (splitResult.length != 2) {
			log.error("Missing ldap url: {}. Url format is ldap://host:port", serverUrl);
			return;
		}
		serverHost = splitResult[0];
		try {
			serverPort = Integer.parseInt(splitResult[1]);
		} catch (NumberFormatException e) {
			log.error("Failed ldap server port {} (NumberFormatException), Ldap url is {}", serverPort, serverUrl);
		}
	}

	@Required
	public void setPrincipleUser(String principleUser) {
		this.principleUser = principleUser;
	}

	@Required
	public void setPrinciplePassword(String principlePassword) {
		this.principlePassword = principlePassword;
	}

	@Required
	public void setStartSearchLoc(String startSearchLoc) {
		this.startSearchLoc = startSearchLoc;
	}
}
