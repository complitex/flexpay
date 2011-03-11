package org.flexpay.eirc.actions.quittance;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.QuittanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class QuittanceSearchAction extends FPActionSupport {

	private String quittanceNumber;
	private Quittance quittance = new Quittance();
	private QuittancePacket packet = new QuittancePacket();

	private QuittanceService quittanceService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
    @Override
	protected String doExecute() throws Exception {

		if (isSubmit()) {
			if (StringUtils.isBlank(quittanceNumber)) {
				addActionError(getText("eirc.error.quittance.no_number_specified"));
				return INPUT;
			}

			quittance = quittanceService.findByNumber(quittanceNumber);
			if (quittance == null) {
				addActionError(getText("eirc.error.quittance.no_quittance_found"));
			}

			return REDIRECT_SUCCESS;
		}
		return INPUT;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
    @Override
	protected String getErrorResult() {
		return INPUT;
	}

	public Quittance getQuittance() {
		return quittance;
	}

	public String getQuittanceNumber() {
		return quittanceNumber;
	}

	public void setQuittanceNumber(String quittanceNumber) {
		this.quittanceNumber = quittanceNumber;
	}

	public QuittancePacket getPacket() {
		return packet;
	}

	public void setPacket(QuittancePacket packet) {
		this.packet = packet;
	}

	@Required
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

}
