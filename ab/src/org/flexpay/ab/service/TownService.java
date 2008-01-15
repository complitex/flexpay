package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.persistence.TownNameTemporal;
import org.flexpay.ab.persistence.TownNameTranslation;
import org.flexpay.common.service.NameTimeDependentService;

public interface TownService extends NameTimeDependentService<
		TownName, TownNameTemporal, Town, TownNameTranslation> {

}
