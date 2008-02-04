package org.flexpay.sz.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.sz.persistence.corrections.DistrictCorrection;

import java.util.List;

public interface DistrictCorrectionDao extends GenericDao<DistrictCorrection, Long> {

	/**
	 * Find district corrections for particular organisation
	 * @param id Organisation id
	 * @param externalCode External organisation district code
	 * @return DistrictCorrection's list
	 */
	List<DistrictCorrection> findCorrection(Long id, String externalCode);
}
