package org.flexpay.common.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.NameTimeDependentDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.*;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Base service implementation for objects with time-dependent name
 */
@Transactional (readOnly = true)
public abstract class NameTimeDependentServiceImpl<
		T extends Translation,
		TV extends TemporaryName<TV, T>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>
		> implements NameTimeDependentService<TV, DI, NTD, T> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Get DAO implementation working with Name time-dependent objects
	 *
	 * @return GenericDao implementation
	 */
	protected abstract NameTimeDependentDao<NTD, Long> getNameTimeDependentDao();

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	protected abstract GenericDao<TV, Long> getNameValueDao();

	/**
	 * Read name time-dependent object by its unique id
	 *
	 * @param stub Object stub
	 * @return object, or <code>null</code> if not found
	 */
	@Nullable
	@Override
	public NTD readFull(@NotNull Stub<NTD> stub) {
		return getNameTimeDependentDao().readFull(stub.getId());
	}

	/**
	 * Get temporal names
	 *
	 * @param filter Filter
	 * @return List of names
	 * @throws FlexPayException if failure occurs
	 */
	public List<TV> findNames(PrimaryKeyFilter<?> filter) throws FlexPayException {

		log.debug("Getting list of names: {}", filter);

		List<NTD> ntds = getNameTimeDependentDao().findObjects(
				ObjectWithStatus.STATUS_ACTIVE, filter.getSelectedId());
		List<TV> names = new ArrayList<TV>(ntds.size());

		// Get last temporal in each object names time line
		for (NTD ntd : ntds) {
			LinkedList<DI> temporals = new LinkedList<DI>(ntd.getNameTemporals());
			if (temporals.isEmpty()) {
				log.warn("Object does not have any temporals: {}", ntd);
				continue;
			}
			TV value = temporals.getLast().getValue();
			if (value == null || value.isNew()) {
				log.warn("Incorrect temporal value: {}", value);
				continue;
			}
			names.add(getNameValueDao().readFull(value.getId()));
		}

		return names;
	}

	protected List<T> getTranslations(PrimaryKeyFilter<?> filter, Locale locale) throws FlexPayException {
		List<TV> names = findNames(filter);
		List<T> nameTranslations = new ArrayList<T>(names.size());
		for (TV name : names) {
			nameTranslations.add(
					TranslationUtil.getTranslation(name.getTranslations(), locale));
		}

		return nameTranslations;
	}

	@Override
	public List<NTD> find(ArrayStack filters) {
		PrimaryKeyFilter<?> filter = (PrimaryKeyFilter<?>) filters.peek();
		return getNameTimeDependentDao().findObjects(
				ObjectWithStatus.STATUS_ACTIVE, filter.getSelectedId());
	}

}
