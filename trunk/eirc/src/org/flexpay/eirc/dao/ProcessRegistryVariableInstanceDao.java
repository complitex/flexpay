package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.registry.ProcessRegistryVariableInstance;

import java.util.List;

public interface ProcessRegistryVariableInstanceDao extends GenericDao<ProcessRegistryVariableInstance, Long> {

	/**
	 * Find  process variables by process Id
	 *
	 * @param processId ProcessInstance Id
	 * @return Process variables
	 */
	List<ProcessRegistryVariableInstance> findByProcessId(Long processId);
}
