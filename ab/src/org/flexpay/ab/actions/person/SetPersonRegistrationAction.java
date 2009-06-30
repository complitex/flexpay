package org.flexpay.ab.actions.person;

import org.flexpay.ab.actions.apartment.ApartmentFilterDependentAction;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

public class SetPersonRegistrationAction extends ApartmentFilterDependentAction {

	private Person person = new Person();
	private Date beginDate;
	private Date endDate;

	private PersonService personService;

	@NotNull
	public String doExecute() throws Exception {

		if (isSubmit()) {
			if (!apartmentFilter.needFilter()) {
				throw new FlexPayException("No apartment", "error.ab.person.apartment_absent");
			}

			person = personService.read(stub(person));
			person.setPersonRegistration(new Apartment(apartmentFilter.getSelectedId()), beginDate, endDate);

			if (person.isNew()) {
				personService.create(person);
			} else {
				personService.update(person);
			}
		}
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
		return ERROR;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getBeginDate() {
		return format(beginDate);
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = DateUtil.parseBeginDate(beginDate);
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
