package org.flexpay.common.process.audit.dao;

import org.jbpm.process.audit.ProcessInstanceLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ProcessInstanceLogDao {

	@NotNull
	List<ProcessInstanceLog> findProcessInstances();

	@NotNull
	List<ProcessInstanceLog> findProcessInstances(@NotNull String processId);

	@Nullable
	ProcessInstanceLog findProcessInstance(long processInstanceId);

	@Nullable
	ProcessInstanceLog findNotEndedProcessInstance(long processInstaceId);

	@NotNull
	ProcessInstanceLog create(@NotNull ProcessInstanceLog processInstance);

	@NotNull
	ProcessInstanceLog update(@NotNull ProcessInstanceLog processInstance);

}
