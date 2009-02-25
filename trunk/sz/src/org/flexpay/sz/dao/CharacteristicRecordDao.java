package org.flexpay.sz.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.sz.persistence.CharacteristicRecord;

import java.util.List;

public interface CharacteristicRecordDao extends GenericDao<CharacteristicRecord, Long>{
	
	List<CharacteristicRecord> findObjects(Page<CharacteristicRecord> pager, Long szFileId);
	
	void deleteBySzFileId(Long id);
	
	
}
