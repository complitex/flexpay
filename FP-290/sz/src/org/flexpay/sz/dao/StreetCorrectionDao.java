package org.flexpay.sz.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.sz.persistence.corrections.StreetCorrection;

public interface StreetCorrectionDao extends GenericDao<StreetCorrection, Long> {

	/**
	 * Find street correction for particular organization
	 *
	 * @param osznId	   Organization id
	 * @param districtId   District id
	 * @param externalCode External organization street code
	 * @return DistrictCorrection
	 */
	StreetCorrection findCorrection(Long osznId, Long districtId, String externalCode);
}
