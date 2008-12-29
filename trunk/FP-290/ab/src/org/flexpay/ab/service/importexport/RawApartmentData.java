package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.service.importexport.RawData;
import org.jetbrains.annotations.NonNls;

import java.util.Collection;
import java.util.HashSet;

public class RawApartmentData extends RawData<Apartment> {

	private static final Collection<String> possibleNames = new HashSet<String>();

	@NonNls
	public static final String FIELD_NUMBER = "apartmentNumber";
	@NonNls
	public static final String FIELD_BUILDING = "buildingId";
	@NonNls
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
