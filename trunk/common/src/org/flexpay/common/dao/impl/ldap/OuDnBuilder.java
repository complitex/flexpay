package org.flexpay.common.dao.impl.ldap;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.filter.OrFilter;

import javax.naming.Name;
import java.util.List;

public class OuDnBuilder implements DnBuilder {

	private String ouDistinguishedName;

	@Override
	public Name buildDn(List<String> names) {
		DistinguishedName dn = new DistinguishedName();
		dn.append("ou", ouDistinguishedName);
		if (names != null && names.size() > 0) {
			dn.append("uid", names.get(0));
		}
		return dn;
	}

	@Override
	public Filter getNameFilter(List<String> names) {
		if (names != null && names.size() > 0) {
			AndFilter resultFilter = new AndFilter();
			OrFilter uidFilter = new OrFilter();
			for (String name : names) {
				uidFilter.or(new EqualsFilter("uid", name));
			}
			resultFilter.and(uidFilter);
			resultFilter.and(new EqualsFilter("ou", ouDistinguishedName));
			return resultFilter;
		}
		return new EqualsFilter("ou", ouDistinguishedName);
	}

	@Required
	public void setOuDistinguishedName(String ouDistinguishedName) {
		this.ouDistinguishedName = ouDistinguishedName;
	}
}
