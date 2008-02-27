package org.flexpay.eirc.service.importexport;

import org.flexpay.common.service.importexport.RawData;
import org.flexpay.eirc.persistence.PersonalAccount;

import java.util.Collection;
import java.util.HashSet;

public class RawPersonalAccountData extends RawData<PersonalAccount> {

	private static Collection<String> possibleNames = new HashSet<String>();

	public static final String FIELD_DISTRICT = "district";
	public static final String FIELD_DISTRICT_ID = "districtId";
	public static final String FIELD_STREET = "street";
	public static final String FIELD_STREET_ID = "streetId";
	public static final String FIELD_STREET_TYPE = "streetType";
	public static final String FIELD_BUILDING = "building";
	public static final String FIELD_BUILDING_ID = "buildingId";
	public static final String FIELD_BULK = "bulk";
	public static final String FIELD_APARTMENT = "apartment";
	public static final String FIELD_APARTMENT_ID = "apartmentId";
	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_MIDDLE_NAME = "middleName";
	public static final String FIELD_LAST_NAME = "lastName";
	public static final String FIELD_EXT_ACCOUNT = "account";

	static {
		possibleNames.add("id");
		possibleNames.add(FIELD_DISTRICT);
		possibleNames.add(FIELD_DISTRICT_ID);
		possibleNames.add(FIELD_STREET);
		possibleNames.add(FIELD_STREET_ID);
		possibleNames.add(FIELD_STREET_TYPE);
		possibleNames.add(FIELD_BUILDING);
		possibleNames.add(FIELD_BUILDING_ID);
		possibleNames.add(FIELD_BULK);
		possibleNames.add(FIELD_APARTMENT);
		possibleNames.add(FIELD_APARTMENT_ID);
		possibleNames.add(FIELD_FIRST_NAME);
		possibleNames.add(FIELD_MIDDLE_NAME);
		possibleNames.add(FIELD_LAST_NAME);
		possibleNames.add(FIELD_EXT_ACCOUNT);
	}

	/**
	 * Get set of valid attribute names
	 *
	 * @return Set of attribute names;
	 */
	public Collection<String> getPossibleNames() {
		return possibleNames;
	}

	public String getDistrict() {
		return getParam(FIELD_DISTRICT);
	}

	public String getDistrictId() {
		return getParam(FIELD_DISTRICT_ID);
	}

	public String getStreet() {
		return getParam(FIELD_STREET);
	}

	public String getStreetId() {
		return getParam(FIELD_STREET_ID);
	}

	public String getStreetType() {
		return getParam(FIELD_STREET_TYPE);
	}

	public String getBuilding() {
		return getParam(FIELD_BUILDING);
	}

	public String getBuildingId() {
		return getParam(FIELD_BUILDING_ID);
	}

	public String getBulk() {
		return getParam(FIELD_BULK);
	}

	public String getApartment() {
		return getParam(FIELD_APARTMENT);
	}

	public String getApartmentId() {
		return getParam(FIELD_APARTMENT_ID);
	}

	public String getFirstName() {
		return getParam(FIELD_FIRST_NAME);
	}

	public String getMiddleName() {
		return getParam(FIELD_MIDDLE_NAME);
	}

	public String getLastName() {
		return getParam(FIELD_LAST_NAME);
	}

	public String getExtAccount() {
		return getParam(FIELD_EXT_ACCOUNT);
	}
}
