package org.flexpay.common.dao.impl.ldap;

import org.flexpay.common.util.config.UserPreferences;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.filter.EqualsFilter;

import javax.naming.Name;

public class UidDnBuilder implements DnBuilder {

	/**
	 * Build relative to base distinguished name that is equals to user uid
	 *
	 * @param person UserPreferences
	 * @return Distinguished name
	 */
	@Override
	public Name buildDn(UserPreferences person) {

		DistinguishedName dn = new DistinguishedName();
		dn.append("uid", person.getUsername());
		return dn;
	}

	/**
	 * Build Filter from user name
	 *
	 * @param userName User name
	 * @return Filter object
	 */
	@Override
	public Filter getUserNameFilter(String userName) {
		return new EqualsFilter("uid", userName);
	}
}
