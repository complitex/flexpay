package org.flexpay.sz.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.sz.dao.CharacteristicRecordDao;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.service.RecordService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class CharacteristicRecordServiceImpl implements RecordService<CharacteristicRecord> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private CharacteristicRecordDao characteristicRecordDao;

	/**
	 * Create CharacteristicRecord
	 *
	 * @param characteristic CharacteristicRecord object
	 * @return created CharacteristicRecord object
	 */
	@Transactional (readOnly = false)
	public CharacteristicRecord create(CharacteristicRecord characteristic) {
		characteristicRecordDao.create(characteristic);
		log.debug("Created CharacteristicRecord: {}", characteristic);

		return characteristic;
	}

	@Transactional (readOnly = false)
	public List<CharacteristicRecord> findObjects(
			Page<CharacteristicRecord> pager, Long szFileId) {
		return characteristicRecordDao.findObjects(pager, szFileId);
	}

	/**
	 * Delete all CharacteristicRecord by SzFile
	 *
	 * @param id CharacteristicRecord's id field
	 */
	@Transactional (readOnly = false)
	public void deleteBySzFileId(Long id) {
		characteristicRecordDao.deleteBySzFileId(id);
	}

	public void setCharacteristicRecordDao(CharacteristicRecordDao characteristicRecordDao) {
		this.characteristicRecordDao = characteristicRecordDao;
	}

}
