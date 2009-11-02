package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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

		apartment = apartmentService.readWithPersons(stub(apartment));
		if (apartment == null) {
			addActionError(getText("ab.error.apartment_not_found"));
			return SUCCESS;
		}
		if (apartment.isNew()) {
			addActionMessage(getText("ab.apartment.hasnt_registered_persons"));
			return SUCCESS;
		}

		List<BuildingAddress> buildingses = buildingService.getBuildingBuildings(apartment.getBuildingStub());
		buildings = buildingService.readFull(stub(buildingses.get(0)));
		street = streetService.readFull(buildings.getStreetStub());
		town = townService.readFull(street.getTownStub());
		region = regionService.readFull(town.getRegionStub());
		country = countryService.readFull(region.getCountryStub());

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
