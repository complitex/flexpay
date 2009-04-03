package org.flexpay.payments.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.orgs.persistence.Organization;

public class EircSubject extends DomainObject implements Subject {

//	private EircAccount eircAccount;
	private Organization organization;

//	public EircAccount getEircAccount() {
//		return eircAccount;
//	}
//
//	public void setEircAccount(EircAccount eircAccount) {
//		this.eircAccount = eircAccount;
//	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("EircSubject {").
				append("id", getId()).
//				append("eircAccount.id", eircAccount.getId()).
				append("organization.id", organization.getId()).
				append("}").toString();
	}

}
