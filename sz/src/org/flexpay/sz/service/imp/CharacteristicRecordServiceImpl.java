package org.flexpay.sz.service.imp;

import java.util.List;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.sz.dao.CharacteristicRecordDao;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.service.RecordService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CharacteristicRecordServiceImpl implements
		RecordService<CharacteristicRecord> {
	private static Logger log = Logger
			.getLogger(CharacteristicRecordServiceImpl.class);

	private CharacteristicRecordDao characteristicRecordDao;

	/**
	 * Create CharacteristicRecord
	 * 
	 * @param characteristic
	 *            CharacteristicRecord object
	 * @return created CharacteristicRecord object
	 */
	@Transactional(readOnly = false)
	public CharacteristicRecord create(CharacteristicRecord characteristic) {
		characteristicRecordDao.create(characteristic);
		if (log.isDebugEnabled()) {
			log.debug("Created CharacteristicRecord: " + characteristic);
		}

		return characteristic;
	}

	@Transactional(readOnly = false)
	public List<CharacteristicRecord> findObjects(Page<CharacteristicRecord> pager, Long szFileId) {
		return characteristicRecordDao.findObjects(pager, szFileId);
	}

	public void setCharacteristicRecordDao(
			CharacteristicRecordDao characteristicRecordDao) {
		this.characteristicRecordDao = characteristicRecordDao;
	}

}
