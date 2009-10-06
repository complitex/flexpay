package org.flexpay.sz.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.sz.dao.ServiceTypeRecordDao;
import org.flexpay.sz.persistence.ServiceTypeRecord;
import org.flexpay.sz.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class ServiceTypeRecordServiceImpl implements RecordService<ServiceTypeRecord> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ServiceTypeRecordDao serviceTypeRecordDao;

	/**
	 * Create ServiceTypeRecord
	 *
	 * @param serviceTypeRecord ServiceTypeRecordRecord object
	 * @return created ServiceTypeRecord object
	 */
	@Transactional (readOnly = false)
	public ServiceTypeRecord create(ServiceTypeRecord serviceTypeRecord) {
		serviceTypeRecordDao.create(serviceTypeRecord);
		log.debug("Created ServiceTypeRecord: {}", serviceTypeRecord);

		return serviceTypeRecord;
	}

	@Transactional (readOnly = false)
	public List<ServiceTypeRecord> findObjects(Page<ServiceTypeRecord> pager, Long szFileId) {
		return serviceTypeRecordDao.findObjects(pager, szFileId);
	}

	/**
	 * Delete all ServiceTypeRecord by SzFile
	 *
	 * @param id ServiceTypeRecord's id field
	 */
	@Transactional (readOnly = false)
	public void deleteBySzFileId(Long id) {
		serviceTypeRecordDao.deleteBySzFileId(id);
	}

	public void setServiceTypeRecordDao(ServiceTypeRecordDao serviceTypeRecordDao) {
		this.serviceTypeRecordDao = serviceTypeRecordDao;
	}
}
