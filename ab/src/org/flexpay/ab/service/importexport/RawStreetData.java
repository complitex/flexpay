package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Street;
import org.flexpay.common.service.importexport.RawData;

import java.util.Collection;
import java.util.HashSet;

public class RawStreetData extends RawData<Street> {

	private static Collection<String> possibleNames = new HashSet<String>();

	public static final String FIELD_TYPE_ID = "type";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_ID = "id";

	static {
		possibleNames.add(FIELD_ID);
		possibleNames.add(FIELD_NAME);
		possibleNames.add(FIELD_TYPE_ID);
	}

	/**
	 * Get set of valid attribute names
	 *
	 * @return Set of attribute names;
	 */
	public Collection<String> getPossibleNames() {
		return possibleNames;
	}

	public String getName() {
		Object obj = getNameToValuesMap().get(FIELD_NAME);
		return obj == null ? null : obj.toString();
	}

	public String getTypeId() {
		Object obj = getNameToValuesMap().get(FIELD_TYPE_ID);
		return obj == null ? null : obj.toString();
	}
}