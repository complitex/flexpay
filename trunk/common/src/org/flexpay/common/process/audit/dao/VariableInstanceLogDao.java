package org.flexpay.common.process.audit.dao;

import org.jbpm.process.audit.VariableInstanceLog;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface VariableInstanceLogDao {

	@NotNull
	List<VariableInstanceLog> findVariableInstances(long processInstanceId);

	@NotNull
	List<VariableInstanceLog> findVariableInstances(long processInstanceId, String variableId);

	@NotNull
	VariableInstanceLog create(@NotNull VariableInstanceLog variableInstance);
}
