package org.flexpay.ab.util.config;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;

public class ApplicationConfig extends org.flexpay.common.util.config.ApplicationConfig {

	private Country defaultCountry;
	private Region defaultRegion;
	private Town defaultTown;

	private BuildingAttributeType buildingAttributeTypeNumber;
	private BuildingAttributeType buildingAttributeTypeBulk;

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

	public static Region getDefaultRegion() {
		return getInstance().defaultRegion;
	}

	public static BuildingAttributeType getBuildingAttributeTypeNumber() {
		return getInstance().buildingAttributeTypeNumber;
	}

	public static BuildingAttributeType getBuildingAttributeTypeBulk() {
		return getInstance().buildingAttributeTypeBulk;
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

	public void setBuildingAttributeTypeNumberId(String numberTypeId) {
		buildingAttributeTypeNumber = new BuildingAttributeType(Long.valueOf(numberTypeId));
	}

	public void setBuildingAttributeTypeBulkId(String bulkTypeId) {
		buildingAttributeTypeBulk = new BuildingAttributeType(Long.valueOf(bulkTypeId));
	}

	protected static ApplicationConfig getInstance() {
		return (ApplicationConfig) org.flexpay.common.util.config.ApplicationConfig.getInstance();
	}
}
