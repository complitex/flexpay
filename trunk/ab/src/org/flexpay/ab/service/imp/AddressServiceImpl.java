package org.flexpay.ab.service.imp;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Transactional (readOnly = true)
public class AddressServiceImpl implements AddressService {

	private ApartmentService apartmentService;
	private BuildingService buildingService;
	private StreetService streetService;

	/**
	 * Find Apartment address
	 *
	 * @param stub   Apartment stub
	 * @param locale Locale to get address in
	 * @return Apartment address
	 * @throws Exception if failure occurs
	 */
	@NotNull
	public String getAddress(@NotNull Stub<Apartment> stub, @Nullable Locale locale) throws Exception {

		if (locale == null) {
			locale = ApplicationConfig.getDefaultLocale();
		}

		Apartment apartment = apartmentService.readWithPersons(stub);
		if (apartment == null) {
			throw new Exception("Invalid apartment stub: " + stub);
		}

		return getBuildingAddress(apartment.getBuildingStub(), locale) +
			   ", " + apartment.format(locale, true);
	}

	@NotNull
	public String getBuildingAddress(@NotNull Stub<Building> stub, @Nullable Locale locale) throws Exception {

		if (locale == null) {
			locale = ApplicationConfig.getDefaultLocale();
		}

		List<Buildings> buildingses = buildingService.getBuildingBuildings(stub);
		Buildings buildings = buildingService.readFull(stub(buildingses.get(0)));
		if (buildings == null) {
			throw new Exception("No buildingses in building: " + stub);
		}
		Street street = streetService.readFull(buildings.getStreetStub());
		if (street == null) {
			throw new Exception("No street found for building: " + stub);
		}

		return street.format(locale, true) + ", " + buildings.format(locale, true);
	}

	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}
}
