package org.flexpay.bti.service.impl;

import org.flexpay.bti.dao.ApartmentAttributeGroupDao;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeGroup;
import org.flexpay.bti.service.ApartmentAttributeGroupService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class ApartmentAttributeGroupServiceImpl implements ApartmentAttributeGroupService {

	private ApartmentAttributeGroupDao groupDao;

	/**
	 * Read full group info
	 *
	 * @param stub group stub
	 * @return group if found, or <code>null</code> if not found
	 */
	public ApartmentAttributeGroup readFull(@NotNull Stub<ApartmentAttributeGroup> stub) {
		return groupDao.readFull(stub.getId());
	}

	/**
	 * List groups
	 *
	 * @param pager Group pager
	 * @return List of all available
	 */
	public List<ApartmentAttributeGroup> listGroups(Page<ApartmentAttributeGroup> pager) {
		return groupDao.findGroups(pager);
	}

	/**
	 * List groups
	 *
	 * @return List of all available
	 */
	public List<ApartmentAttributeGroup> listGroups() {
		return groupDao.findAllGroups();
	}

	@Required
	public void setGroupDao(ApartmentAttributeGroupDao groupDao) {
		this.groupDao = groupDao;
	}

}
