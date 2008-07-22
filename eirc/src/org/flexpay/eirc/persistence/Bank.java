package org.flexpay.eirc.persistence;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.set;
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

	public void setDescription(BankDescription bankDescription) {
		if (descriptions == Collections.EMPTY_SET) {
			descriptions = set();
		}

		BankDescription candidate = null;
		for (BankDescription description : descriptions) {
			if (description.isSameLanguage(bankDescription)) {
				candidate = description;
				break;
			}
		}

		if (candidate != null) {
			if (StringUtils.isBlank(bankDescription.getName())) {
				descriptions.remove(candidate);
				return;
			}
			candidate.setName(bankDescription.getName());
			return;
		}

		if (StringUtils.isBlank(bankDescription.getName())) {
			return;
		}

		bankDescription.setTranslatable(this);
		descriptions.add(bankDescription);
	}

}
