package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.persistence.TownNameTemporal;
import org.flexpay.ab.persistence.TownNameTranslation;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Town service
 */
public interface TownService extends
		ParentService<TownFilter>,
		NameTimeDependentService<TownName, TownNameTemporal, Town, TownNameTranslation> {

	/**
	 * Create or update Town object
	 *
	 * @param town Town object to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void save(@NotNull Town town) throws FlexPayExceptionContainer;
}
