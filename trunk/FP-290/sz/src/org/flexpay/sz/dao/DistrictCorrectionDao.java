package org.flexpay.sz.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.sz.persistence.corrections.DistrictCorrection;

import java.util.List;

public interface DistrictCorrectionDao extends GenericDao<DistrictCorrection, Long> {

	/**
	 * Find district corrections for particular organization
	 * @param id Organization id
	 * @param externalCode External organization district code
	 * @return DistrictCorrection's list
	 */
	List<DistrictCorrection> findCorrection(Long id, String externalCode);
}
