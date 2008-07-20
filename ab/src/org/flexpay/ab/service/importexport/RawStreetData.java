package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Street;
import org.flexpay.common.service.importexport.RawData;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NonNls;

import java.util.Collection;

public class RawStreetData extends RawData<Street> {

	@NonNls
	public static final String FIELD_TYPE_ID = "type";
	@NonNls
	public static final String FIELD_NAME = "name";
	@NonNls
	public static final String FIELD_ID = "id";

	private static Collection<String> possibleNames = set(FIELD_ID, FIELD_NAME, FIELD_TYPE_ID);

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