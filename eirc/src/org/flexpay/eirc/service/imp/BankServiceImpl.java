package org.flexpay.eirc.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.dao.BankDao;
import org.flexpay.eirc.persistence.Bank;
import org.flexpay.eirc.persistence.BankDescription;
import org.flexpay.eirc.service.BankService;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class BankServiceImpl
		extends OrganisationInstanceServiceImpl<BankDescription, Bank>
		implements BankService {

	private BankDao bankDao;

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

	public void setBankDao(BankDao bankDao) {
		this.bankDao = bankDao;
	}
}
