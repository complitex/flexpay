package org.flexpay.eirc.actions.eircaccount;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.EircAccountService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class EircAccountCreateAction extends FPActionSupport {

	private static final String REDIRECT_FORM1 = "redirectForm1";
	private static final String REDIRECT_FORM2 = "redirectForm2";

	private Long personId;
	private Long apartmentFilter;
	private EircAccount eircAccount = new EircAccount();

	private EircAccountService eircAccountService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayExceptionContainer {

		if (apartmentFilter == null || apartmentFilter <= 0) {
			addActionError(getText("eirc.error.account.create.no_apartment"));
			return REDIRECT_FORM1;
		} else if (personId == null || personId <= 0) {
			addActionError(getText("eirc.error.account.create.no_person"));
			return REDIRECT_FORM2;
		}

		eircAccount.setApartment(new Apartment(apartmentFilter));
		eircAccount.setPerson(new Person(personId));
		eircAccountService.create(eircAccount);

		return REDIRECT_SUCCESS;
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
		return REDIRECT_FORM2;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Long getPersonId() {
		return personId;
	}

	public Long getApartmentFilter() {
		return apartmentFilter;
	}

	public void setApartmentFilter(Long apartmentFilter) {
		this.apartmentFilter = apartmentFilter;
	}

	public EircAccount getEircAccount() {
		return eircAccount;
	}

	@Required
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

}
