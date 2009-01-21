package org.flexpay.common.service.importexport.imp;

import org.flexpay.common.dao.MasterIndexDao;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.persistence.MasterIndex;
import org.flexpay.common.persistence.MasterIndexBounds;
import org.flexpay.common.service.importexport.MasterIndexProvider;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class MasterIndexProviderDBImpl implements MasterIndexProvider {

	private static final String LOCK_NAME = MasterIndexProviderDBImpl.class.getName() + ".lock.";

	private LockManager lockManager;
	private MasterIndexDao masterIndexDao;

	public MasterIndexBounds getIndexBatch(int type, int number) {

		MasterIndexBounds bounds = new MasterIndexBounds(type);
		try {
			lockManager.lock(LOCK_NAME);
			List<MasterIndex> indexes = masterIndexDao.findIndexes(type);
			MasterIndex index = indexes.isEmpty() ? null : indexes.get(0);
			if (index == null) {
				index = new MasterIndex();
				index.setObjectType(type);
			}

			bounds.setLowerBound(index.getIndex() + 1);
			index.increment(number);
			bounds.setUpperBound(index.getIndex());

			if (index.isNew()) {
				masterIndexDao.create(index);
			} else {
				masterIndexDao.update(index);
			}

			return bounds;
		} finally {
			lockManager.releaseLock(LOCK_NAME);
		}
	}

	@Required
	public void setMasterIndexDao(MasterIndexDao masterIndexDao) {
		this.masterIndexDao = masterIndexDao;
	}

	@Required
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}
}
