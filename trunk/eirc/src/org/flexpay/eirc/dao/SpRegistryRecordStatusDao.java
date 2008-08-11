package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.RegistryRecordStatus;

import java.util.List;

public interface SpRegistryRecordStatusDao extends GenericDao<RegistryRecordStatus, Long> {

	List<RegistryRecordStatus> findAll();
}