package org.flexpay.orgs.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.orgs.persistence.Bank;
import org.flexpay.orgs.persistence.BankDescription;
import org.flexpay.orgs.service.BankService;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class BankServiceImpl extends OrganisationInstanceServiceImpl<BankDescription, Bank>
		implements BankService {

	protected String getSeveralInstancesErrorCode() {
		return "eirc.error.bank.several_instances";
	}

	protected String getInstanceExistsErrorCode() {
		return "eirc.error.bank.instance_exists";
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	protected void doValidate(@NotNull Bank bank, FlexPayExceptionContainer ex) {

		if (StringUtils.isBlank(bank.getBankIdentifierCode())) {
			ex.addException(new FlexPayException("No BIK", "eirc.error.bank.no_bank_identifier_code"));
		}

		if (StringUtils.isBlank(bank.getCorrespondingAccount())) {
			ex.addException(new FlexPayException(
					"No corresponding account", "eirc.error.bank.no_corresponding_account"));
		}
	}
}
