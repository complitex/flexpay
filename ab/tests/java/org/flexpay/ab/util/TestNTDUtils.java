package org.flexpay.ab.util;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Map;

public class TestNTDUtils {

	public static Map<Long, String> initNames(String value) {
		Map<Long, String> names = treeMap();
		for (Language lang : ApplicationConfig.getLanguages()) {
			names.put(lang.getId(), value);
		}
		return names;
	}

	public static Country createSimpleCountry(String name) {

		Country country = new Country();
		for (Language lang : ApplicationConfig.getLanguages()) {
			CountryTranslation translation = new CountryTranslation(name, lang);
			translation.setShortName(name.substring(0, 3));
			country.setTranslation(translation);
		}

		return country;

	}

	public static Region createSimpleRegion(String name) {

		Region region = new Region();
		RegionName regionName = new RegionName();
		for (Language lang : ApplicationConfig.getLanguages()) {
			regionName.setTranslation(new RegionNameTranslation(name, lang));
		}
		region.setNameForDate(regionName, DateUtil.now());
		region.setParent(new Country(TestData.COUNTRY_RUS.getId()));

		return region;
	}

	public static Town createSimpleTown(String name) {

		Town town = new Town();
		TownName townName = new TownName();
		for (Language lang : ApplicationConfig.getLanguages()) {
			townName.setTranslation(new TownNameTranslation(name, lang));
		}
		town.setNameForDate(townName, DateUtil.now());
		town.setTypeForDate(new TownType(TestData.TOWN_TYPE_CITY), DateUtil.now());
		town.setParent(new Region(TestData.REGION_NSK.getId()));

		return town;
	}

	public static District createSimpleDistrict(String name) {

		District district = new District();
		DistrictName districtName = new DistrictName();
		for (Language lang : ApplicationConfig.getLanguages()) {
			districtName.setTranslation(new DistrictNameTranslation(name, lang));
		}

		district.setNameForDate(districtName, DateUtil.now());
		district.setParent(new Town(TestData.TOWN_NSK.getId()));

		return district;
	}

	public static Street createSimpleStreet(String name) {

		Street street = new Street();
		StreetName streetName = new StreetName();
		for (Language lang : ApplicationConfig.getLanguages()) {
			streetName.setTranslation(new StreetNameTranslation(name, lang));
		}

		street.setNameForDate(streetName, DateUtil.now());
		street.setTypeForDate(new StreetType(TestData.STR_TYPE_STREET), DateUtil.now());
		street.setParent(new Town(TestData.TOWN_NSK.getId()));

		return street;
	}

}
