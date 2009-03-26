package org.flexpay.eirc.actions.quittance;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.actions.eirc_account.EircAccountsListAction;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.QuittanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class QuittanceSearchByAddressAction extends EircAccountsListAction {

	private Quittance quittance;

	private QuittanceService quittanceService;

	@NotNull
	@Override
	public String doExecute() {
		String result = super.doExecute();

		if (eircAccounts.size() != 1) {
			return result;
		}

		EircAccount account = eircAccounts.get(0);
		quittance = getAccountQuittance(account.getId());
		if (quittance == null) {
			addActionError(getText("eirc.error.quittance.no_for_single_account"));
			return result;
		}

		return REDIRECT_SUCCESS;
	}

	public Quittance getQuittance() {
		return quittance;
	}

	public Quittance getAccountQuittance(Long accountId) {

		List<Quittance> quittances = quittanceService.getLatestAccountQuittances(
				new Stub<EircAccount>(accountId), new Page<Quittance>());
		if (quittances.isEmpty()) {
			return null;
		}

		return quittances.get(0);
	}

	@Required
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}
}
