package org.flexpay.common.dao;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.MeasureUnit;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface MeasureUnitDao extends GenericDao<MeasureUnit, Long> {

	/**
	 * Get a list of available measure units
	 *
	 * @return List of Measure units
	 */
	@NotNull
	List<MeasureUnit> listUnits();

	List<MeasureUnit> listUnitsTest(Long id1, Page<?> pager, Collection<Long> ids1, Long id2, Long[] ids2);

	List<MeasureUnit> listUnitsTest(Page<?> pager, Long id1, Long[] ids, Long id2, Collection<Long> ids1);

	List<MeasureUnit> listUnitsRangeTest(FetchRange range);

	List<MeasureUnit> listUnitsRangeTest2(Long lowerId, FetchRange range, Long upperId);

	List<MeasureUnit> findByNameAndLanguage(String name, Long languageId);

}
