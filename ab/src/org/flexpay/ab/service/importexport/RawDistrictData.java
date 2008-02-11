package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.District;
import org.flexpay.common.service.importexport.RawData;

import java.util.Collection;
import java.util.HashSet;

public class RawDistrictData extends RawData<District> {

	private static Collection<String> possibleNames = new HashSet<String>();
	public static final String FIELD_NAME = "name";

	static {
		possibleNames.add("id");
		possibleNames.add(FIELD_NAME);
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
}
