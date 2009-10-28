package org.flexpay.common.dao;

import org.flexpay.common.dao.paging.Page;

import java.io.Serializable;
import java.util.List;

public interface NameTimeDependentDao<NTD, PK extends Serializable>
		extends GenericDao<NTD, PK> {

	/**
	 * Get page of children for parent
	 *
	 * @param pager	Number of children filter
	 * @param status   Object status to retrive
	 * @param parentId Parent id
	 * @return List of children
	 */
	List<NTD> findObjects(Page<NTD> pager, int status, PK parentId);

	/**
	 * Get all children for parent
	 *
	 * @param status   Object status to retrive
	 * @param parentId Parent id
	 * @return List of children
	 */
	List<NTD> findObjects(int status, PK parentId);
}
