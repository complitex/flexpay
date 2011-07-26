package org.flexpay.common.service.impl;

import org.flexpay.common.dao.DiffDao;
import org.flexpay.common.dao.DiffDaoExt;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.AllObjectsService;
import org.flexpay.common.service.DiffService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.util.LRUCache;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;

@SuppressWarnings ({"RawUseOfParameterizedType"})
@Transactional (readOnly = true)
public class DiffServiceImpl implements DiffService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ClassToTypeRegistry registry;
	private DiffDao diffDao;
	private DiffDaoExt diffDaoExt;

	private Map<Class, AllObjectsService> type2AllObjectsServiceMap = map();
	private Map<Integer, AllObjectsService> typeId2AllObjectsServiceMap = map();

	private Map<Integer, LRUCache<Long, Boolean>> hasHistoryCaches = map();

	/**
	 * Persist a new Diff object
	 *
	 * @param diff Diff object to persist
	 * @return Diff back
	 */
	@Transactional (readOnly = false)
	@NotNull
	public Diff create(@NotNull Diff diff) {

		log.debug("Creating a new diff {}", diff);

		diffDao.create(diff);
		return diff;
	}

	/**
	 * Persist a batch of new Diff objects
	 *
	 * @param diffs Diff objects to persist
	 * @return Persisted Diffs back
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Collection<Diff> create(@NotNull Collection<Diff> diffs) {

		for (Diff diff : diffs) {
			log.debug("Creating a new diff {}", diff);
			diffDao.create(diff);
		}

		return diffs;
	}

	/**
	 * Update existing diff
	 *
	 * @param diff History records set to update
	 * @return Diff object back
	 */
	@Transactional (readOnly = false)
	@NotNull
	public Diff update(@NotNull Diff diff) {

		log.debug("Updating diff {}", diff);

		diffDao.update(diff);
		return diff;
	}

	/**
	 * Read diff with all records fetched
	 *
	 * @param stub Diff stub
	 * @return Diff if found, or <code>null</code> otherwise
	 */
	public Diff readFull(@NotNull Stub<Diff> stub) {
		return diffDao.readFull(stub.getId());
	}

	/**
	 * Find existing diffs for domain object
	 *
	 * @param obj Domain object to find history diffs for
	 * @param <T> Object type
	 * @return List of diffs for object
	 */
	@NotNull
	public <T extends DomainObject> List<Diff> findDiffs(@NotNull T obj) {
		int objectType = registry.getType(obj.getClass());
		return diffDao.findDiffs(obj.getId(), objectType);
	}

	/**
	 * Check if there is some history for an object
	 *
	 * @param obj Object to check history existence for
	 * @return <code>true</code> if there is diffs, or <code>false</code> otherwise
	 */
	public <T extends DomainObject> boolean hasDiffs(@NotNull T obj) {

		int objectType = registry.getType(obj.getClass());

		LRUCache<Long, Boolean> cache = hasHistoryCache(objectType);
		if (cache.get(obj.getId()) != null) {
			return true;
		}

		boolean hasDiffs = diffDaoExt.hasDiffs(obj.getId(), objectType);

		if (hasDiffs) {
			cache.put(obj.getId(), true);
		}

		return hasDiffs;
	}

	private LRUCache<Long, Boolean> hasHistoryCache(int objectType) {

		LRUCache<Long, Boolean> cache = hasHistoryCaches.get(objectType);
		if (cache == null) {
			cache = new LRUCache<Long, Boolean>(5000);
			hasHistoryCaches.put(objectType, cache);
		}

		return cache;
	}

	/**
	 * Check if there is some history for all objects of a specified class
	 *
	 * @param clazz Objects class to check history existence for
	 * @return <code>true</code> if there is diffs for all objects of that class, or <code>false</code> otherwise
	 */
	@Override
	public <T extends DomainObject> boolean allObjectsHaveDiff(Class<T> clazz) {

		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (type2AllObjectsServiceMap != Collections.EMPTY_MAP) {
			moveAllObjectsServices();
		}

		int objectsType = registry.getType(clazz);
		@SuppressWarnings ({"unchecked"})
		AllObjectsService<T> service = typeId2AllObjectsServiceMap.get(objectsType);
		if (service == null) {
			throw new IllegalStateException("Check for all objects " + clazz +
											" called, but no AllObjectsService set for this class");
		}
		List<T> objects = service.getAll();
		for (T t : objects) {
			if (!hasDiffs(t)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Remove all persisted loaded diffs, in case of failure for example
	 */
	@Transactional (readOnly = false)
	@Override
	public void removeLoadedDiffs() {
		diffDaoExt.removeDiffs(ProcessingStatus.STATUS_LOADED);
	}

	/**
	 * Update loaded diffs state, make their status {@link org.flexpay.common.persistence.history.ProcessingStatus#STATUS_NEW}
	 */
	@Transactional (readOnly = false)
	@Override
	public void moveLoadedDiffsToNewState() {
		diffDaoExt.updateDiffsProcessingStatus(
				ProcessingStatus.STATUS_LOADED, ProcessingStatus.STATUS_NEW);
	}

	/**
	 * Fetch diffs got from last consumer update
	 *
	 * @param range Fetch range
	 * @return list of diffs, possibly empty
	 */
	@NotNull
	public List<Diff> findNewDiffs(@NotNull FetchRange range) {
		return diffDaoExt.findNewHistoryRecords(range);
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        diffDao.setJpaTemplate(jpaTemplate);
        diffDaoExt.setJpaTemplate(jpaTemplate);
    }

    @SuppressWarnings ({"unchecked"})
    private void moveAllObjectsServices() {
        for (Map.Entry<Class, AllObjectsService> entry : type2AllObjectsServiceMap.entrySet()) {
            typeId2AllObjectsServiceMap.put(registry.getType(entry.getKey()), entry.getValue());
        }

        type2AllObjectsServiceMap = Collections.emptyMap();
    }

	@Required
	public void setDiffDao(DiffDao diffDao) {
		this.diffDao = diffDao;
	}

	@Required
	public void setDiffDaoExt(DiffDaoExt diffDaoExt) {
		this.diffDaoExt = diffDaoExt;
	}

	@Required
	public void setRegistry(ClassToTypeRegistry registry) {
		this.registry = registry;
	}

	public void setAllObjectsService(Map<Class, AllObjectsService> map) {
		type2AllObjectsServiceMap.putAll(map);
	}

}
