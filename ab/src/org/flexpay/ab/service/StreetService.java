package org.flexpay.ab.service;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.persistence.Pair;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public interface StreetService
		extends
		NameTimeDependentService<StreetName, StreetNameTemporal, Street, StreetNameTranslation>,
		ParentService<StreetFilter> {

	/**
	 * Save street districts
	 * 
	 * @param street
	 *            Street to save districts for
	 * @param objectIds
	 *            List of district ids
	 * @return saved street object
	 */
	Street saveDistricts(Street street, Set<Long> objectIds);

	List<Street> findByTownAndName(@NotNull Stub<Town> stub, @NotNull String name);

	/**
	 * Save Street types timeline
	 * 
	 * @param object
	 *            Street to update
	 */
	void saveTypes(Street object);

	String format(@NotNull Stub<Street> stub, @NotNull Locale locale, boolean shortMode)
			throws FlexPayException;

	/**
	 * Get street and name pair
	 *
	 * @param stub Street stub
	 * @return Street and street name pair
	 */
	Pair<Street, String> getFullStreetName(Stub<Street> stub);
}
