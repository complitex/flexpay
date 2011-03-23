package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.common.service.importexport.RawData;

import java.util.Collection;

import static org.flexpay.common.util.CollectionUtils.set;

public class RawBuildingsData extends RawData<BuildingAddress> {

	private static final Collection<String> possibleNames = set();

	public static final String FIELD_STREET = "streetId";
	public static final String FIELD_DISTRICT = "districtId";
	public static final String FIELD_NUMBER = "buildingNumber";
	public static final String FIELD_NUMBER_BULK = "buildingBulkNumber";
	public static final String FIELD_ID = "id";

	static {
		possibleNames.add(FIELD_ID);
		possibleNames.add(FIELD_NUMBER);
		possibleNames.add(FIELD_NUMBER_BULK);
		possibleNames.add(FIELD_STREET);
		possibleNames.add(FIELD_DISTRICT);
	}

	/**
	 * Get set of valid attribute names
	 *
	 * @return Set of attribute names;
	 */
	@Override
	public Collection<String> getPossibleNames() {
		return possibleNames;
	}

	public String getStreetId() {
		Object obj = getNameToValuesMap().get(FIELD_STREET);
		return obj == null ? null : obj.toString();
	}

	public String getDistrictId() {
		Object obj = getNameToValuesMap().get(FIELD_DISTRICT);
		return obj == null ? null : obj.toString();
	}

	public String getNumber() {
		Object obj = getNameToValuesMap().get(FIELD_NUMBER);
		return obj == null ? null : obj.toString();
	}

	public String getBulkNumber() {
		Object obj = getNameToValuesMap().get(FIELD_NUMBER_BULK);
		return obj == null ? null : obj.toString();
	}

}
