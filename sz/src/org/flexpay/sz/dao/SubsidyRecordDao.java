package org.flexpay.sz.dao;

import org.flexpay.sz.persistence.SubsidyRecord;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface SubsidyRecordDao extends GenericDao<SubsidyRecord, Long> {

	List<SubsidyRecord> findObjects(Page<SubsidyRecord> pager, Long szFileId);

	void deleteBySzFileId(Long id);

}
