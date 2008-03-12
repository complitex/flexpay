package org.flexpay.eirc.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.dao.SpRegistryRecordDao;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SpRegistryRecordServiceImpl implements SpRegistryRecordService {
	private static Logger log = Logger
			.getLogger(SpRegistryRecordServiceImpl.class);

	private SpRegistryRecordDao spRegistryRecordDao;

	/**
	 * Create SpRegistryRecord
	 * 
	 * @param spRegistryRecord
	 *            SpRegistryRecord object
	 * @return created SpRegistryRecord object
	 */
	@Transactional(readOnly = false)
	public SpRegistryRecord create(SpRegistryRecord spRegistryRecord)
			throws FlexPayException {
		spRegistryRecordDao.create(spRegistryRecord);

		if (log.isDebugEnabled()) {
			log.debug("Created SpRegistryRecord: " + spRegistryRecord);
		}

		return spRegistryRecord;
	}

	/**
	 * Read SpRegistryRecord object by its unique id
	 * 
	 * @param id
	 *            SpRegistryRecord key
	 * @return SpRegistryRecord object, or <code>null</code> if object not
	 *         found
	 */
	public SpRegistryRecord read(Long id) {
		return spRegistryRecordDao.read(id);
	}

	/**
	 * Update SpRegistryRecord
	 * 
	 * @param spRegistryRecord
	 *            SpRegistryRecord to update for
	 * @return Updated SpRegistryRecord object
	 * @throws FlexPayException
	 *             if SpRegistryRecord object is invalid
	 */
	@Transactional(readOnly = false)
	public SpRegistryRecord update(SpRegistryRecord spRegistryRecord)
			throws FlexPayException {
		spRegistryRecordDao.update(spRegistryRecord);

		return spRegistryRecord;
	}

	@Transactional(readOnly = false)
	public void delete(SpRegistryRecord spRegistryRecord) {
		spRegistryRecordDao.delete(spRegistryRecord);
	}

	/**
	 * @param spRegistryRecordDao
	 *            the spRegistryRecordDao to set
	 */
	public void setSpRegistryRecordDao(SpRegistryRecordDao spRegistryRecordDao) {
		this.spRegistryRecordDao = spRegistryRecordDao;
	}
}
