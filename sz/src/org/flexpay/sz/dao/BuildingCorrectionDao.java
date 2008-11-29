package org.flexpay.sz.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.sz.persistence.corrections.BuildingNumberCorrection;

public interface BuildingCorrectionDao extends GenericDao<BuildingNumberCorrection, Long> {

	/**
	 * Find building correction for particular organization
	 *
	 * @param osznId	   Organization id
	 * @param streetId	 Street id
	 * @param externalCode External organization building number code
	 * @param bulk		 Optional building bulk
	 * @return DistrictCorrection
	 */
	BuildingNumberCorrection findCorrection(Long osznId, Long streetId, String externalCode, String bulk);
}
