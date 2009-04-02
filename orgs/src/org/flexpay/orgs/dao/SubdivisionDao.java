package org.flexpay.orgs.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.orgs.persistence.Subdivision;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SubdivisionDao extends GenericDao<Subdivision, Long> {

	/**
	 * Find subdivisions
	 *
	 * @param headId	  Head organization key
	 * @param juridicalId Juridical person key
	 * @return List of subdivisions
	 */
	@NotNull
	List<Subdivision> findSubdivisions(@NotNull Long headId, @NotNull Long juridicalId);
}
