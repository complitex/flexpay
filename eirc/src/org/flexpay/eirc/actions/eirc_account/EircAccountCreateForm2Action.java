package org.flexpay.eirc.actions.eirc_account;

import org.flexpay.ab.actions.person.PersonsListAction;
import org.jetbrains.annotations.NotNull;

public class EircAccountCreateForm2Action extends PersonsListAction {
	
	private Long apartmentId;

	@NotNull
	public String doExecute() throws Exception {
		if(apartmentId == null) {
			addActionError(getText("eirc.error.account.create.no_apartment"));
			return "redirectForm1";
		}
		
		super.doExecute();
		
		return "form2";
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
		return "redirectForm1";
	}

	public Long getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(Long apartmentId) {
		this.apartmentId = apartmentId;
	}

}
