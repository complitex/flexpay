package org.flexpay.eirc.persistence;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Set;
import java.util.Collections;

public class Bank extends DomainObjectWithStatus {

	private Organisation organisation;
	private Set<BankDescription> descriptions = Collections.emptySet();
	private Set<BankAccount> accounts = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public Bank() {
	}

	public Bank(@NotNull Long id) {
		super(id);
	}

	public Bank(@NotNull Stub<Bank> stub) {
		super(stub.getId());
	}

	@NotNull
	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(@NotNull Organisation organisation) {
		this.organisation = organisation;
	}

	public Set<BankAccount> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<BankAccount> accounts) {
		this.accounts = accounts;
	}

	@NotNull
	public Set<BankDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(@NotNull Set<BankDescription> descriptions) {
		this.descriptions = descriptions;
	}
}
