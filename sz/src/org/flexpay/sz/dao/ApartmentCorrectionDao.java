package org.flexpay.sz.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.sz.persistence.corrections.ApartmentNumberCorrection;

public interface ApartmentCorrectionDao extends GenericDao<ApartmentNumberCorrection, Long> {

	/**
	 * Find apartment number correction for particular organisation
	 *
	 * @param osznId	   Organisation id
	 * @param buildingId   Building id
	 * @param externalCode External organisation apartment number code
	 * @return DistrictCorrection
	 */
	ApartmentNumberCorrection findCorrection(Long osznId, Long buildingId, String externalCode);
}
