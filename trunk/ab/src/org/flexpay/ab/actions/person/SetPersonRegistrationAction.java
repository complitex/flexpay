package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

public class SetPersonRegistrationAction extends FPActionSupport {

	private Person person = new Person();
	private Date beginDate;
	private Date endDate;
	private String countryFilter;
	private String regionFilter;
	private String townFilter;
	private String streetFilter;
	private String buildingFilter;
	private String apartmentFilter;

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

			Long apartmentFilterLong;

			try {
				apartmentFilterLong = Long.parseLong(apartmentFilter);
			} catch (Exception e) {
				log.warn("Incorrect apartment id in filter ({})", apartmentFilter);
				return REDIRECT_SUCCESS;
			}

			person = personService.read(stub(person));
			person.setPersonRegistration(new Apartment(apartmentFilterLong), beginDate, endDate);

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
	@Override
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
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
