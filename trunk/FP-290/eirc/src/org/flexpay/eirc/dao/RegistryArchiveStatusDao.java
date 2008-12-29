package org.flexpay.eirc.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.RegistryArchiveStatus;

public interface RegistryArchiveStatusDao extends GenericDao<RegistryArchiveStatus, Long> {

	List<RegistryArchiveStatus> findByCode(int code);
}
