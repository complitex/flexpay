package org.flexpay.eirc.actions.quittance;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.QuittanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class QuittancePaySearchAction extends FPActionSupport {

	// form data
	private String quittanceNumber;
	private Long quittanceId;

	// required services
	private QuittanceService quittanceService;

	@NotNull
    @Override
	protected String doExecute() throws Exception {

		if (isSubmit()) {

			Quittance quittance = quittanceService.findByNumber(quittanceNumber);

			if (quittance == null) {
				addActionError(getText("eirc.error.quittance.no_quittance_found"));
				return INPUT;
			}

			if (alreadyPayed(quittanceNumber)) {
				addActionError(getText("eirc.quittances.quittance_pay_search.already_payed"));
				return INPUT;
			}

			quittanceId = quittance.getId();
			return REDIRECT_SUCCESS;
		}

		return INPUT;
	}

	private boolean alreadyPayed(String quittanceNumber) {
		return session.get(quittanceNumber) != null;
	}

	@Override
	public void validate() {

		if (isSubmit()) {
			if (StringUtils.isEmpty(quittanceNumber)) {
				addActionError(getText("eirc.error.quittance.invalid_number"));
			}
		}
	}

	@NotNull
    @Override
	protected String getErrorResult() {
		return INPUT;
	}

	// get/set form data
	public void setQuittanceNumber(String quittanceNumber) {
		this.quittanceNumber = quittanceNumber;
	}

	public String getQuittanceNumber() {
		return quittanceNumber;
	}

	public Long getQuittanceId() {
		return quittanceId;
	}

	// required services setters
	@Required
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}
}
