package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.DateUtil;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

public class PersonSaveRegistrationAction extends FPActionSupport {

	private Person person = new Person();
	private Date beginDate = DateUtil.now();
	private Date endDate = getFutureInfinite();
	private Long apartmentFilter;

	private PersonService personService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (apartmentFilter == null || apartmentFilter <= 0) {
			addActionError(getText("common.error.invalid_id"));
			return SUCCESS;
		}

		person = personService.readFull(stub(person));
		person.setPersonRegistration(new Apartment(apartmentFilter), beginDate, endDate);

		if (person.isNew()) {
			return SUCCESS;
		}
		personService.update(person);

		addActionMessage(getText("ab.person.registration.saved"));

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
		return SUCCESS;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setApartmentFilter(Long apartmentFilter) {
		this.apartmentFilter = apartmentFilter;
	}

	public String getBeginDate() {
		return format(beginDate);
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = DateUtil.parseBeginDate(beginDate);
	}

	public String getEndDate() {
		return format(endDate);
	}

	public void setEndDate(String endDate) {
		this.endDate = DateUtil.parseEndDate(endDate);
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
