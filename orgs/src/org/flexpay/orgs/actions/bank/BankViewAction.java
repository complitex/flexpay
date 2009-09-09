package org.flexpay.orgs.actions.bank;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.orgs.persistence.Bank;
import org.flexpay.orgs.service.BankService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class BankViewAction extends FPActionSupport {

	private Bank bank = new Bank();

	private BankService bankService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (bank.isNew()) {
			log.error(getText("error.invalid_id"));
			addActionError(getText("error.invalid_id"));
			return REDIRECT_ERROR;
		}
		bank = bankService.read(stub(bank));

		if (bank == null) {
			log.error(getText("common.object_not_selected"));
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	@Required
	public void setBankService(BankService bankService) {
		this.bankService = bankService;
	}

}
