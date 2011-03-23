package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.common.service.importexport.RawData;

import java.util.Collection;

import static org.flexpay.common.util.CollectionUtils.set;

public class RawStreetTypeData extends RawData<StreetType> {

	public static final String FIELD_NAME = "name";
	public static final String FIELD_ID = "id";

	private static final Collection<String> possibleNames = set(FIELD_ID, FIELD_NAME);

	/**
	 * Get set of valid attribute names
	 *
	 * @return Set of attribute names;
	 */
	@Override
	public Collection<String> getPossibleNames() {
		return possibleNames;
	}

	public String getName() {
		Object obj = getNameToValuesMap().get(FIELD_NAME);
		return obj == null ? null : obj.toString();
	}

}
