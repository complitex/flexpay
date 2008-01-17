package org.flexpay.ab.service;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.DistrictNameTemporal;
import org.flexpay.ab.persistence.DistrictNameTranslation;
import org.flexpay.common.service.NameTimeDependentService;

public interface DistrictService extends NameTimeDependentService<
		DistrictName, DistrictNameTemporal, District, DistrictNameTranslation> {

}
