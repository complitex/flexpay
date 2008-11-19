package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ApartmentRegistrationAction extends FPActionSupport {

	private ApartmentService apartmentService;
	private BuildingService buildingService;
	private StreetService streetService;
	private TownService townService;
	private RegionService regionService;
	private CountryService countryService;

	private Apartment apartment = new Apartment();
	private Buildings buildings;
	private Street street;
	private Town town;
	private Region region;
	private Country country;

	@NotNull
	public String doExecute() throws FlexPayException {

		apartment = apartmentService.readWithPersons(stub(apartment));
		List<Buildings> buildingses = buildingService.getBuildingBuildings(apartment.getBuildingStub());
		buildings = buildingService.readFull(stub(buildingses.get(0)));
		street = streetService.readFull(buildings.getStreetStub());
		town = townService.readFull(street.getTownStub());
		region = regionService.readFull(town.getRegionStub());
		country = countryService.readFull(region.getParent().getId());

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
		return SUCCESS;
	}

	public List<PersonRegistration> sortPersonRegistrations(Set<PersonRegistration> registrations) {
		List<PersonRegistration> result = new ArrayList<PersonRegistration>(registrations);

		Collections.sort(result, new Comparator<PersonRegistration>() {
			public int compare(PersonRegistration o1, PersonRegistration o2) {
				return o1.getBeginDate().compareTo(o2.getBeginDate());
			}
		});

		return result;
	}

	/**
	 * @return the apartment
	 */
	public Apartment getApartment() {
		return apartment;
	}

	/**
	 * @param apartment the apartment to set
	 */
	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Street getStreet() {
		return street;
	}

	/**
	 * @return the buildings
	 */
	public Buildings getBuildings() {
		return buildings;
	}

	/**
	 * @return the town
	 */
	public Town getTown() {
		return town;
	}

	/**
	 * @return the region
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * @param buildings the buildings to set
	 */
	public void setBuildings(Buildings buildings) {
		this.buildings = buildings;
	}

	/**
	 * @param apartmentService the apartmentService to set
	 */
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	/**
	 * @param buildingService the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	/**
	 * @param townService the townService to set
	 */
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	/**
	 * @param regionService the regionService to set
	 */
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	/**
	 * @param countryService the countryService to set
	 */
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}
}
