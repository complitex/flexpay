package org.flexpay.bti.service.impl;

import org.flexpay.bti.dao.BuildingAttributeGroupDao;
import org.flexpay.bti.persistence.BuildingAttributeGroup;
import org.flexpay.bti.persistence.filters.BuildingAttributeGroupFilter;
import org.flexpay.bti.service.BuildingAttributeGroupService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class BuildingAttributeGroupServiceImpl implements BuildingAttributeGroupService {

	private BuildingAttributeGroupDao groupDao;

	/**
	 * Read full group info
	 *
	 * @param stub group stub
	 * @return group if found, or <code>null</code> if not found
	 */
	public BuildingAttributeGroup readFull(@NotNull Stub<BuildingAttributeGroup> stub) {
		return groupDao.readFull(stub.getId());
	}

	/**
	 * List groups
	 *
	 * @param pager Group pager
	 * @return List of all available
	 */
	public List<BuildingAttributeGroup> listGroups(Page<BuildingAttributeGroup> pager) {
		return groupDao.findGroups(pager);
	}

	/**
	 * List groups
	 *
	 * @return List of all available
	 */
	public List<BuildingAttributeGroup> listGroups() {
		return groupDao.findAllGroups();
	}

	/**
	 * Initialize filter
	 *
	 * @param filter Group filter to init
	 * @return filter back
	 */
	@NotNull
	public BuildingAttributeGroupFilter initFilter(@NotNull BuildingAttributeGroupFilter filter) {
		filter.setGroups(listGroups());
		return filter;
	}

	@Required
	public void setGroupDao(BuildingAttributeGroupDao groupDao) {
		this.groupDao = groupDao;
	}
}
