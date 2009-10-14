package org.flexpay.sz.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.sz.dao.SubsidyRecordDao;
import org.flexpay.sz.persistence.SubsidyRecord;
import org.flexpay.sz.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class SubsidyRecordServiceImpl implements RecordService<SubsidyRecord> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private SubsidyRecordDao subsidyRecordDao;

	/**
	 * Create SubsidyRecord
	 * 
	 * @param subsidyRecord
	 *            SubsidyRecordRecord object
	 * @return created SubsidyRecord object
	 */
	@Transactional(readOnly = false)
	@Override
	public SubsidyRecord create(SubsidyRecord subsidyRecord) {
		subsidyRecordDao.create(subsidyRecord);
		log.debug("Created SubsidyRecord: {}", subsidyRecord);

		return subsidyRecord;
	}

	@Transactional(readOnly = false)
	@Override
	public List<SubsidyRecord> findObjects(Page<SubsidyRecord> pager, Long szFileId) {
		return subsidyRecordDao.findObjects(pager, szFileId);
	}

	/**
	 * Delete all SubsidyRecord by SzFile
	 * 
	 * @param id
	 *            SubsidyRecord's id field
	 */
	@Transactional(readOnly = false)
	@Override
	public void deleteBySzFileId(Long id) {
		subsidyRecordDao.deleteBySzFileId(id);
	}

	@Required
	public void setSubsidyRecordDao(SubsidyRecordDao subsidyRecordDao) {
		this.subsidyRecordDao = subsidyRecordDao;
	}

}
