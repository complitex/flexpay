package org.flexpay.sz.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.sz.persistence.corrections.DistrictCorrection;

public interface DistrictCorrectionDao extends GenericDao<DistrictCorrection, Long> {

	/**
	 * Find district correction for particular organisation
	 * @param id Organisation id
	 * @param externalCode External organisation district code
	 * @return DistrictCorrection
	 */
	DistrictCorrection findCorrection(Long id, String externalCode);
}
