package org.flexpay.sz.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.sz.persistence.ServiceTypeRecord;

import java.util.List;

public interface ServiceTypeRecordDao extends GenericDao<ServiceTypeRecord, Long> {

	List<ServiceTypeRecord> findObjects(Page<ServiceTypeRecord> pager, Long szFileId);

	void deleteBySzFileId(Long id);
}