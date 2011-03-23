package org.flexpay.samples.ab.importexport.street;

import org.flexpay.ab.persistence.Street;
import org.flexpay.common.service.importexport.RawData;

import java.util.Collection;

import static org.flexpay.common.util.CollectionUtils.set;

public class RawStreetData extends RawData<Street> {

	public static final String FIELD_TYPE = "type";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_ID = "id";

	private static Collection<String> possibleNames = set(FIELD_ID, FIELD_NAME, FIELD_TYPE);

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

	public String getType() {
		Object obj = getNameToValuesMap().get(FIELD_TYPE);
		return obj == null ? null : obj.toString();
	}
}
