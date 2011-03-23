package org.flexpay.bti.service;

import org.flexpay.bti.persistence.building.BuildingAttributeGroup;
import org.flexpay.bti.persistence.filters.BuildingAttributeGroupFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
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
	 * Read full group info with ids in collection of ids
	 *
	 * @param ids collection group ids wich want to find
	 * @return list of groups
	 */
	List<BuildingAttributeGroup> readFullGroups(@NotNull Collection<Long> ids);

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

	/**
	 * Create attribute group
	 *
	 * @param group Attribute group to persist
	 * @return Saved group back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	BuildingAttributeGroup create(@NotNull BuildingAttributeGroup group) throws FlexPayExceptionContainer;

	/**
	 * Update attribute group
	 *
	 * @param group Attribute group to persist
	 * @return Saved group back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	BuildingAttributeGroup update(@NotNull BuildingAttributeGroup group) throws FlexPayExceptionContainer;

	/**
	 * Disable attribute group
	 *
	 * @param ids Attribute group keys to disable
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void disable(@NotNull Collection<Long> ids) throws FlexPayExceptionContainer;

}
