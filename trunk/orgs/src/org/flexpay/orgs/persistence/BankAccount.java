package org.flexpay.orgs.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

public class BankAccount extends DomainObjectWithStatus {

	private Bank bank;
	private Organization juridicalPerson;
	private String accountNumber;
	private boolean isDefault = false;

	public BankAccount() {
	}

	public BankAccount(@NotNull Long id) {
		super(id);
	}

	public BankAccount(@NotNull Stub<BankAccount> stub) {
		super(stub.getId());
	}

	@NotNull
	public Bank getBank() {
		return bank;
	}

	public void setBank(@NotNull Bank bank) {
		this.bank = bank;
	}

	@NotNull
	public Organization getJuridicalPerson() {
		return juridicalPerson;
	}

	public void setJuridicalPerson(@NotNull Organization juridicalPerson) {
		this.juridicalPerson = juridicalPerson;
	}

	@NotNull
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(@NotNull String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Check if this account is default for juridical person
	 *
	 * @return <code>true</code> if account is default, or <code>false</code> otherwise
	 */
	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("accountNumber", accountNumber).
				append("isDefault", isDefault).
				toString();
	}

}
