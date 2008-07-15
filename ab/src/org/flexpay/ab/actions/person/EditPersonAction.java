package org.flexpay.ab.actions.person;

import org.flexpay.ab.actions.apartment.ApartmentFilterDependentAction;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.persistence.PersonRegistration;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.ab.util.config.ApplicationConfig;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.DateIntervalUtil;

import java.util.Date;

public class EditPersonAction extends ApartmentFilterDependentAction {

	private PersonService personService;
	private ApartmentService apartmentService;

	private Person person = new Person();
	private Date beginDate = DateIntervalUtil.now();
	private Date endDate = ApplicationConfig.getFutureInfinite();

	public String doExecute() throws Exception {

		if (person.getId() == null) {
			log.info("No person id specified");
			addActionError(getText("error.ab.person.id_not_specified"));
			return REDIRECT_ERROR;
		}

		if (person.isNotNew()) {
			person = personService.read(stub(person));
			if (person == null) {
				addActionError(getText("error.ab.person.invalid_id"));
				return REDIRECT_ERROR;
			}
		}

		if (getCountryFilter().getSelectedId() == null) {
			PersonRegistration registration = person.getCurrentRegistration();
			if (registration != null) {
				apartmentService.fillFilterIds(stub(registration.getApartment()), getFilters());
			}
		}

		initFilters();

		if (log.isDebugEnabled()) {
			log.debug("Buildings: " + buildingsFilter.getBuildingses());
		}

		return INPUT;
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
		return INPUT;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public PersonIdentity getFIOIdentity() {
		PersonIdentity fio = person.getFIOIdentity();

		if (log.isDebugEnabled()) {
			log.debug("Person FIO: " + fio);
		}

		return fio != null ? fio : new PersonIdentity();
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}
}
