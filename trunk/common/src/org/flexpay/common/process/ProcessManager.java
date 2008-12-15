package org.flexpay.common.process;

import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.service.Roles;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProcessManager {

	/**
	 * Deploys process definition to jbpm by process definition name
	 *
	 * @param name	name of process definition
	 * @param replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 * @throws ProcessDefinitionException when can't deplot process definition to jbpm
	 */
	@Secured (Roles.PROCESS_DEFINITION_UPLOAD_NEW)
	public long deployProcessDefinition(String name, boolean replace) throws ProcessDefinitionException;

	/**
	 * Deploys parsed process definition to jbpm
	 *
	 * @param definition parsed process definition
	 * @param replace	replace replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 */
	@Secured (Roles.PROCESS_DEFINITION_UPLOAD_NEW)
	public long deployProcessDefinition(ProcessDefinition definition, boolean replace);

	/**
	 * Deploys process definition to jbpm from inputStream
	 *
	 * @param in	  input stream with process definition
	 * @param replace replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 * @throws ProcessDefinitionException when can't deploy process definition to jbpm
	 */
	public long deployProcessDefinition(InputStream in, boolean replace) throws ProcessDefinitionException;

	@Secured (Roles.PROCESS_DELETE)
	public void deleteProcessInstance(final Process process);

	@Secured (Roles.PROCESS_DELETE)
	public void deleteProcessInstances(List<Process> processes);

	@Secured (Roles.PROCESS_DELETE)
	public void deleteProcessInstances(final Set<Long> processIds);

	/**
	 * Wait for process completion
	 *
	 * @param processId ProcessInstance id
	 * @throws InterruptedException if waiting thread is interrupted
	 */
	public void join(long processId) throws InterruptedException;

	/**
	 * Create process for process definition name
	 *
	 * @param definitionName process definitiona name
	 * @param parameters	 initial context variables
	 * @return process instance identifier
	 * @throws org.flexpay.common.process.exception.ProcessInstanceException
	 *                                    when can't instantiate process instance
	 * @throws ProcessDefinitionException when process definition not found
	 */
	public long createProcess(String definitionName, Map<Serializable, Serializable> parameters)
			throws ProcessInstanceException, ProcessDefinitionException;

	/**
	 * Get list of system processes
	 *
	 * @return Process list
	 */
	@Secured (Roles.PROCESS_READ)
	public List<Process> getProcesses();

	public List<TaskInstance> getRunningTasks();

	/**
	 * Called when process job was finished
	 *
	 * @param taskId	 Task ID
	 * @param parameters Task context parameters
	 * @param transition transition name
	 */
	public void taskCompleted(Long taskId, Map<Serializable, Serializable> parameters, String transition);

	/**
	 * Retrive process info
	 *
	 * @param processId ProcessInstance id
	 * @return Process info
	 */
	@Secured (Roles.PROCESS_READ)
	@NotNull
	public Process getProcessInstanceInfo(@NotNull final Long processId);

	public void setDefinitionPaths(List<String> definitionPaths);

	/**
	 * Add voters
	 *
	 * @param lyfecycleVoters Voters to set
	 */
	public void setLyfecycleVoters(List<LyfecycleVoter> lyfecycleVoters);
}
