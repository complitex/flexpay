package org.flexpay.eirc.action.quittance;

import org.flexpay.ab.service.AddressService;
import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.persistence.QuittancePayment;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.QuittancePacketService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;

public class QuittancePaymentsListAction extends FPActionWithPagerSupport<QuittancePayment> {

	private QuittancePacket packet = new QuittancePacket();
	private List<QuittancePayment> payments = list();

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
	@Override
	protected String doExecute() throws Exception {

		if (packet == null || packet.isNew()) {
			log.warn("Incorrect quittance packet id");
			addActionError(getText("eirc.error.quittance_packet.incorrect_quittance_packet_id"));
			return SUCCESS;
		}

		Stub<QuittancePacket> stub = stub(packet);
		packet = quittancePacketService.read(stub);

		if (packet == null) {
			log.warn("Can't get apartment with id {} from DB", stub.getId());
			addActionError(getText("eirc.error.quittance_packet.cant_get_quittance_packet"));
			return SUCCESS;
		} else if (packet.isNotActive()) {
			log.warn("Apartment with id {} is disabled", stub.getId());
			addActionError(getText("eirc.error.quittance_packet.cant_get_quittance_packet"));
			return SUCCESS;
		}

		payments = quittancePacketService.listPayments(stub, getPager());

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
	@Override
	protected String getErrorResult() {
		return SUCCESS;
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
