package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.SpRegistryRecordStatus;

import java.util.List;

public interface SpRegistryRecordStatusDao extends GenericDao<SpRegistryRecordStatus, Long> {

	List<SpRegistryRecordStatus> findAll();
}