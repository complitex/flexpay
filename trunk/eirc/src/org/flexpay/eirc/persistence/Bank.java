package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class Bank extends DomainObjectWithStatus {

	private Organisation organisation;
	private Set<BankDescription> descriptions = Collections.emptySet();
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

	@NotNull
	public Set<BankDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(@NotNull Set<BankDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public void setDescription(BankDescription description) {
		descriptions = TranslationUtil.setTranslation(descriptions, this, description);
	}

}
