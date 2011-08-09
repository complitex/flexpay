package org.flexpay.orgs.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class Bank extends OrganizationInstance<BankDescription, Bank> {

	private String bankIdentifierCode;
	private String correspondingAccount;

	private Set<BankAccount> accounts = set();

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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("bankIdentifierCode", bankIdentifierCode).
				append("correspondingAccount", correspondingAccount).
				toString();
	}

}
