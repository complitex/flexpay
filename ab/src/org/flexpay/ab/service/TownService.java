package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.NameTimeDependentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

import java.util.Collection;
import java.util.List;

/**
 * Town service
 */
public interface TownService extends NameTimeDependentService<TownName, TownNameTemporal, Town, TownNameTranslation> {

	/**
	 * Read towns collection by theirs ids
	 *
 	 * @param townIds Town ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found towns
	 */
	@Secured ({Roles.TOWN_READ})
	@NotNull
	List<Town> readFull(@NotNull Collection<Long> townIds, boolean preserveOrder);

	/**
	 * Disable towns
	 *
	 * @param townIds IDs of towns to disable
	 */
	@Secured (Roles.TOWN_DELETE)
	void disable(@NotNull Collection<Long> townIds);

	/**
	 * Create town
	 *
	 * @param town Town to save
	 * @return Saved instance of town
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.TOWN_ADD)
	@NotNull
	Town create(@NotNull Town town) throws FlexPayExceptionContainer;

	/**
	 * Update or create town
	 *
	 * @param town Town to save
	 * @return Saved instance of town
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.TOWN_CHANGE)
	@NotNull
	Town update(@NotNull Town town) throws FlexPayExceptionContainer;

	/**
	 * Read town with its full hierarchical structure:
	 * country-region
	 *
	 * @param townStub Town stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured ({Roles.TOWN_READ})
	@Nullable
	Town readWithHierarchy(@NotNull Stub<Town> townStub);

	/**
	 * Lookup towns by query and region id.
	 * Query is a string which may contains in folow string:
	 * <p/>
	 * town_name
	 *
	 * @param parentStub  Region stub
	 * @param query searching string
	 * @return List of found regions
	 */
	@Secured (Roles.TOWN_READ)
	@NotNull
	List<Town> findByParentAndQuery(@NotNull Stub<Region> parentStub, @NotNull String query);

	/**
	 * Get a list of available towns
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of towns
	 */
	@Secured (Roles.TOWN_READ)
	@NotNull
	List<Town> find(@NotNull ArrayStack filters, @NotNull List<? extends ObjectSorter> sorters, @NotNull Page<Town> pager);

}
