package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.NameTimeDependentService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.annotation.Secured;

import java.util.Collection;
import java.util.List;

/**
 * Region service
 */
public interface RegionService extends NameTimeDependentService<RegionName, RegionNameTemporal, Region, RegionNameTranslation> {

	/**
	 * Read regions collection by theirs ids
	 *
 	 * @param regionIds Region ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found regions
	 */
	@Secured ({Roles.REGION_READ})
	@NotNull
	List<Region> readFull(@NotNull Collection<Long> regionIds, boolean preserveOrder);

	/**
	 * Disable regions
	 *
	 * @param regionIds IDs of regions to disable
	 */
	@Secured (Roles.REGION_DELETE)
	void disable(@NotNull Collection<Long> regionIds);

	/**
	 * Create region
	 *
	 * @param region Region to save
	 * @return Saved instance of region
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.REGION_ADD)
	@NotNull
	Region create(@NotNull Region region) throws FlexPayExceptionContainer;

	/**
	 * Update or create region
	 *
	 * @param region Region to save
	 * @return Saved instance of region
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.REGION_CHANGE)
	@NotNull
	Region update(@NotNull Region region) throws FlexPayExceptionContainer;

	/**
	 * Lookup regions by query and country id.
	 * Query is a string which may contains in folow string:
	 * <p/>
	 * region_name
	 *
	 * @param parentStub  Country stub
	 * @param query searching string
	 * @return List of found regions
	 */
	@Secured (Roles.REGION_READ)
	@NotNull
	List<Region> findByParentAndQuery(@NotNull Stub<Country> parentStub, @NotNull String query);

	/**
	 * Get a list of available regions
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of regions
	 */
	@Secured (Roles.REGION_READ)
	@NotNull
	List<Region> find(@NotNull ArrayStack filters, @NotNull List<? extends ObjectSorter> sorters, @NotNull Page<Region> pager);

}
