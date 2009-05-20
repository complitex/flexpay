package org.flexpay.bti.service;

import org.flexpay.bti.persistence.apartment.ApartmentAttributeGroup;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ApartmentAttributeGroupService {


	/**
	 * Read full group info
	 *
	 * @param stub group stub
	 * @return group if found, or <code>null</code> if not found
	 */
	@Nullable
	ApartmentAttributeGroup readFull(@NotNull Stub<ApartmentAttributeGroup> stub);

	/**
	 * List groups
	 *
	 * @param pager Group pager
	 * @return List of all available
	 */
	List<ApartmentAttributeGroup> listGroups(Page<ApartmentAttributeGroup> pager);

	/**
	 * List groups
	 *
	 * @return List of all available
	 */
	List<ApartmentAttributeGroup> listGroups();

}
