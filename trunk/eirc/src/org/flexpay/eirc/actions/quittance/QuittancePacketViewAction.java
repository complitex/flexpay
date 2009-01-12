package org.flexpay.eirc.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.persistence.QuittancePayment;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.QuittancePacketService;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.ab.persistence.Person;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Collections;

public class QuittancePacketViewAction extends FPActionSupport {

	private AddressService addressService;
	private PersonService personService;
	private QuittancePacketService quittancePacketService;

	private QuittancePacket packet = new QuittancePacket();
	private List<QuittancePayment> payments = Collections.emptyList();
	private Page<QuittancePayment> pager = new Page<QuittancePayment>();

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
			addActionError(getText("error.no_id"));
			return REDIRECT_ERROR;
		}

		packet = quittancePacketService.read(stub(packet));
		if (packet == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_ERROR;
		}

		payments = quittancePacketService.listPayments(stub(packet), pager);

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

		Stub<Person> personStub = quittance.getEircAccount().getPersonStub();
		if (personStub == null) {
			return quittance.getEircAccount().getConsumerInfo().getFIO();
		}

		Person person = personService.read(personStub);
		if (person == null) {
			log.error("No person found {}", personStub);
			return "ERROR";
		}
		return person.getFIO();
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

	public Page<QuittancePayment> getPager() {
		return pager;
	}

	public void setPager(Page<QuittancePayment> pager) {
		this.pager = pager;
	}

	@Required
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@Required
	public void setQuittancePacketService(QuittancePacketService quittancePacketService) {
		this.quittancePacketService = quittancePacketService;
	}
}
