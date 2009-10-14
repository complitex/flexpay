package org.flexpay.sz.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.sz.dao.ServiceTypeRecordDao;
import org.flexpay.sz.persistence.ServiceTypeRecord;
import org.flexpay.sz.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
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
	@Override
	public ServiceTypeRecord create(ServiceTypeRecord serviceTypeRecord) {
		serviceTypeRecordDao.create(serviceTypeRecord);
		log.debug("Created ServiceTypeRecord: {}", serviceTypeRecord);

		return serviceTypeRecord;
	}

	@Transactional (readOnly = false)
	@Override
	public List<ServiceTypeRecord> findObjects(Page<ServiceTypeRecord> pager, Long szFileId) {
		return serviceTypeRecordDao.findObjects(pager, szFileId);
	}

	/**
	 * Delete all ServiceTypeRecord by SzFile
	 *
	 * @param id ServiceTypeRecord's id field
	 */
	@Transactional (readOnly = false)
	@Override
	public void deleteBySzFileId(Long id) {
		serviceTypeRecordDao.deleteBySzFileId(id);
	}

	@Required
	public void setServiceTypeRecordDao(ServiceTypeRecordDao serviceTypeRecordDao) {
		this.serviceTypeRecordDao = serviceTypeRecordDao;
	}

}
