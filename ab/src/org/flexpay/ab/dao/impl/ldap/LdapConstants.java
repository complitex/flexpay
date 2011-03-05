package org.flexpay.ab.dao.impl.ldap;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

abstract public class LdapConstants {
	public static final String FLEXPAY_PERSON_OBJECT_CLASS = "flexpayAbPerson";

	public static final List<String> REQUIRED_OBJECT_CLASSES = list(FLEXPAY_PERSON_OBJECT_CLASS);
}
