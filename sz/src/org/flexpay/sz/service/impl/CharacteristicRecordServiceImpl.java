package org.flexpay.sz.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.sz.dao.CharacteristicRecordDao;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
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
	@Override
	public CharacteristicRecord create(CharacteristicRecord characteristic) {
		characteristicRecordDao.create(characteristic);
		log.debug("Created CharacteristicRecord: {}", characteristic);

		return characteristic;
	}

	@Transactional (readOnly = false)
	@Override
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
	@Override
	public void deleteBySzFileId(Long id) {
		characteristicRecordDao.deleteBySzFileId(id);
	}

	@Required
	public void setCharacteristicRecordDao(CharacteristicRecordDao characteristicRecordDao) {
		this.characteristicRecordDao = characteristicRecordDao;
	}

}
