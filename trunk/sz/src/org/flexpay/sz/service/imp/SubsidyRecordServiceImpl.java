package org.flexpay.sz.service.imp;

import org.flexpay.sz.persistence.SubsidyRecord;
import org.flexpay.sz.service.RecordService;
import org.flexpay.sz.dao.SubsidyRecordDao;
import org.flexpay.common.dao.paging.Page;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SubsidyRecordServiceImpl implements RecordService<SubsidyRecord> {

	private Logger log = Logger.getLogger(getClass());

	private SubsidyRecordDao subsidyRecordDao;

	/**
	 * Create SubsidyRecord
	 * 
	 * @param subsidyRecord
	 *            SubsidyRecordRecord object
	 * @return created SubsidyRecord object
	 */
	@Transactional(readOnly = false)
	public SubsidyRecord create(SubsidyRecord subsidyRecord) {
		subsidyRecordDao.create(subsidyRecord);
		if (log.isDebugEnabled()) {
			log.debug("Created SubsidyRecord: " + subsidyRecord);
		}

		return subsidyRecord;
	}

	@Transactional(readOnly = false)
	public List<SubsidyRecord> findObjects(Page<SubsidyRecord> pager,
			Long szFileId) {
		return subsidyRecordDao.findObjects(pager, szFileId);
	}

	/**
	 * Delete all SubsidyRecord by SzFile
	 * 
	 * @param id
	 *            SubsidyRecord's id field
	 */
	@Transactional(readOnly = false)
	public void deleteBySzFileId(Long id) {
		subsidyRecordDao.deleteBySzFileId(id);
	}

	public void setSubsidyRecordDao(SubsidyRecordDao subsidyRecordDao) {
		this.subsidyRecordDao = subsidyRecordDao;
	}

}
