package org.flexpay.eirc.actions.eirc_account;

import org.flexpay.ab.actions.person.ListPersons;

public class EircAccountCreateForm2Action extends ListPersons {
	
	private Long apartmentId;

	public String execute() throws Exception {
		if(apartmentId == null) {
			return "form1";
		}
		
		super.execute();
		
		return "form2";
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
