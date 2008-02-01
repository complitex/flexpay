package org.flexpay.sz.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.sz.persistence.CharacteristicRecord;

public interface CharacteristicRecordDao extends GenericDao<CharacteristicRecord, Long>{
	
	List<CharacteristicRecord> findObjects(Page<CharacteristicRecord> pager, Long szFileId);
	
	void deleteBySzFileId(Long id);
	
	
}
