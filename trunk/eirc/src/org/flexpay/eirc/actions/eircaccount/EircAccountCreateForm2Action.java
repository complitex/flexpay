package org.flexpay.eirc.actions.eircaccount;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class EircAccountCreateForm2Action extends FPActionWithPagerSupport<Person> {
	
	private static final String REDIRECT_FORM1 = "redirectForm1";

	private String personSearchString;
	private Long apartmentFilter;
	private List<Person> persons = list();

	private PersonService personService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (apartmentFilter == null || apartmentFilter <= 0) {
			addActionError(getText("eirc.error.account.create.no_apartment"));
			return REDIRECT_FORM1;
		}

		if (StringUtils.isEmpty(personSearchString)) {
			persons = personService.find(new Stub<Apartment>(apartmentFilter), getPager());
		} else {
			persons = personService.findByFIO("%" + personSearchString + "%", getPager());
		}

		log.debug("Found persons: {}", persons);

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
		return REDIRECT_FORM1;
	}

	public void setPersonSearchString(String personSearchString) {
		this.personSearchString = personSearchString;
	}

	public Long getApartmentFilter() {
		return apartmentFilter;
	}

	public void setApartmentFilter(Long apartmentFilter) {
		this.apartmentFilter = apartmentFilter;
	}

	public List<Person> getPersons() {
		return persons;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
