package org.flexpay.eirc.action.quittance;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.service.QuittancePacketService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class QuittancePacketViewAction extends FPActionSupport {

	private QuittancePacket packet = new QuittancePacket();

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

		Stub<QuittancePacket> stub = stub(packet);
		packet = quittancePacketService.read(stub);

		if (packet == null) {
			log.warn("Can't get quittance packet with id {} from DB", stub.getId());
			addActionError(getText("eirc.error.quittance_packet.cant_get_quittance_packet"));
			return REDIRECT_ERROR;
		} else if (packet.isNotActive()) {
			log.warn("Quittance packet with id {} is disabled", stub.getId());
			addActionError(getText("eirc.error.quittance_packet.cant_get_quittance_packet"));
			return REDIRECT_ERROR;
		}

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
		return REDIRECT_ERROR;
	}

	public QuittancePacket getPacket() {
		return packet;
	}

	public void setPacket(QuittancePacket packet) {
		this.packet = packet;
	}

	@Required
	public void setQuittancePacketService(QuittancePacketService quittancePacketService) {
		this.quittancePacketService = quittancePacketService;
	}

}
