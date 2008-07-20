package org.flexpay.common.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.NameTimeDependentDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.*;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.TranslationUtil;
import org.springframework.transaction.annotation.Transactional;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Base service implementation for objects with time-dependent name
 */
@Transactional (readOnly = true, rollbackFor = Exception.class)
public abstract class NameTimeDependentServiceImpl<
		T extends Translation,
		TV extends TemporaryName<TV, T>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		Parent extends DomainObject
		> implements NameTimeDependentService<TV, DI, NTD, T> {

	protected Logger log = Logger.getLogger(getClass());

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
	protected abstract GenericDao<DI, Long> getNameTemporalDao();

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	protected abstract GenericDao<TV, Long> getNameValueDao();

	/**
	 * Get DAO implementation working with name translations
	 *
	 * @return GenericDao implementation
	 */
	protected abstract GenericDao<T, Long> getNameTranslationDao();

	/**
	 * Get DAO implementation working with parent objects
	 *
	 * @return GenericDao implementation
	 */
	protected abstract GenericDao<Parent, Long> getParentDao();

	/**
	 * Getter for property 'newNameTemporal'.
	 *
	 * @return Value for property 'newNameTemporal'.
	 */
	protected abstract DI getNewNameTemporal();

	/**
	 * Getter for property 'newNameTimeDependent'.
	 *
	 * @return Value for property 'newNameTimeDependent'.
	 */
	protected abstract NTD getNewNameTimeDependent();

	/**
	 * Getter for property 'emptyName'.
	 *
	 * @return Value for property 'emptyName'.
	 */
	protected abstract TV getEmptyName();

	/**
	 * Check if disable operation on object is allowed
	 *
	 * @param ntd	   Name time dependent object
	 * @param container Exceptions container to add exception for
	 * @return <code>true</code> if operation allowed, or <code>false</otherwise>
	 */
	protected abstract boolean canDisable(NTD ntd, FlexPayExceptionContainer container);

	/**
	 * return base for name time-dependent objects in i18n files, like 'region', 'town',
	 * etc.
	 *
	 * @return Localization key base
	 */
	protected abstract String getI18nKeyBase();

	/**
	 * Read name time-dependent object by its unique id
	 *
	 * @param id key
	 * @return object, or <code>null</code> if not found
	 */
	public NTD read(Long id) {
		return getNameTimeDependentDao().readFull(id);
	}

	/**
	 * Disable NTD
	 *
	 * @param objects NTDs to disable
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	@Transactional (readOnly = false)
	public void disable(Collection<NTD> objects) throws FlexPayExceptionContainer {

		if (log.isInfoEnabled()) {
			log.info(objects.size() + " objects to disable");
		}
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		for (NTD ntd : objects) {
			if (!canDisable(ntd, container)) {
				continue;
			}
			NTD ntdDB = getNameTimeDependentDao().read(ntd.getId());
			ntdDB.setStatus(DomainObjectWithStatus.STATUS_DISABLED);
			getNameTimeDependentDao().update(ntdDB);

			if (log.isDebugEnabled()) {
				log.debug("Disabled: " + ntdDB);
			}
		}

		if (!container.getExceptions().isEmpty()) {
			throw container;
		}
	}

	/**
	 * Read temporal object name by its unique id
	 *
	 * @param id key
	 * @return temporal object name, or <code>null</code> if object not found
	 */
	public DI readTemporalName(Long id) {
		return getNameTemporalDao().readFull(id);
	}

	/**
	 * Get temporal names
	 *
	 * @param filters Filters
	 * @param pager   Objects list pager
	 * @return List of names
	 * @throws FlexPayException if failure occurs
	 */
	public List<TV> findNames(ArrayStack filters, Page pager)
			throws FlexPayException {

		if (log.isInfoEnabled()) {
			log.info("Getting list of names: " + filters);
		}

		PrimaryKeyFilter filter = (PrimaryKeyFilter) filters.peek();

		List<NTD> ntds = getNameTimeDependentDao().findObjects(pager,
				ObjectWithStatus.STATUS_ACTIVE, filter.getSelectedId());
		List<TV> names = new ArrayList<TV>(ntds.size());

		// Get last temporal in each object names time line
		for (NTD ntd : ntds) {
			Collection<DI> temporals = ntd.getNameTemporals();
			LinkedList<DI> wrapper = new LinkedList<DI>(temporals);
			if (wrapper.isEmpty()) {
				log.info("Found NTD, but no temporals: " + ntd);
			} else {
				DI temporal = wrapper.getLast();
				names.add(getNameValueDao().readFull(temporal.getValue().getId()));
			}
		}

		return names;
	}

	/**
	 * Get temporal names
	 *
	 * @param filter Filter
	 * @return List of names
	 * @throws FlexPayException if failure occurs
	 */
	protected List<TV> findNames(PrimaryKeyFilter filter)
			throws FlexPayException {

		if (log.isDebugEnabled()) {
			log.debug("Getting list of names: " + filter);
		}

		List<NTD> ntds = getNameTimeDependentDao().findObjects(
				ObjectWithStatus.STATUS_ACTIVE, filter.getSelectedId());
		List<TV> names = new ArrayList<TV>(ntds.size());

		// Get last temporal in each object names time line
		for (NTD ntd : ntds) {
			LinkedList<DI> temporals = new LinkedList<DI>(ntd.getNameTemporals());
			if (temporals.isEmpty()) {
				log.warn("Object does not have any temporals: " + ntd);
				continue;
			}
			DI temporal = temporals.getLast();
			names.add(getNameValueDao().readFull(temporal.getValue().getId()));
		}

		return names;
	}

	protected List<T> getTranslations(PrimaryKeyFilter filter, Locale locale) throws FlexPayException {
		List<TV> names = findNames(filter);
		List<T> nameTranslations = new ArrayList<T>(names.size());
		for (TV name : names) {
			nameTranslations.add(
					TranslationUtil.getTranslation(name.getTranslations(), locale));
		}

		return nameTranslations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public NTD create(NTD object, List<T> nameTranslations, ArrayStack filters, Date date)
			throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();
		DomainObject parent = getParent(filters, container);
		Set<T> names = getTranslations(nameTranslations, container);
		if (container.isNotEmpty()) {
			throw container;
		}

		NTD namable = object == null ? getNewNameTimeDependent() : object;
		namable.setStatus(ObjectWithStatus.STATUS_ACTIVE);
		namable.setParent(parent);

		DI nameTemporal = getNewNameTemporal();
		nameTemporal.setObject(namable);
		nameTemporal.setBegin(date);
		TimeLine<TV, DI> tl = new TimeLine<TV, DI>(nameTemporal);
		namable.setNamesTimeLine(tl);

		namable = postCreate(namable);

		TV objectName = getEmptyName();
		objectName.setTranslations(names);
		objectName.setObject(namable);

		for (T translation : objectName.getTranslations()) {
			translation.setTranslatable(objectName);
		}

		nameTemporal.setValue(objectName);
		for (DI temporal : namable.getNamesTimeLine().getIntervals()) {
			TV empty = getEmptyName();
			if (temporal.getValue().equals(empty)) {
				temporal.setValue(null);
			}
		}

		getNameTimeDependentDao().create(namable);

		return namable;
	}

	/**
	 * Run any post create actions on object
	 *
	 * @param object Persisted object
	 * @return The object itself
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	public NTD postCreate(NTD object) throws FlexPayExceptionContainer {
		return object;
	}

	/**
	 * Extract parent from filter data
	 *
	 * @param filters   PrimaryKeyFilter parent filters
	 * @param container Exception container
	 * @return Country if found or <code>null</code> otherwise
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private DomainObject getParent(
			ArrayStack filters, FlexPayExceptionContainer container) {

		PrimaryKeyFilter filter = (PrimaryKeyFilter) filters.peek();
		if (filter.getSelectedId() == null) {
			container.addException(new FlexPayException("null",
					getI18nKeyBase() + ".no_parent_selectected"));
			return null;
		}

		GenericDao<Parent, Long> parentDao = getParentDao();

		DomainObject domainObject = parentDao.read(filter.getSelectedId());
		if (domainObject == null) {
			container.addException(new FlexPayException("null",
					getI18nKeyBase() + ".parent_id_invalid"));
			log.info("Failed getting parent: filter:" +
					 filter.getClass().getName() + "[id = " + filter.getSelectedId() + "]");
			return null;
		}
		return domainObject;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<Long, T> getTranslations(Long temporalId) {
		DI temporal = getNameTemporalDao().readFull(temporalId);
		if (log.isInfoEnabled()) {
			log.info("Temporal: " + temporal);
		}

		if (temporal == null || temporal.getValue() == null) {
			return Collections.emptyMap();
		}

		TV name = temporal.getValue();
		if (log.isInfoEnabled()) {
			log.info("Translations: " + name.getTranslations());
		}

		Map<Long, T> map = new HashMap<Long, T>();
		for (T translation : name.getTranslations()) {
			map.put(translation.getLang().getId(), translation);
		}

		return map;
	}

	/**
	 * Save object name translations
	 *
	 * @param object		   object to update
	 * @param temporalId	   Temporal id to apply changes for
	 * @param nameTranslations New translations
	 * @param date			 Date from which the name is valid
	 * @return updated object instance
	 * @throws FlexPayExceptionContainer exceptions container
	 */
	@Transactional (readOnly = false)
	public NTD updateNameTranslations(NTD object, Long temporalId, List<T> nameTranslations,
									  Date date) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();
		Set<T> names = getTranslations(nameTranslations, container);
		if (container.isNotEmpty()) {
			throw container;
		}
		DI temporal = getNameTemporalDao().readFull(temporalId);
		TV objectName = getEmptyName();
		objectName.setTranslations(names);
		objectName.setObject(object);
		// no changes made, return object
		if (temporal != null
			&& temporal.getBegin().equals(date)
			&& objectName.equals(temporal.getValue())) {
			log.info("No changes made for object name temporal");
			return object;
		}

		// no changes made to translation
		if (temporal != null && objectName.equals(temporal.getValue())) {
			objectName = temporal.getValue();
		} else {
			for (T translation : names) {
				translation.setTranslatable(objectName);
				translation.setId(null);
			}
			getNameValueDao().create(objectName);
		}

		if (temporal == null) {
			temporal = getNewNameTemporal();
		}
		temporal.setBegin(date);
		temporal.setObject(object);
		temporal.setValue(objectName);

		TimeLine<TV, DI> timeLine = getTimeLine(object.getId());
		TimeLine<TV, DI> timeLineNew =
				DateIntervalUtil.addInterval(timeLine, temporal);
		timeLine = DateIntervalUtil.invalidate(timeLine);
		saveTimeLine(timeLine);
		saveTimeLine(timeLineNew);
		object.setNamesTimeLine(timeLineNew);

		return object;
	}

	private TimeLine<TV, DI> getTimeLine(Long objectId) {
		NTD object = getNameTimeDependentDao().readFull(objectId);
		return object.getNamesTimeLine();
	}

	private void saveTimeLine(TimeLine<TV, DI> tl) {
		for (DI temporal : tl.getIntervals()) {
			if (temporal.getId() == null) {
				getNameTemporalDao().create(temporal);
			} else {
				getNameTemporalDao().update(temporal);
			}
		}
	}

	/**
	 * Check object names translations
	 *
	 * @param nameTranslations translations
	 * @param container		Errors container
	 * @return Set of not empty translations
	 * @throws FlexPayExceptionContainer if translations specified have invalid values
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private Set<T> getTranslations(List<T> nameTranslations, FlexPayExceptionContainer container)
			throws FlexPayExceptionContainer {

		Set<T> names = new HashSet<T>();
		for (T nameTranslation : nameTranslations) {
			if (nameTranslation.getLang().isDefault()
				&& StringUtils.isBlank(nameTranslation.getName())) {

				FlexPayException e = new FlexPayException("No default lang translation",
						"error.no_default_translation");
				container.addException(e);
				continue;
			}
			if (StringUtils.isNotBlank(nameTranslation.getName())) {
				names.add(nameTranslation);
			}
		}

		if (!container.getExceptions().isEmpty()) {
			throw container;
		}

		return names;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<NTD> find(ArrayStack filters) {
		PrimaryKeyFilter filter = (PrimaryKeyFilter) filters.peek();
		return getNameTimeDependentDao().findObjects(
				ObjectWithStatus.STATUS_ACTIVE, filter.getSelectedId());
	}

	/**
	 * {@inheritDoc}
	 */
	public List<NTD> find(ArrayStack filters, Page pager) {
		PrimaryKeyFilter filter = (PrimaryKeyFilter) filters.peek();
		return getNameTimeDependentDao().findObjects(
				pager, ObjectWithStatus.STATUS_ACTIVE, filter.getSelectedId());
	}

	/**
	 * Find existing object by name
	 *
	 * @param name	 Object name to search
	 * @param filter Parent object filter
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	public NTD findByName(String name, PrimaryKeyFilter filter) {
		List<NTD> objs = getNameTimeDependentDao().findObjects(
				ObjectWithStatus.STATUS_ACTIVE, filter.getSelectedId());
		for (NTD obj : objs) {
			TV nameObj = obj.getCurrentName();
			if (nameObj == null) {
				continue;
			}
			for (T translation : nameObj.getTranslations()) {
				if (translation.getName().equalsIgnoreCase(name)) {
					return obj;
				}
			}
		}

		return null;
	}
}
