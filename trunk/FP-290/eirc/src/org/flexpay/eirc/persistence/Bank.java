package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class Bank extends OrganizationInstance<BankDescription, Bank> {

	private Set<BankAccount> accounts = Collections.emptySet();

	private String bankIdentifierCode;
	private String correspondingAccount;

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

	public Set<BankAccount> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<BankAccount> accounts) {
		this.accounts = accounts;
	}

	public String getBankIdentifierCode() {
		return bankIdentifierCode;
	}

	public void setBankIdentifierCode(String bankIdentifierCode) {
		this.bankIdentifierCode = bankIdentifierCode;
	}

	public String getCorrespondingAccount() {
		return correspondingAccount;
	}

	public void setCorrespondingAccount(String correspondingAccount) {
		this.correspondingAccount = correspondingAccount;
	}
}
