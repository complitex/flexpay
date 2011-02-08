package org.flexpay.common.dao.impl.ldap;

import org.flexpay.common.util.config.UserPreferences;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;

import javax.naming.Name;

public class ObjectClassDnBuilder implements UserPreferencesDnBuilder {

	/**
	 * Build relative to base distinguished name that is equals to  ObjectClass
	 *
	 * @param preferences UserPreferences
	 * @return Distinguished name
	 */
	@Override
	public Name buildDn(UserPreferences preferences) {
		DistinguishedName dn = new DistinguishedName();
		for (String objectClass : preferences.getObjectClasses()) {
			dn.append("objectClass", objectClass);
		}
		return dn;
	}

	/**
	 * Build Filter from object class
	 *
	 * @param objectClass Object class
	 * @return Filter object
	 */
	@Override
	public Filter getNameFilter(String objectClass) {
		return new EqualsFilter("objectClass", objectClass);
	}
}
