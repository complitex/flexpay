package org.flexpay.common.dao.impl.ldap;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

abstract public class LdapConstants {

	public static final String FLEXPAY_PERSON_OBJECT_CLASS = "flexpayPerson";

	public static final List<String> REQUIRED_OBJECT_CLASSES = list(
            "top",
            "person",
            "organizationalPerson",
            "inetorgperson",
            "iplanet-am-user-service",
            "inetuser",
            "sunIdentityServerLibertyPPService",
            "sunFMSAML2NameIdentifier",
            "sunFederationManagerDataStore",
            "iplanet-am-managed-person",
            "iPlanetPreferences",
            FLEXPAY_PERSON_OBJECT_CLASS);
}
