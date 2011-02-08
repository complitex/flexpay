package org.flexpay.common.dao.impl.ldap;

import org.flexpay.common.util.config.UserPreferences;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;

import javax.naming.Name;

public class GroupRoleDnBuilder implements UserPreferencesDnBuilder {

	/**
	 * Build relative to base distinguished name that is equals to user role external id
	 *
	 * @param person UserPreferences
	 * @return Distinguished name or null if group role is null
	 */
	@Override
	public Name buildDn(UserPreferences person) {
		if (person.getUserRole() == null) {
			return null;
		}
		DistinguishedName dn = new DistinguishedName();
		dn.append("flexpayGroupRole", person.getUserRole().getExternalId());
		return dn;
	}

	/**
	 * Build Filter from user role
	 *
	 * @param externalId User role exteranl Id
	 * @return Filter object
	 */
	@Override
	public Filter getNameFilter(String externalId) {
		return new EqualsFilter("flexpayGroupRole", externalId);
	}
}
