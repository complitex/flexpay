package org.flexpay.orgs.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.orgs.persistence.Bank;
import org.flexpay.orgs.persistence.BankDescription;
import org.flexpay.orgs.service.BankService;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class BankServiceImpl extends OrganizationInstanceServiceImpl<BankDescription, Bank>
		implements BankService {

	protected String getSeveralInstancesErrorCode() {
		return "orgs.error.bank.several_instances";
	}

	protected String getInstanceExistsErrorCode() {
		return "orgs.error.bank.instance_exists";
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	protected void doValidate(@NotNull Bank bank, FlexPayExceptionContainer ex) {

		if (StringUtils.isBlank(bank.getBankIdentifierCode())) {
			ex.addException(new FlexPayException("No BIK", "orgs.error.bank.no_bank_identifier_code"));
		}

		if (StringUtils.isBlank(bank.getCorrespondingAccount())) {
			ex.addException(new FlexPayException(
					"No corresponding account", "orgs.error.bank.no_corresponding_account"));
		}
	}
}
