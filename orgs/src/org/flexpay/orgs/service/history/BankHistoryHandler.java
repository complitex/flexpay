package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.orgs.persistence.Bank;
import org.flexpay.orgs.persistence.BankDescription;
import org.jetbrains.annotations.NotNull;

public class BankHistoryHandler
		extends OrganizationInstanceHistoryHandler<BankDescription, Bank> {

    @Override
	protected Bank newInstance() {
		return new Bank();
	}

    @Override
	protected Class<Bank> getType() {
		return Bank.class;
	}

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
    @Override
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(Bank.class) == diff.getObjectType();
	}
}
