package org.flexpay.eirc.actions.eirc_account;

import org.flexpay.ab.actions.person.ListPersons;

public class EircAccountCreateForm2Action extends ListPersons {
	
	private Long apartmentId;

	public String doExecute() throws Exception {
		if(apartmentId == null) {
			return "form1";
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
	@Override
	protected String getErrorResult() {
		return "form1";
	}

	/**
	 * @return the apartmentId
	 */
	public Long getApartmentId() {
		return apartmentId;
	}

	/**
	 * @param apartmentId the apartmentId to set
	 */
	public void setApartmentId(Long apartmentId) {
		this.apartmentId = apartmentId;
	}
	
}
