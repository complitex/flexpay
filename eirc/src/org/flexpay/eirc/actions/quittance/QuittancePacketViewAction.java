package org.flexpay.eirc.actions.quittance;

import org.flexpay.ab.service.AddressService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.persistence.QuittancePayment;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.QuittancePacketService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class QuittancePacketViewAction extends FPActionWithPagerSupport<QuittancePayment> {

	private QuittancePacket packet = new QuittancePacket();
	private List<QuittancePayment> payments = Collections.emptyList();

	private AddressService addressService;
	private EircAccountService eircAccountService;
	private QuittancePacketService quittancePacketService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected String doExecute() throws Exception {

		if (packet.isNew()) {
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		packet = quittancePacketService.read(stub(packet));
		if (packet == null) {
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		payments = quittancePacketService.listPayments(stub(packet), getPager());

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public String getAddress(@NotNull Quittance quittance) throws Exception {
		return addressService.getAddress(quittance.getEircAccount().getApartmentStub(), getLocale());
	}

	public String getPersonFIO(@NotNull Quittance quittance) {

		return eircAccountService.getPersonFIO(quittance.getEircAccount());
	}

	public QuittancePacket getPacket() {
		return packet;
	}

	public void setPacket(QuittancePacket packet) {
		this.packet = packet;
	}

	public List<QuittancePayment> getPayments() {
		return payments;
	}

	@Required
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

	@Required
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	@Required
	public void setQuittancePacketService(QuittancePacketService quittancePacketService) {
		this.quittancePacketService = quittancePacketService;
	}

}
