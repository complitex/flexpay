package org.flexpay.ab.actions.apartment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.PersonRegistration;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;

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
	
	public String execute() throws FlexPayException {
		apartment = apartmentService.readWithPersons(apartment.getId());
		buildings = buildingService.readFull(buildings.getId());
		town = townService.readFull(buildings.getStreet().getParent().getId());
		region = regionService.readFull(town.getParent().getId());
		country = countryService.readFull(region.getParent().getId());
		
		
		return "success";
	}
	
	public List<PersonRegistration> sortPersonRegistrations(Set<PersonRegistration> registrations) {
		List<PersonRegistration> result = new ArrayList<PersonRegistration>(registrations);
		
		Collections.sort(result, new Comparator () {
	        public int compare(Object o1, Object o2) {
	        	PersonRegistration pr1 = (PersonRegistration)o1;
	        	PersonRegistration pr2 = (PersonRegistration)o2;
	            return pr1.getBeginDate().compareTo(pr2.getBeginDate());
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
