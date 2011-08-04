package org.flexpay.eirc.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.ProcessRegistryVariableInstanceDao;
import org.flexpay.eirc.persistence.registry.ProcessRegistryVariableInstance;
import org.flexpay.eirc.service.ProcessRegistryVariableInstanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ProcessRegistryVariableInstanceServiceImpl implements ProcessRegistryVariableInstanceService {

	ProcessRegistryVariableInstanceDao processRegistryVariableInstanceDao;

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public ProcessRegistryVariableInstance create(@NotNull ProcessRegistryVariableInstance variable) {
		variable.setId(null);
		processRegistryVariableInstanceDao.create(variable);

		return variable;
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Transactional (readOnly = false, propagation = Propagation.SUPPORTS)
	@Override
	public ProcessRegistryVariableInstance update(@NotNull ProcessRegistryVariableInstance variable) {
		processRegistryVariableInstanceDao.update(variable);

		return variable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(@NotNull ProcessRegistryVariableInstance variable) {
		processRegistryVariableInstanceDao.delete(variable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional (readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public ProcessRegistryVariableInstance findVariable(@NotNull Long processId) {
		List<ProcessRegistryVariableInstance> variables = processRegistryVariableInstanceDao.findByProcessId(processId);
		if (variables == null || variables.size() == 0) {
			return null;
		}
		return variables.get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional (readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public ProcessRegistryVariableInstance readFull(@NotNull Stub<ProcessRegistryVariableInstance> stub) {
		return processRegistryVariableInstanceDao.readFull(stub.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional (readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public ProcessRegistryVariableInstance update(@NotNull Stub<ProcessRegistryVariableInstance> stub,
												  @NotNull Long lastProcessedRegistryRecord,
												  @NotNull Integer incProcessedCountRecords) {
		ProcessRegistryVariableInstance variable = readFull(stub);
		if (variable == null) {
			return null;
		}

		Integer processedCountRecords = variable.getProcessedCountRecords() == null? 0: variable.getProcessedCountRecords();
		variable.setProcessedCountRecords(processedCountRecords + incProcessedCountRecords);

		if (variable.getLastProcessedRegistryRecord() == null ||
				lastProcessedRegistryRecord > variable.getLastProcessedRegistryRecord()) {

			variable.setLastProcessedRegistryRecord(lastProcessedRegistryRecord);

		}
		return update(variable);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Required
	public void setProcessRegistryVariableInstanceDao(ProcessRegistryVariableInstanceDao processRegistryVariableInstanceDao) {
		this.processRegistryVariableInstanceDao = processRegistryVariableInstanceDao;
	}
}
