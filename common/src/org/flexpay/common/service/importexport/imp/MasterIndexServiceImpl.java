package org.flexpay.common.service.importexport.imp;

import org.flexpay.common.dao.MasterIndexBoundsDao;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.persistence.MasterIndexBounds;
import org.flexpay.common.service.importexport.MasterIndexProvider;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public class MasterIndexServiceImpl implements MasterIndexService {

	private static final String LOCK_NAME = MasterIndexServiceImpl.class.getName() + ".lock.";

	/**
	 * Number of indexes taken at time from provider
	 */
	private static final int DEFAULT_BATCH_SIZE = 1000;

	private LockManager lockManager;
	private MasterIndexProvider masterIndexProvider;
	private MasterIndexBoundsDao masterIndexBoundsDao;

	private Map<Integer, MasterIndexBounds> bounds = CollectionUtils.map();

	/**
	 * Get next master index value
	 *
	 * @param type Objects type to get next index for
	 * @return next index value
	 */
	public Long next(int type) {
		return nextBatch(type, 1)[0];
	}

	/**
	 * Get batch of master index values
	 *
	 * @param type   Objects type to get next indexes for
	 * @param number Number of required index values
	 * @return next index values, minimum-maximum pair
	 */
	public Long[] nextBatch(int type, int number) {
		try {
			Long[] minmax = {null, null};
			lockManager.lock(LOCK_NAME);
			MasterIndexBounds indexBounds = bounds.get(type);
			if (indexBounds == null) {
				List<MasterIndexBounds> boundses = masterIndexBoundsDao.findIndexBoundses(type);
				if (boundses.isEmpty()) {
					indexBounds = new MasterIndexBounds(type);
				} else {
					indexBounds = boundses.get(0);
				}
			}

			// if number of available indexes is not anough, request more from provider
			if (indexBounds.getSize() < number) {
				int requestedNumber = Math.max(DEFAULT_BATCH_SIZE, number);
				MasterIndexBounds requestedBounds = masterIndexProvider.getIndexBatch(type, requestedNumber);
				indexBounds.setLowerBound(requestedBounds.getLowerBound());
				indexBounds.setUpperBound(requestedBounds.getUpperBound());
			}

			minmax[0] = indexBounds.getLowerBound();
			indexBounds.increment(number);
			minmax[1] = minmax[0] + number;

			if (indexBounds.isNew()) {
				masterIndexBoundsDao.create(indexBounds);
			} else {
				masterIndexBoundsDao.update(indexBounds);
			}
			return minmax;
		} finally {
			lockManager.releaseLock(LOCK_NAME);
		}
	}

	@Required
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	@Required
	public void setMasterIndexProvider(MasterIndexProvider masterIndexProvider) {
		this.masterIndexProvider = masterIndexProvider;
	}

	@Required
	public void setMasterIndexBoundsDao(MasterIndexBoundsDao masterIndexBoundsDao) {
		this.masterIndexBoundsDao = masterIndexBoundsDao;
	}
}
