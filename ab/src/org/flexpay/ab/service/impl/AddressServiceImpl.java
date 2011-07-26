package org.flexpay.ab.service.impl;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLocale;

@Transactional (readOnly = true)
public class AddressServiceImpl implements AddressService {

	private ApartmentService apartmentService;
	private BuildingService buildingService;
	private StreetService streetService;

	/**
	 * Get Apartment address
	 *
	 * @param stub Apartment stub
	 * @param locale Locale to get address in
	 * @return Apartment address
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	@Override
	public String getAddress(@NotNull Stub<Apartment> stub, @Nullable Locale locale) throws Exception {

		if (locale == null) {
			locale = getDefaultLocale();
		}

		Apartment apartment = apartmentService.readFull(stub);
		if (apartment == null) {
			throw new Exception("Can't get apartment with id " + stub.getId() + " from DB");
		}

		return getBuildingAddress(apartment.getBuildingStub(), locale) +
			   ", " + apartment.format(locale, true);
	}

	@NotNull
	@Override
	public String getBuildingAddress(@NotNull Stub<Building> stub, @Nullable Locale locale) throws Exception {

		List<BuildingAddress> buildingses = buildingService.findAddresesByBuilding(stub);
		BuildingAddress candidate = null;
		for (BuildingAddress buildingAddress : buildingses) {
			if (buildingAddress.isPrimary()) {
				candidate = buildingAddress;
			}
		}
		if (candidate == null) {
			if (buildingses.isEmpty()) {
				throw new IllegalStateException("Building does not have any address: " + stub);
			} else {
				candidate = buildingses.get(0);
			}
		}
		return getBuildingsAddress(stub(candidate), locale);
	}

	@NotNull
	@Override
	public String getBuildingsAddress(@NotNull Stub<BuildingAddress> stub, @Nullable Locale locale) throws Exception {

		if (locale == null) {
			locale = getDefaultLocale();
		}

		BuildingAddress buildingAddress = buildingService.readFullAddress(stub);
		if (buildingAddress == null) {
			throw new Exception("Can't get building address with id " + stub.getId() + " from DB");
		}
		Street street = streetService.readFull(buildingAddress.getStreetStub());
		if (street == null) {
			throw new Exception("Can't get street with id " + buildingAddress.getStreetStub().getId() + " from DB");
		}

		return street.format(locale, true) + ", " + buildingAddress.format(locale, true);
	}

	/**
	 * Get Building address on street
	 *
	 * @param stub	   Building stub
	 * @param streetStub Street stub to get address on
	 * @param locale	 Locale to get address in
	 * @return Building address
	 * @throws Exception if failure occurs
	 */
	@NotNull
	@Override
	public String getBuildingAddressOnStreet(@NotNull Stub<Building> stub, @NotNull Stub<Street> streetStub,
											 @Nullable Locale locale) throws Exception {
		Building building = buildingService.readFull(stub);
		BuildingAddress address = building.getAddressOnStreet(streetStub);
		if (address == null) {
			throw new IllegalStateException("Building #" + stub.getId() +
											" does not have any address on street #" + streetStub.getId());
		}

		return getBuildingsAddress(stub(address), locale);
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        apartmentService.setJpaTemplate(jpaTemplate);
        buildingService.setJpaTemplate(jpaTemplate);
        streetService.setJpaTemplate(jpaTemplate);
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
