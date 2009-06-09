package org.flexpay.common.process;

import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.sorter.ProcessSorter;
import org.flexpay.common.service.Roles;
import org.flexpay.common.dao.paging.Page;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;

public interface ProcessManager {

	/**
	 * Key name used to store security context
	 */
	String PARAM_SECURITY_CONTEXT = "_PROCESS_MANAGER_SECURITY_CONTEXT";

	/**
	 * Deploys process definition to jbpm by process definition name
	 *
	 * @param name	name of process definition
	 * @param replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 * @throws ProcessDefinitionException when can't deplot process definition to jbpm
	 */
	@Secured (Roles.PROCESS_DEFINITION_UPLOAD_NEW)
	long deployProcessDefinition(String name, boolean replace) throws ProcessDefinitionException;

	/**
	 * Deploys parsed process definition to jbpm
	 *
	 * @param definition parsed process definition
	 * @param replace	replace replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 */
	@Secured (Roles.PROCESS_DEFINITION_UPLOAD_NEW)
	long deployProcessDefinition(ProcessDefinition definition, boolean replace);

	/**
	 * Deploys process definition to jbpm from inputStream
	 *
	 * @param in	  input stream with process definition
	 * @param replace replace if true old process definition should be removed with new one
	 * @return ID of process definition
	 * @throws ProcessDefinitionException when can't deploy process definition to jbpm
	 */
	long deployProcessDefinition(InputStream in, boolean replace) throws ProcessDefinitionException;

	@Secured (Roles.PROCESS_DELETE)
	void deleteProcessInstance(final Process process);

	@Secured (Roles.PROCESS_DELETE)
	void deleteProcessInstances(List<Process> processes);

	@Secured (Roles.PROCESS_DELETE)
	void deleteProcessInstances(final Set<Long> processIds);

	/**
	 * Wait for process completion
	 *
	 * @param processId ProcessInstance id
	 * @throws InterruptedException if waiting thread is interrupted
	 */
	void join(long processId) throws InterruptedException;

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
	long createProcess(String definitionName, Map<Serializable, Serializable> parameters)
			throws ProcessInstanceException, ProcessDefinitionException;

	/**
	 * Get list of system processes
	 *
	 * @return Process list
	 */
	@Secured (Roles.PROCESS_READ)
	List<Process> getProcesses();

	/**
	 * Get paged list of system processes
	 *
	 * @param pager pager
	 * @return Process list
	 */
	@Secured (Roles.PROCESS_READ)
	List<Process> getProcesses(Page<Process> pager);

	/**
	 * Get paged list of system processes filtered by state, "start from" and "end before" dates
	 *
	 * @param processSorter process sorter
	 * @param pager pager
	 * @param startFrom lower bound for process start date (if null will not be used)
	 * @param endBefore upper bound for process end date (if null will not be used)
	 * @param state state allowed by filter (if null will not be used)
	 * @param name name allowed by filter (if null will not be used)
	 * @return Process list
	 */
	@Secured (Roles.PROCESS_READ)
	List<Process> getProcesses(ProcessSorter processSorter, Page<Process> pager, Date startFrom, Date endBefore, ProcessState state, String name);

	/**
	 * Returns list which contains unique names of all the processes in the system
	 * @return list which contains unique names of all the processes in the system
	 */
	@Secured (Roles.PROCESS_READ)
	List<String> getAllProcessNames();

	List<TaskInstance> getRunningTasks();

	/**
	 * Called when process job was finished
	 *
	 * @param taskId	 Task ID
	 * @param parameters Task context parameters
	 * @param transition transition name
	 */
	void taskCompleted(Long taskId, Map<Serializable, Serializable> parameters, String transition);

	/**
	 * Retrieve process info
	 *
	 * @param processId ProcessInstance id
	 * @return Process info
	 */
	@Secured (Roles.PROCESS_READ)
	@NotNull
	Process getProcessInstanceInfo(@NotNull final Long processId);

	void setDefinitionPaths(List<String> definitionPaths);

	/**
	 * Add voters
	 *
	 * @param lyfecycleVoters Voters to set
	 */
	void setLyfecycleVoters(List<LyfecycleVoter> lyfecycleVoters);

	@Secured (Roles.PROCESS_READ)
	@Nullable
	/**
	 * Retrieve ProcessInstance
	 * @param processId ProcessInstance id
	 * @return Process info
	 */
	public ProcessInstance getProcessInstance(@NotNull final Long processInstanceId);
}
