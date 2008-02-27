package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.service.importexport.RawData;

import java.util.Collection;
import java.util.HashSet;

public class RawApartmentData extends RawData<Apartment> {

	private static Collection<String> possibleNames = new HashSet<String>();

	public static final String FIELD_NUMBER = "apartmentNumber";
	public static final String FIELD_BUILDING = "buildingId";
	public static final String FIELD_ID = "id";

	static {
		possibleNames.add(FIELD_ID);
		possibleNames.add(FIELD_NUMBER);
		possibleNames.add(FIELD_BUILDING);
	}

	/**
	 * Get set of valid attribute names
	 *
	 * @return Set of attribute names;
	 */
	public Collection<String> getPossibleNames() {
		return possibleNames;
	}

	public String getNumber() {
		return getParam(FIELD_NUMBER);
	}

	public String getBuildingId() {
		return getParam(FIELD_BUILDING);
	}
}
