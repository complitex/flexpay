package org.flexpay.ab.service.imp;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
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
import org.springframework.beans.factory.annotation.Required;

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
	public String getBuildingsAddress(@NotNull Stub<BuildingAddress> stub, @Nullable Locale locale) throws Exception {

		if (locale == null) {
			locale = ApplicationConfig.getDefaultLocale();
		}

		BuildingAddress buildingAddress = buildingService.readFull(stub);
		if (buildingAddress == null) {
			throw new Exception("No buildingses in building: " + stub);
		}
		Street street = streetService.readFull(buildingAddress.getStreetStub());
		if (street == null) {
			throw new Exception("No street found for building: " + stub);
		}

		return street.format(locale, true) + ", " + buildingAddress.format(locale, true);
	}

	@NotNull
	public String getBuildingAddress(@NotNull Stub<Building> stub, @Nullable Locale locale) throws Exception {

		List<BuildingAddress> buildingses = buildingService.getBuildingBuildings(stub);
		BuildingAddress candidate = null;
		for (BuildingAddress buildingAddress : buildingses) {
			if (buildingAddress.isPrimary()) {
				candidate = buildingAddress;
			}
		}
		if (candidate == null){
			if (buildingses.size() > 0) {
				candidate = buildingses.get(0);
			} else {
				throw new IllegalStateException("Building does not have any address: " + stub);
			}
		}
		return getBuildingsAddress(stub(candidate), locale);
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

}
