package org.flexpay.ab.actions.person;

import org.flexpay.ab.actions.apartment.ApartmentFilterDependentAction;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NonNls;

import java.util.Date;

public class SetRegistrationAction extends ApartmentFilterDependentAction {

	@NonNls
	public static final String DATE_FORMAT = "dd/MM/yyyy";

	private PersonService personService;

	private Person person = new Person();
	private Date beginDate;
	private Date endDate;

	public String doExecute() throws Exception {

		if (isSubmitted()) {
			if (!apartmentFilter.needFilter()) {
				throw new FlexPayException("No apartment", "ab.person.apartment_absent");
			}

			person = personService.read(stub(person));
			person.setPersonRegistration(new Apartment(apartmentFilter.getSelectedId()), beginDate, endDate);
//			personService.save(person);
		}
		return REDIRECT_SUCCESS;
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
		return REDIRECT_ERROR;
	}

	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}


	/**
	 * @return the beginDate
	 */
	public String getBeginDate() {
		return format(beginDate);
	}


	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(String beginDate) {
		this.beginDate = DateUtil.parseBeginDate(beginDate);
	}


	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return format(endDate);
	}


	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = DateUtil.parseEndDate(endDate);
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
}
