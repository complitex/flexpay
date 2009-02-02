package org.flexpay.eirc.actions.quittance;

import org.flexpay.ab.service.AddressService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.persistence.QuittancePayment;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.QuittancePacketService;
import org.flexpay.eirc.service.QuittancePaymentService;
import org.flexpay.eirc.service.QuittanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class QuittancePayAction extends FPActionSupport {

	private AddressService addressService;
	private EircAccountService eircAccountService;
	private QuittanceNumberService quittanceNumberService;
	private QuittanceService quittanceService;
	private QuittancePacketService quittancePacketService;
	private QuittancePaymentService quittancePaymentService;

	private QuittancePacket packet = new QuittancePacket();
	private Quittance quittance = new Quittance();
	// list of packets where quittance was payed
	private List<QuittancePacket> payedQuittancePackets = Collections.emptyList();
	private QuittancePayment payment = new QuittancePayment();

	@NotNull
	public String doExecute() {

		if (isSubmit()) {
			addActionError(getText("eirc.quittance.payment.saved_successfully"));
			return REDIRECT_SUCCESS;
		}

		payedQuittancePackets = quittancePaymentService.getPacketsWhereQuittancePayed(stub(quittance));

		quittance = quittanceService.readFull(stub(quittance));
		packet = quittancePacketService.read(stub(packet));

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

	public String getQuittanceNumber() {
		return quittanceNumberService.getNumber(quittance);
	}

	public String getAddress() throws Exception {
		return addressService.getAddress(quittance.getEircAccount().getApartmentStub(), getLocale());
	}

	public String getPersonFIO() {
		return eircAccountService.getPersonFIO(quittance.getEircAccount());
	}

	@NotNull
	public BigDecimal getSumm() {
		return null;
	}

	public String getMonth() {
		return new SimpleDateFormat("mm/yyyy").format(quittance.getDateTill());
	}

	public QuittancePacket getPacket() {
		return packet;
	}

	public void setPacket(QuittancePacket packet) {
		this.packet = packet;
	}

	public Quittance getQuittance() {
		return quittance;
	}

	public void setQuittance(Quittance quittance) {
		this.quittance = quittance;
	}

	public List<QuittancePacket> getPayedQuittancePackets() {
		return payedQuittancePackets;
	}

	@Required
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	@Required
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

	@Required
	public void setQuittanceNumberService(QuittanceNumberService quittanceNumberService) {
		this.quittanceNumberService = quittanceNumberService;
	}

	@Required
	public void setQuittancePacketService(QuittancePacketService quittancePacketService) {
		this.quittancePacketService = quittancePacketService;
	}

	@Required
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

	@Required
	public void setQuittancePaymentService(QuittancePaymentService quittancePaymentService) {
		this.quittancePaymentService = quittancePaymentService;
	}
}
