package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.District;
import org.flexpay.common.service.importexport.RawData;

import java.util.Collection;

import static org.flexpay.common.util.CollectionUtils.set;

public class RawDistrictData extends RawData<District> {

	private static final Collection<String> possibleNames = set();
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
	@Override
	public Collection<String> getPossibleNames() {
		return possibleNames;
	}

	public String getName() {
		Object obj = getNameToValuesMap().get(FIELD_NAME);
		return obj == null ? null : obj.toString();
	}

}
