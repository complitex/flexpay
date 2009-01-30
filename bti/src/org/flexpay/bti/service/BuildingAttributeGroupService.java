package org.flexpay.bti.service;

import org.flexpay.bti.persistence.BuildingAttributeGroup;
import org.flexpay.bti.persistence.filters.BuildingAttributeGroupFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface BuildingAttributeGroupService {


	/**
	 * Read full group info
	 *
	 * @param stub group stub
	 * @return group if found, or <code>null</code> if not found
	 */
	@Nullable
	BuildingAttributeGroup readFull(@NotNull Stub<BuildingAttributeGroup> stub);

	/**
	 * List groups
	 *
	 * @param pager Group pager
	 * @return List of all available
	 */
	List<BuildingAttributeGroup> listGroups(Page<BuildingAttributeGroup> pager);

	/**
	 * List groups
	 *
	 * @return List of all available
	 */
	List<BuildingAttributeGroup> listGroups();

	/**
	 * Initialize filter
	 *
	 * @param filter Group filter to init
	 * @return filter back
	 */
	@NotNull
	BuildingAttributeGroupFilter initFilter(@NotNull BuildingAttributeGroupFilter filter);
}
