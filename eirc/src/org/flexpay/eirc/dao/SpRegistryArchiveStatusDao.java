package org.flexpay.eirc.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.SpRegistryArchiveStatus;

public interface SpRegistryArchiveStatusDao extends
		GenericDao<SpRegistryArchiveStatus, Long> {
	List<SpRegistryArchiveStatus> findByCode(int code);

}
