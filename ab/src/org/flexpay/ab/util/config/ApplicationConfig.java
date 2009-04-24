package org.flexpay.ab.util.config;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.service.Security;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;

public class ApplicationConfig extends org.flexpay.common.util.config.ApplicationConfig {

	private Country defaultCountry;
	private Region defaultRegion;
	private Town defaultTown;

	private AddressAttributeType buildingHouseType;
	private AddressAttributeType addressAttributeTypeNumber;
	private AddressAttributeType addressAttributeTypeBulk;

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

	public static AddressAttributeType getBuildingHouseType() {
		return getInstance().buildingHouseType;
	}

	/**
	 * TODO: perform lookup by name, not id
	 *
	 * @param townId Town id
	 */
	public void setDefaultTownId(String townId) {
		defaultTown = new Town(Long.valueOf(townId));
	}

	/**
	 * TODO: perform lookup by name, not id
	 *
	 * @param regionId Region id
	 */
	public void setDefaultRegionId(String regionId) {
		defaultRegion = new Region(Long.valueOf(regionId));
	}

	/**
	 * TODO: perform lookup by name, not id
	 *
	 * @param countryId Country id
	 */
	public void setDefaultCountryId(String countryId) {
		defaultCountry = new Country(Long.valueOf(countryId));
	}

	public void setBuildingHouseTypeId(String buildingHouseTypeId) {
		buildingHouseType = new AddressAttributeType(Long.valueOf(buildingHouseTypeId));
	}

	public void setBuildingAttributeTypeNumberId(String numberTypeId) {
		addressAttributeTypeNumber = new AddressAttributeType(Long.valueOf(numberTypeId));
	}

	public void setBuildingAttributeTypeBulkId(String bulkTypeId) {
		addressAttributeTypeBulk = new AddressAttributeType(Long.valueOf(bulkTypeId));
	}

	protected static ApplicationConfig getInstance() {
		return (ApplicationConfig) org.flexpay.common.util.config.ApplicationConfig.getInstance();
	}

}
