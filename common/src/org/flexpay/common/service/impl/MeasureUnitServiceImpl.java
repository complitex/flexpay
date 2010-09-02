package org.flexpay.common.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.MeasureUnitDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.MeasureUnitName;
import org.flexpay.common.persistence.Stub;
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

import static org.flexpay.common.persistence.Stub.stub;

@Transactional (readOnly = true)
public class MeasureUnitServiceImpl implements MeasureUnitService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private MeasureUnitDao measureUnitDao;

	private SessionUtils sessionUtils;
	private ModificationListener<MeasureUnit> modificationListener;

	/**
	 * Read measure unit
	 *
	 * @param unitStub MeasureUnit stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public MeasureUnit readFull(@NotNull Stub<? extends MeasureUnit> unitStub) {
		return measureUnitDao.readFull(unitStub.getId());
	}

    //TODO: not implemented
    @NotNull
    @Override
    public List<MeasureUnit> readFull(@NotNull Collection<Long> ids, boolean preserveOrder) {
        return null;
    }

    /**
	 * Disable measure units
	 *
	 * @param unitIds IDs of measure units to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> unitIds) {
		for (Long id : unitIds) {
			if (id == null) {
				log.warn("Null id in collection of measure unit ids for disable");
				continue;
			}
			MeasureUnit unit = measureUnitDao.read(id);
			if (unit == null) {
				log.warn("Can't get measure unit with id {} from DB", id);
				continue;
			}
			unit.disable();
			measureUnitDao.update(unit);

			modificationListener.onDelete(unit);
			log.debug("Measure unit disabled: {}", unit);
		}
	}

	/**
	 * Create measure unit
	 *
	 * @param unit MeasureUnit to save
	 * @return Saved instance of measure unit
	 * @throws FlexPayExceptionContainer if validation fails
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
	 * Update or create measure unit
	 *
	 * @param unit MeasureUnit to save
	 * @return Saved instance of measure unit
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public MeasureUnit update(@NotNull MeasureUnit unit) throws FlexPayExceptionContainer {

		validate(unit);

		MeasureUnit old = readFull(stub(unit));
		if (old == null) {
			throw new FlexPayExceptionContainer(new FlexPayException("No measure unit found to update " + unit));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, unit);

		measureUnitDao.update(unit);

		return unit;
	}

	/**
	 * Validate measure unit before save
	 *
	 * @param unit MeasureUnit object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull MeasureUnit unit) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultLangNameFound = false;

		for (MeasureUnitName translation : unit.getUnitNames()) {

			Language lang = translation.getLang();
			String name = translation.getName();
			boolean nameNotEmpty = StringUtils.isNotEmpty(name);

			if (lang.isDefault()) {
				defaultLangNameFound = nameNotEmpty;
			}

			if (nameNotEmpty) {
				List<MeasureUnit> units = measureUnitDao.findByNameAndLanguage(name, lang.getId());
				if (!units.isEmpty() && !units.get(0).getId().equals(unit.getId())) {
					container.addException(new FlexPayException(
							"Name \"" + name + "\" is already use", "ab.error.name_is_already_use", name));
				}
			}

		}

		if (!defaultLangNameFound) {
			container.addException(new FlexPayException(
					"No default language translation", "common.error.measure_unit.name_is_required"));
		}

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	/**
	 * Get a list of available measure units
	 *
	 * @return List of Measure units
	 */
	@NotNull
	@Override
	public List<MeasureUnit> find() {
		return measureUnitDao.listUnits();
	}

	/**
	 * Initialize filter
	 *
	 * @param measureUnitFilter filter to init
	 * @return Filter back, or a new instance if filter is <code>null</code>
	 */
	@NotNull
	@Override
	public MeasureUnitFilter initFilter(@Nullable MeasureUnitFilter measureUnitFilter) {

		if (measureUnitFilter == null) {
			measureUnitFilter = new MeasureUnitFilter();
		}

		measureUnitFilter.setMeasureUnits(find());

		return measureUnitFilter;
	}

	@Transactional (readOnly = false)
	@Override
	public void delete(@NotNull MeasureUnit unit) {
		measureUnitDao.delete(unit);
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
