package org.flexpay.orgs.persistence.filters;

import org.flexpay.orgs.persistence.Bank;
import org.flexpay.orgs.persistence.BankDescription;

import java.util.List;

public class BankFilter
		extends OrganizationInstanceFilter<BankDescription, Bank> {

	private List<Bank> instances;

	public BankFilter() {
		super(-1L);
	}

	public List<Bank> getInstances() {
		return instances;
	}

	public void setInstances(List<Bank> instances) {
		this.instances = instances;
	}
}
