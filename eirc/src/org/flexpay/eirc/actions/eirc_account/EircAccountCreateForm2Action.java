package org.flexpay.eirc.actions.eirc_account;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class EircAccountCreateForm2Action extends FPActionWithPagerSupport<Person> {
	
	private String personSearchString;
	private String apartmentFilter;
	private List<Person> persons = new ArrayList<Person>();

	private PersonService personService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		Long apartmentIdLong = null;

		try {
			apartmentIdLong = Long.parseLong(apartmentFilter);
		} catch (Exception e) {
			log.warn("Incorrect apartment id in filter ({})", apartmentFilter);
			addActionError(getText("eirc.error.account.create.no_apartment"));
			return "redirectForm1";
		}

		if (StringUtils.isEmpty(personSearchString)) {
			persons = personService.getPersons(new Stub<Apartment>(apartmentIdLong), getPager());
		} else {
			persons = personService.findByFIO(getPager(), "%" + personSearchString + "%");
		}

		log.info("Found persons: {}", persons);

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

	public void setPersonSearchString(String personSearchString) {
		this.personSearchString = personSearchString;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public String getApartmentFilter() {
		return apartmentFilter;
	}

	public void setApartmentFilter(String apartmentFilter) {
		this.apartmentFilter = apartmentFilter;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
