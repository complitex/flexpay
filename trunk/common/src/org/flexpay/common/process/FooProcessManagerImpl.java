package org.flexpay.common.process;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.filter.ProcessNameFilter;
import org.flexpay.common.process.sorter.ProcessSorter;
import org.jbpm.JbpmConfiguration;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Foo process manager who doesn't do anything at all
 */
public class FooProcessManagerImpl implements ProcessManager {

	private static final FooProcessManagerImpl instance = new FooProcessManagerImpl();

	public long deployProcessDefinition(String name, boolean replace) throws ProcessDefinitionException {
		return 0;
	}

	public long deployProcessDefinition(ProcessDefinition definition, boolean replace) {
		return 0;
	}

	public long deployProcessDefinition(InputStream in, boolean replace) throws ProcessDefinitionException {
		return 0;
	}

	public void deleteProcessInstance(Process process) {
	}

	public void deleteProcessInstances(List<Process> processes) {
	}

	public void deleteProcessInstances(Set<Long> processIds) {
	}

	@Override
	public void deleteProcessInstances(DateRange range, ProcessNameFilter nameFilter) {
	}

	public void join(long processId) throws InterruptedException {
	}

	public long createProcess(@NotNull String definitionName, Map<Serializable, Serializable> parameters) {
		return 0;
	}

	public List<Process> getProcesses() {
		return null;
	}

	/**
	 * Get paged list of system processes
	 *
	 * @param pager pager
	 * @return Process list
	 */
	public List<Process> getProcesses(Page<Process> pager) {
		return null;
	}

	/**
	 * Get paged list of system processes filtered by state, "start from" and "end before" dates
	 *
	 * @param processSorter process sorter
	 * @param pager		 pager
	 * @param startFrom	 lower bound for process start date (if null will not be used)
	 * @param endBefore	 upper bound for process end date (if null will not be used)
	 * @param state		 state allowed by filter (if null will not be used)
	 * @param name		  name allowed by filter (if null will not be used)
	 * @return Process list
	 */
	public List<Process> getProcesses(ProcessSorter processSorter, Page<Process> pager, Date startFrom, Date endBefore, ProcessState state, String name) {
		return null;
	}

	/**
	 * Returns list which contains unique names of all the processes in the system
	 *
	 * @return list which contains unique names of all the processes in the system
	 */
	public List<String> getAllProcessNames() {
		return null;
	}

	public List<TaskInstance> getRunningTasks() {
		return null;
	}

	public void taskCompleted(Long taskId, Map<Serializable, Serializable> parameters, String transition) {
	}

	@NotNull
	public Process getProcessInstanceInfo(@NotNull Long processId) {
		return null;
	}

	public void setDefinitionPaths(List<String> definitionPaths) {
	}

	public void setLyfecycleVoters(List<LyfecycleVoter> lyfecycleVoters) {
	}

	public static FooProcessManagerImpl getInstance() {
		return instance;
	}

	public void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration) {
	}

	public void setRescanFrequency(String rescanFrequency) {
	}

	public void setTaskRepeatLimit(String taskRepeatLimit) {
	}

	public void start() {
	}

	public void stop() {
	}

	public ProcessInstance getProcessInstance(@NotNull Long processInstanceId) {
		return null;
	}

	public <T> T execute(@NotNull ContextCallback<T> callback) {
		return null;
	}

	public <T> T execute(@NotNull ContextCallback<T> callback, boolean useExistingContext) {
		return null;
	}
}
