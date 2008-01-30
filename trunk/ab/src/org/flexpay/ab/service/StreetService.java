package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetName;
import org.flexpay.ab.persistence.StreetNameTemporal;
import org.flexpay.ab.persistence.StreetNameTranslation;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.service.ParentService;

import java.util.Set;

public interface StreetService extends NameTimeDependentService<
		StreetName, StreetNameTemporal, Street, StreetNameTranslation>,
		ParentService<StreetFilter> {

	/**
	 * Save street districts
	 *
	 * @param street Street to save districts for
	 * @param objectIds List of district ids
	 * @return saved street object
	 */
	Street saveDistricts(Street street, Set<Long> objectIds);
}
