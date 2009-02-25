package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.RegistryArchiveStatus;

import java.util.List;

public interface RegistryArchiveStatusDao extends GenericDao<RegistryArchiveStatus, Long> {

	List<RegistryArchiveStatus> findByCode(int code);
}
