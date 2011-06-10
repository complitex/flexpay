package org.flexpay.common.process.audit;

import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ProcessInstanceDbLog {
	@NotNull
	List<ProcessInstanceLog> findProcessInstances();

	@NotNull
	List<ProcessInstanceLog> findProcessInstances(@NotNull String processId);

	@Nullable
	ProcessInstanceLog findProcessInstance(long processInstanceId);

	@NotNull
	List<NodeInstanceLog> findNodeInstances(long processInstanceId);

	@NotNull
	List<NodeInstanceLog> findNodeInstances(long processInstanceId, @NotNull String nodeId);

	@NotNull
	List<VariableInstanceLog> findVariableInstances(long processInstanceId);

	@NotNull
	List<VariableInstanceLog> findVariableInstances(long processInstanceId, @NotNull String variableId);

	void clear();
}
