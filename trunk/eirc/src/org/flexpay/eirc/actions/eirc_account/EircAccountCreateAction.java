package org.flexpay.eirc.actions.eirc_account;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.EircAccountService;

public class EircAccountCreateAction {
	
	private EircAccountService eircAccountService;
	
	private Long personId;
	private Long apartmentId;
	private EircAccount eircAccount;
	
	
	public String execute() throws FlexPayExceptionContainer {
		if(apartmentId == null) {
			return "form1";
		} else if(personId == null) {
			return "form2";
		} else {
			eircAccount = new EircAccount();
			eircAccount.setApartment(new Apartment(apartmentId));
			eircAccount.setPerson(new Person(personId));
			eircAccountService.save(eircAccount);
			
			return "view";
		}
	}


	/**
	 * @param eircAccountService the eircAccountService to set
	 */
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}


	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(Long personId) {
		this.personId = personId;
	}


	/**
	 * @param apartmentId the apartmentId to set
	 */
	public void setApartmentId(Long apartmentId) {
		this.apartmentId = apartmentId;
	}


	/**
	 * @return the personId
	 */
	public Long getPersonId() {
		return personId;
	}


	/**
	 * @return the apartmentId
	 */
	public Long getApartmentId() {
		return apartmentId;
	}


	/**
	 * @return the eircAccount
	 */
	public EircAccount getEircAccount() {
		return eircAccount;
	}

}
