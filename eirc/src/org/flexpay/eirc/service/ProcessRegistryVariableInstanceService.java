package org.flexpay.eirc.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.Roles;
import org.flexpay.eirc.persistence.registry.ProcessRegistryVariableInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

public interface ProcessRegistryVariableInstanceService {

	/**
	 * Create variables for processing registry
	 *
	 * @param variable New variable
	 * @return  persisted variable instance object back
	 */
	@NotNull
	@Secured(Roles.PROCESS_READ)
	ProcessRegistryVariableInstance create(@NotNull ProcessRegistryVariableInstance variable);

	/**
	 * Update variables for processing registry
	 *
	 * @param variable Variable
	 * @return  updated variable instance object back
	 */
	@NotNull
	ProcessRegistryVariableInstance update(@NotNull ProcessRegistryVariableInstance variable);

	/**
	 * Delete variables for processing registry
	 *
	 * @param variable Variable
	 */
	@Secured(Roles.PROCESS_READ)
	void delete(@NotNull ProcessRegistryVariableInstance variable);

	/**
	 * Find variable by  process Id
	 *
	 * @param processId Process Id
	 * @return Variable instance object back
	 */
	@Nullable
	@Secured(Roles.PROCESS_READ)
	ProcessRegistryVariableInstance findVariable(@NotNull Long processId);

	/**
	 * Read variable instance
	 *
	 * @param stub Variable stub
	 * @return Variable if found, or <code>null</code> if stub references no object
	 */
	@Nullable
	@Secured(Roles.PROCESS_READ)
	ProcessRegistryVariableInstance readFull(@NotNull Stub<ProcessRegistryVariableInstance> stub);

}
