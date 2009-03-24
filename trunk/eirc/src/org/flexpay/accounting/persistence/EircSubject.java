package org.flexpay.accounting.persistence;

import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.Organization;

public class EircSubject implements Subject{
	private EircAccount eircAccount;
	private Organization organization;

	public EircAccount getEircAccount() {
		return eircAccount;
	}

	public void setEircAccount(EircAccount eircAccount) {
		this.eircAccount = eircAccount;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
}
