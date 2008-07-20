package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;import static org.flexpay.common.persistence.Stub.stub;

import java.util.*;

public class ApartmentRegistrationsAction extends FPActionSupport {

	private ApartmentService apartmentService;
	private BuildingService buildingService;
	private TownService townService;
	private RegionService regionService;
	private CountryService countryService;

	private Apartment apartment;
	private Buildings buildings;
	private Town town;
	private Region region;
	private Country country;

	public String doExecute() throws FlexPayException {
		apartment = apartmentService.readWithPersons(apartment.getId());
		buildings = buildingService.readFull(stub(buildings));
		town = townService.readFull(buildings.getStreet().getParent().getId());
		region = regionService.readFull(town.getParent().getId());
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

}
