package org.flexpay.common.service.imp;

import org.flexpay.common.service.DiffService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.dao.DiffDao;
import org.flexpay.common.dao.DiffDaoExt;
import org.flexpay.common.dao.paging.FetchRange;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Transactional (readOnly = true)
public class DiffServiceImpl implements DiffService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ClassToTypeRegistry registry;
	private DiffDao diffDao;
	private DiffDaoExt diffDaoExt;

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
		return diffDaoExt.hasDiffs(obj.getId(), objectType);
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
}
