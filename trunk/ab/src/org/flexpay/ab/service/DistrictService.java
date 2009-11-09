package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.NameTimeDependentService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.Collection;
import java.util.List;

/**
 * District service
 */
public interface DistrictService extends NameTimeDependentService<DistrictName, DistrictNameTemporal, District, DistrictNameTranslation> {

	/**
	 * Read districts collection by theirs ids
	 *
 	 * @param districtIds Districts ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found districts
	 */
	@Secured ({Roles.DISTRICT_READ})
	@NotNull
	List<District> readFull(@NotNull Collection<Long> districtIds, boolean preserveOrder);

	/**
	 * Disable districts
	 *
	 * @param districtIds IDs of districts to disable
	 */
	@Secured (Roles.DISTRICT_DELETE)
	void disable(@NotNull Collection<Long> districtIds);

	/**
	 * Create district
	 *
	 * @param district District to save
	 * @return Saved instance of district
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.DISTRICT_ADD)
	@NotNull
	District create(@NotNull District district) throws FlexPayExceptionContainer;

	/**
	 * Update or create district
	 *
	 * @param district District to save
	 * @return Saved instance of district
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.DISTRICT_CHANGE)
	@NotNull
	District update(@NotNull District district) throws FlexPayExceptionContainer;

	/**
	 * Lookup districts by query and town id.
	 * Query is a string which may contains in folow string:
	 * <p/>
	 * district_name
	 *
	 * @param parentStub  Town stub
	 * @param query searching string
	 * @param language language for search
	 * @return List of found districts
	 */
	@Secured (Roles.DISTRICT_READ)
	@NotNull
	List<District> findByParentAndQuery(@NotNull Stub<Town> parentStub, @NotNull String query, @NotNull Language language);

	/**
	 * Get a list of available districts
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of districts
	 */
	@Secured(Roles.DISTRICT_READ)
	@NotNull
	List<District> find(@NotNull ArrayStack filters, @NotNull List<? extends ObjectSorter> sorters, @NotNull Page<District> pager);

}
