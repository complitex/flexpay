package org.flexpay.ab.dao.impl.ldap;

import org.flexpay.common.util.CollectionUtils;

import java.util.List;

abstract public class LdapConstants {
	public static final String FLEXPAY_PERSON_OBJECT_CLASS = "flexpayAbPerson";

	public static final List<String> REQUIRED_OBJECT_CLASSES = CollectionUtils.list(
			FLEXPAY_PERSON_OBJECT_CLASS);
}
