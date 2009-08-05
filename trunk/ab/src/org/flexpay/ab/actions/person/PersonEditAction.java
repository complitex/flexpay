package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

public class PersonEditAction extends FPActionSupport {

	private Person person = new Person();
	private Date beginDate = DateUtil.now();
	private Date endDate = ApplicationConfig.getFutureInfinite();
	private String editType;
	private String countryFilter;
	private String regionFilter;
	private String townFilter;
	private String streetFilter;
	private String buildingFilter;
	private String apartmentFilter;

	private String crumbCreateKey;
	private SetPersonRegistrationAction setPersonRegistrationAction;
	private PersonService personService;
	private RegionService regionService;
	private TownService townService;
	private StreetService streetService;
	private BuildingService buildingService;
	private ApartmentService apartmentService;

	@NotNull
	@Override
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

		if (countryFilter == null) {
			PersonRegistration registration = person.getCurrentRegistration();
			if (registration != null) {
				Building building = apartmentService.getBuilding(new Stub<Apartment>(registration.getApartment()));;
				BuildingAddress address = buildingService.getFirstBuildings(new Stub<Building>(building.getId()));
				Street street = streetService.readFull(address.getStreetStub());
				Town town = townService.readFull(street.getTownStub());
				Region region = regionService.readFull(town.getRegionStub());
				apartmentFilter = registration.getApartment().getId() + "";
				buildingFilter = address.getId() + "";
				streetFilter = address.getStreetStub().getId() + "";
				townFilter = town.getId() + "";
				regionFilter = region.getId() + "";
				countryFilter = region.getCountryStub().getId() + "";
				beginDate = registration.getBeginDate();
				endDate = registration.getEndDate();
			}
		}

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
		setPersonRegistrationAction.setBuildingFilter(buildingFilter);
		setPersonRegistrationAction.setApartmentFilter(apartmentFilter);
		setPersonRegistrationAction.setSubmitted(submitted);
		setPersonRegistrationAction.setSession(session);
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

	public String getCountryFilter() {
		return countryFilter;
	}

	public void setCountryFilter(String countryFilter) {
		this.countryFilter = countryFilter;
	}

	public String getRegionFilter() {
		return regionFilter;
	}

	public void setRegionFilter(String regionFilter) {
		this.regionFilter = regionFilter;
	}

	public String getTownFilter() {
		return townFilter;
	}

	public void setTownFilter(String townFilter) {
		this.townFilter = townFilter;
	}

	public String getStreetFilter() {
		return streetFilter;
	}

	public void setStreetFilter(String streetFilter) {
		this.streetFilter = streetFilter;
	}

	public String getBuildingFilter() {
		return buildingFilter;
	}

	public void setBuildingFilter(String buildingFilter) {
		this.buildingFilter = buildingFilter;
	}

	public String getApartmentFilter() {
		return apartmentFilter;
	}

	public void setApartmentFilter(String apartmentFilter) {
		this.apartmentFilter = apartmentFilter;
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

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
