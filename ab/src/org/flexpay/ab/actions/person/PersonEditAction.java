package org.flexpay.ab.actions.person;

import org.flexpay.ab.actions.apartment.ApartmentFilterDependentAction;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.persistence.PersonRegistration;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.ab.util.config.ApplicationConfig;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

public class PersonEditAction extends ApartmentFilterDependentAction {

	private Person person = new Person();
	private Date beginDate = DateUtil.now();
	private Date endDate = ApplicationConfig.getFutureInfinite();
	private String editType;

	private String crumbCreateKey;
	private SetPersonRegistrationAction setPersonRegistrationAction;
	private PersonService personService;
	private ApartmentService apartmentService;

    @NotNull
	public String doExecute() throws Exception {

		if (isSubmit()) {
			processSubmit();
            return SUCCESS;
        }

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
				beginDate = registration.getBeginDate();
				endDate = registration.getEndDate();
			}
		}

		initFilters();
		apartmentFilter.setNeedAutoChange(false);

		return INPUT;
	}

	@SuppressWarnings ({"unchecked"})
	private void processSubmit() throws Exception {
		if ("registration".equals(editType)) {
			initSetPersonRegistrationAction();
			// ignore set registration result
			try {
				setPersonRegistrationAction.execute();
				beginDate = DateUtil.now();
				endDate = ApplicationConfig.getFutureInfinite();
			} finally {
				addActionErrors(setPersonRegistrationAction.getActionErrors());
			}
		}
	}

	private void initSetPersonRegistrationAction() {
		setPersonRegistrationAction.setPerson(person);
		setPersonRegistrationAction.setBeginDate(beginDate);
		setPersonRegistrationAction.setEndDate(endDate);
		setPersonRegistrationAction.setCountryFilter(countryFilter);
		setPersonRegistrationAction.setRegionFilter(regionFilter);
		setPersonRegistrationAction.setTownFilter(townFilter);
		setPersonRegistrationAction.setStreetFilter(streetFilter);
		setPersonRegistrationAction.setBuildingsFilter(buildingsFilter);
		setPersonRegistrationAction.setApartmentFilter(apartmentFilter);
		setPersonRegistrationAction.setSubmitted(submitted);
		setPersonRegistrationAction.setSession(session);
		setPersonRegistrationAction.setUserPreferences(userPreferences);
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
		return INPUT;
	}

	@Override
	protected void setBreadCrumbs() {
		if (person.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public PersonIdentity getFIOIdentity() {
		PersonIdentity fio = person.getFIOIdentity();

		log.debug("Person FIO: {}", fio);

		return fio != null ? fio : new PersonIdentity();
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public void setBeginDateStr(String date) {
		this.beginDate = DateUtil.parseBeginDate(date);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setEndDateStr(String date) {
		endDate = DateUtil.parseEndDate(date);
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	public void setSetPersonRegistrationAction(SetPersonRegistrationAction setPersonRegistrationAction) {
		log.debug("Setting setPersonRegistrationAction");
		this.setPersonRegistrationAction = setPersonRegistrationAction;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

}
