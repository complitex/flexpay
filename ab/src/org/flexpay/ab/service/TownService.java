package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.persistence.TownNameTemporal;
import org.flexpay.ab.persistence.TownNameTranslation;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.service.ParentService;

public interface TownService extends
		ParentService<TownNameTranslation, TownFilter>,
		NameTimeDependentService<TownName, TownNameTemporal, Town, TownNameTranslation> {

}
