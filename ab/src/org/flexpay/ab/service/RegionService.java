package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTemporal;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.service.ParentService;

public interface RegionService extends
		ParentService<RegionFilter>,
		NameTimeDependentService<RegionName, RegionNameTemporal, Region, RegionNameTranslation> {
	
	Region readFull(Long id);

}
