package org.flexpay.common.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.MeasureUnitDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.MeasureUnitName;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.MeasureUnitFilter;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.common.service.internal.SessionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional (readOnly = true)
public class MeasureUnitServiceImpl implements MeasureUnitService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private MeasureUnitDao measureUnitDao;

	private SessionUtils sessionUtils;
	private ModificationListener<MeasureUnit> modificationListener;

	/**
	 * Read full unit info
	 *
	 * @param stub MeasureUnit stub to read
	 * @return MeasureUnit if available
	 */
	@Nullable
	public MeasureUnit readFull(@NotNull Stub<? extends MeasureUnit> stub) {
		return measureUnitDao.readFull(stub.getId());
	}

	/**
	 * Get a list of available measure units
	 *
	 * @return List of Measure units
	 */
	@NotNull
	@Override
	public List<MeasureUnit> listUnits() {
		return measureUnitDao.listUnits();
	}

	@NotNull
	@Override
	public MeasureUnit newInstance() {
		return new MeasureUnit();
	}

	@Override
	public Class<? extends MeasureUnit> getType() {
		return MeasureUnit.class;
	}

	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> ids) {

		log.debug("Disabling service types");
		for (Long id : ids) {
			MeasureUnit unit = measureUnitDao.read(id);
			if (unit != null) {
				unit.disable();
				measureUnitDao.update(unit);

				modificationListener.onDelete(unit);
				log.debug("Disabled unit: {}", unit);
			}
		}
	}

	/**
	 * Initialize filter
	 *
	 * @param filter MeasureUnitFilter to init
	 * @return Filter back, or a new instance if filter is <code>null</code>
	 */
	@NotNull
	@Override
	public MeasureUnitFilter initFilter(@Nullable MeasureUnitFilter filter) {

		if (filter == null) {
			filter = new MeasureUnitFilter();
		}

		filter.setMeasureUnits(listUnits());

		return filter;
	}

	/**
	 * Create or update measure unit
	 *
	 * @param unit MeasureUnit to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public MeasureUnit create(@NotNull MeasureUnit unit) throws FlexPayExceptionContainer {

		validate(unit);
		unit.setId(null);
		measureUnitDao.create(unit);

		modificationListener.onCreate(unit);

		return unit;
	}

	/**
	 * Update measure unit
	 *
	 * @param obj Unit to update
	 * @return updatyed object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@NotNull
	@Override
	@Transactional (readOnly = false)
	public MeasureUnit update(@NotNull MeasureUnit obj) throws FlexPayExceptionContainer {

		validate(obj);

		MeasureUnit old = readFull(stub(obj));
		if (old == null) {
			throw new FlexPayExceptionContainer(new FlexPayException("No object found to update " + obj));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, obj);

		measureUnitDao.update(obj);

		return obj;
	}

	@Transactional (readOnly = false)
	@Override
	public void delete(@NotNull MeasureUnit unit) {
		measureUnitDao.delete(unit);
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull MeasureUnit unit) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		boolean defaultNameFound = false;
		for (MeasureUnitName name : unit.getUnitNames()) {
			if (name.getLang().isDefault() && StringUtils.isNotBlank(name.getName())) {
				defaultNameFound = true;
			}
		}
		if (!defaultNameFound) {
			ex.addException(new FlexPayException(
					"No default lang name", "common.error.no_default_lang_name"));
		}

		if (ex.isNotEmpty()) {
			throw ex;
		}
	}

	@Required
	public void setMeasureUnitDao(MeasureUnitDao measureUnitDao) {
		this.measureUnitDao = measureUnitDao;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<MeasureUnit> modificationListener) {
		this.modificationListener = modificationListener;
	}

}
