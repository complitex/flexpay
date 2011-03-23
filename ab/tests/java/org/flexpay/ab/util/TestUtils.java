package org.flexpay.ab.util;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.MeasureUnitName;
import org.flexpay.common.util.DateUtil;

import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

public class TestUtils {

	public static Map<Long, String> initNames(String value) {
		Map<Long, String> names = treeMap();
		for (Language lang : getLanguages()) {
			names.put(lang.getId(), value);
		}
		return names;
	}

	public static Country createSimpleCountry(String name) {

		Country country = new Country();
		for (Language lang : getLanguages()) {
			CountryTranslation translation = new CountryTranslation(name, lang);
			translation.setShortName(name.substring(0, 3));
			country.setTranslation(translation);
		}

		return country;

	}

	public static Region createSimpleRegion(String name) {

		Region region = new Region();
		RegionName regionName = new RegionName();
		for (Language lang : getLanguages()) {
			regionName.setTranslation(new RegionNameTranslation(name, lang));
		}
		region.setNameForDate(regionName, DateUtil.now());
		region.setParent(new Country(TestData.COUNTRY_RUS));

		return region;
	}

	public static Town createSimpleTown(String name) {

		Town town = new Town();
		TownName townName = new TownName();
		for (Language lang : getLanguages()) {
			townName.setTranslation(new TownNameTranslation(name, lang));
		}
		town.setNameForDate(townName, DateUtil.now());
		town.setTypeForDate(new TownType(TestData.TOWN_TYPE_CITY), DateUtil.now());
		town.setParent(new Region(TestData.REGION_NSK));

		return town;
	}

	public static District createSimpleDistrict(String name) {

		District district = new District();
		DistrictName districtName = new DistrictName();
		for (Language lang : getLanguages()) {
			districtName.setTranslation(new DistrictNameTranslation(name, lang));
		}

		district.setNameForDate(districtName, DateUtil.now());
		district.setParent(new Town(TestData.TOWN_NSK));

		return district;
	}

	public static Street createSimpleStreet(String name) {

		Street street = new Street();
		StreetName streetName = new StreetName();
		for (Language lang : getLanguages()) {
			streetName.setTranslation(new StreetNameTranslation(name, lang));
		}

		street.setNameForDate(streetName, DateUtil.now());
		street.setTypeForDate(new StreetType(TestData.STR_TYPE_STREET), DateUtil.now());
		street.setParent(new Town(TestData.TOWN_NSK));

		return street;
	}

	public static Building createSimpleBuilding(String buildingNumber) {

		Building building = Building.newInstance();

		building.setDistrict(new District(TestData.DISTRICT_SOVETSKIY));

		BuildingAddress address1 = new BuildingAddress();
		address1.setPrimaryStatus(true);
		address1.setStreet(new Street(TestData.IVANOVA));
		address1.setBuildingAttribute(buildingNumber, org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber());
		address1.setPrimaryStatus(true);

		building.addAddress(address1);

		BuildingAddress address2 = new BuildingAddress();
		address2.setPrimaryStatus(true);
		address2.setStreet(new Street(TestData.DEMAKOVA));
		address2.setBuildingAttribute(buildingNumber + "22", org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber());
		address2.setPrimaryStatus(false);

		building.addAddress(address2);

		return building;
	}

	public static Apartment createSimpleApartment(String apartmentNumber) {

		Apartment apartment = Apartment.newInstance();
		apartment.setNumber(apartmentNumber);
		apartment.setBuilding(new Building(TestData.IVANOVA_27));

		return apartment;
	}

	public static TownType createSimpleTownType(String name) {

		TownType townType = new TownType();
		for (Language lang : getLanguages()) {
			TownTypeTranslation translation = new TownTypeTranslation(name, lang);
			translation.setShortName("srt" + name);
			townType.setTranslation(translation);
		}

		return townType;
	}

	public static StreetType createSimpleStreetType(String name) {

		StreetType streetType = new StreetType();

		try {
			for (Language lang : getLanguages()) {
				streetType.setTranslation(new StreetTypeTranslation(name, "srt" + name, lang));
			}
		} catch (Exception e) {
			// do nothing
		}

		return streetType;
	}

	public static IdentityType createSimpleIdentityType(String name) {

		IdentityType identityType = new IdentityType();
		for (Language lang : getLanguages()) {
			IdentityTypeTranslation translation = new IdentityTypeTranslation(name, lang);
			identityType.setTranslation(translation);
		}

		return identityType;
	}

	public static MeasureUnit createSimpleMeasureUnit(String name) {

		MeasureUnit measureUnit = new MeasureUnit();
		for (Language lang : getLanguages()) {
			MeasureUnitName translation = new MeasureUnitName(name, lang);
			measureUnit.setName(translation);
		}

		return measureUnit;
	}

	public static AddressAttributeType createSimpleAddressAttributeType(String name) {

		AddressAttributeType attributeType = new AddressAttributeType();
		for (Language lang : getLanguages()) {
			AddressAttributeTypeTranslation translation = new AddressAttributeTypeTranslation(name, name + "shrt", lang);
			attributeType.setTranslation(translation);
		}

		return attributeType;
	}

	public static Person createSimplePerson(String name) {

		Person person = new Person();
		PersonIdentity identity = new PersonIdentity();

		identity.setIdentityType(new IdentityType(TestData.IDENTITY_TYPE_FIO));
		identity.setFirstName(name + "first");
		identity.setMiddleName(name + "middle");
		identity.setLastName(name + "last");
		identity.setBirthDate(null);
		identity.setBeginDate(null);
		identity.setEndDate(null);
		identity.setOrganization("");
		identity.setSerialNumber("");
		identity.setDocumentNumber("");
		identity.setDefault(true);

		person.setFIOIdentity(identity);

		return person;
	}

}
