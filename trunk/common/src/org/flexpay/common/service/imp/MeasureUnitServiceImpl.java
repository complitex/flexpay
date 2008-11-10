package org.flexpay.common.service.imp;

import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.MeasureUnitFilter;
import org.flexpay.common.dao.MeasureUnitDao;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class MeasureUnitServiceImpl implements MeasureUnitService {

	private MeasureUnitDao measureUnitDao;

	/**
	 * Read full unit info
	 *
	 * @param stub MeasureUnit stub to read
	 * @return MeasureUnit if available
	 */
	@Nullable
	public MeasureUnit read(@NotNull Stub<MeasureUnit> stub) {
		return measureUnitDao.readFull(stub.getId());
	}

	/**
	 * Get a list of available measure units
	 *
	 * @return List of Measure units
	 */
	@NotNull
	public List<MeasureUnit> listUnits() {
		return measureUnitDao.listUnits();
	}

	/**
	 * Initialize filter
	 *
	 * @param filter MeasureUnitFilter to init
	 * @return Filter back, or a new instance if filter is <code>null</code>
	 */
	@NotNull
	public MeasureUnitFilter initFilter(@Nullable MeasureUnitFilter filter) {

		if (filter == null) {
			filter = new MeasureUnitFilter();
		}

		filter.setMeasureUnits(listUnits());

		return filter;
	}

	public void setMeasureUnitDao(MeasureUnitDao measureUnitDao) {
		this.measureUnitDao = measureUnitDao;
	}
}
