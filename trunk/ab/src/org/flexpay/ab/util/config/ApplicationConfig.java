package org.flexpay.ab.util.config;

import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.Security;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class ApplicationConfig {

	private Country defaultCountry;
	private Region defaultRegion;
	private Town defaultTown;

	private AddressAttributeType addressAttributeTypeNumber;
	private AddressAttributeType addressAttributeTypeBulk;
	private AddressAttributeType addressAttributeTypePart;

	private static final ApplicationConfig INSTANCE = new ApplicationConfig();

	static {
		// ensure Security fields are initialised
		Security.touch();
	}

	public static Town getDefaultTown() {
		return getInstance().defaultTown;
	}

	public static Stub<Town> getDefaultTownStub() {
		return stub(getInstance().defaultTown);
	}

	@NotNull
	public static Country getDefaultCountry() {
		return getInstance().defaultCountry;
	}

	public static Stub<Country> getDefaultCountryStub() {
		return stub(getInstance().defaultCountry);
	}

	public static Region getDefaultRegion() {
		return getInstance().defaultRegion;
	}

	public static Stub<Region> getDefaultRegionStub() {
		return stub(getInstance().defaultRegion);
	}

	public static AddressAttributeType getBuildingAttributeTypeNumber() {
		return getInstance().addressAttributeTypeNumber;
	}

	public static AddressAttributeType getBuildingAttributeTypeBulk() {
		return getInstance().addressAttributeTypeBulk;
	}

	public static AddressAttributeType getBuildingAttributeTypePart() {
		return getInstance().addressAttributeTypePart;
	}

    public static ApplicationConfig getInstance() {
        return INSTANCE;
    }

	@Required
	public void setDefaultTownId(Long townId) {
		defaultTown = new Town(townId);
	}

	@Required
	public void setDefaultRegionId(Long regionId) {
		defaultRegion = new Region(regionId);
	}

	@Required
	public void setDefaultCountryId(Long countryId) {
		defaultCountry = new Country(Long.valueOf(countryId));
	}

	@Required
	public void setBuildingAttributeTypeNumberId(Long numberTypeId) {
		addressAttributeTypeNumber = new AddressAttributeType(numberTypeId);
	}

	@Required
	public void setBuildingAttributeTypeBulkId(Long bulkTypeId) {
		addressAttributeTypeBulk = new AddressAttributeType(bulkTypeId);
	}

	@Required
	public void setBuildingAttributeTypePartId(Long partTypeId) {
		addressAttributeTypePart = new AddressAttributeType(partTypeId);
	}

}
