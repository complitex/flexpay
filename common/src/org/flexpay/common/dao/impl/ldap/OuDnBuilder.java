package org.flexpay.common.dao.impl.ldap;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;

import javax.naming.Name;

public class OuDnBuilder implements DnBuilder {
	private String ouDistinguishedName;

	@Override
	public Name buildDn() {
		DistinguishedName dn = new DistinguishedName();
		dn.append("ou", ouDistinguishedName);
		return dn;
	}

	@Override
	public Filter getNameFilter() {
		return new EqualsFilter("ou", ouDistinguishedName);
	}

	@Required
	public void setOuDistinguishedName(String ouDistinguishedName) {
		this.ouDistinguishedName = ouDistinguishedName;
	}
}
