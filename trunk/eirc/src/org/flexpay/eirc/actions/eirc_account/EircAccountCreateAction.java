package org.flexpay.eirc.actions.eirc_account;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.EircAccountService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class EircAccountCreateAction extends FPActionSupport {

	private Long personId;
	private Long apartmentId;
	private EircAccount eircAccount;

	private EircAccountService eircAccountService;

	@NotNull
	public String doExecute() throws FlexPayExceptionContainer {
		if (apartmentId == null) {
			addActionError(getText("eirc.error.account.create.no_apartment"));
			return "redirectForm1";
		} else if (personId == null) {
			addActionError(getText("eirc.error.account.create.no_person"));
			return "redirectForm2";
		} else {
			eircAccount = new EircAccount();
			eircAccount.setApartment(new Apartment(apartmentId));
			eircAccount.setPerson(new Person(personId));
			eircAccountService.create(eircAccount);

			return REDIRECT_SUCCESS;
		}
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
		return "redirectForm2";
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public void setApartmentId(Long apartmentId) {
		this.apartmentId = apartmentId;
	}

	public Long getPersonId() {
		return personId;
	}

	public Long getApartmentId() {
		return apartmentId;
	}

	public EircAccount getEircAccount() {
		return eircAccount;
	}

	@Required
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

}
