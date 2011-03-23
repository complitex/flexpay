package org.flexpay.ab.action.apartment;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;

public class ApartmentRegistrationAction extends FPActionSupport {

	private Apartment apartment = Apartment.newInstance();
	private BuildingAddress buildings;
	private Street street;
	private Town town;
	private Region region;
	private Country country;

	private ApartmentService apartmentService;
	private BuildingService buildingService;
	private StreetService streetService;
	private TownService townService;
	private RegionService regionService;
	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		if (apartment == null || apartment.isNew()) {
			log.warn("Incorrect apartment id");
			addActionError(getText("ab.error.apartment.incorrect_apartment_id"));
			return REDIRECT_ERROR;
		}

		Stub<Apartment> stub = stub(apartment);
		apartment = apartmentService.readWithPersons(stub);
		if (apartment.isNotActive()) {
			log.warn("Apartment with id {} is disabled", stub.getId());
			addActionError(getText("ab.error.apartment.cant_get_apartment"));
			return REDIRECT_ERROR;
		} else if (apartment.getPersonRegistrations().isEmpty()) {
			log.debug("In apartment with id {} no registered persons", stub.getId());
			addActionMessage(getText("ab.apartment.hasnt_registered_persons"));
			return SUCCESS;
		}

		buildings = buildingService.readFullAddress(stub(apartment.getDefaultBuildings()));
		street = streetService.readFull(stub(apartment.getDefaultStreet()));
		town = townService.readFull(stub(apartment.getTown()));
		region = regionService.readFull(stub(apartment.getRegion()));
		country = countryService.readFull(stub(apartment.getCountry()));

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

	public List<PersonRegistration> sortPersonRegistrations(Set<PersonRegistration> registrations) {
		List<PersonRegistration> result = list(registrations);

		Collections.sort(result, new Comparator<PersonRegistration>() {
			@Override
			public int compare(PersonRegistration o1, PersonRegistration o2) {
				return o1.getBeginDate().compareTo(o2.getBeginDate());
			}
		});

		return result;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Street getStreet() {
		return street;
	}

	public BuildingAddress getBuildings() {
		return buildings;
	}

	public Town getTown() {
		return town;
	}

	public Region getRegion() {
		return region;
	}

	public Country getCountry() {
		return country;
	}

	public void setBuildings(BuildingAddress buildingAddress) {
		this.buildings = buildingAddress;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

}
