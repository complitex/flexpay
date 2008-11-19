package org.flexpay.ab.service;

import java.util.List;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.DistrictNameTemporal;
import org.flexpay.ab.persistence.DistrictNameTranslation;
import org.flexpay.ab.persistence.filters.DistrictFilter;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.jetbrains.annotations.NotNull;

public interface DistrictService extends NameTimeDependentService<
        DistrictName, DistrictNameTemporal, District, DistrictNameTranslation>,
        ParentService<DistrictFilter> {

	/**
	 * Create or update district
	 *
	 * @param object District to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void save(@NotNull District object) throws FlexPayExceptionContainer;
}
