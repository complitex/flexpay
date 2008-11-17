package org.flexpay.common.process;

import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StopWatch;
import org.jbpm.taskmgmt.exe.TaskInstance;

import java.util.List;
import java.util.Set;

/**
 * Helper class to ease processes handling
 */
public class ProcessHelper {

	/**
	 * ProcessManager
	 */
	private ProcessManager processManager;

	/**
	 * Find processes by definition name
	 *
	 * @param definitionName Process definition name to look up
	 * @return List<Process> that has requested definition name
	 */
	@NotNull
	public List<Process> findProcesses(@NotNull String definitionName) {
		List<Process> result = CollectionUtils.list();
		List<Process> processes = processManager.getProcessList();
		for (Process process : processes) {
			if (definitionName.equals(process.getProcessDefinitionName())) {
				result.add(process);
			}
		}

		return result;
	}

	/**
	 * get running tasks with specified name
	 *
	 * @param taskName Task name to lookup
	 * @return List of task instances with specified name
	 */
	@NotNull
	public List<TaskInstance> getRunningTasks(@NotNull String taskName) {

		return getRunningTasks(CollectionUtils.set(taskName));
	}

	/**
	 * Get running tasks that have one of requested <code>taskNames</code>
	 *
	 * @param taskNames Task names to lookup
	 * @return List of task instances with specified names
	 */
	@NotNull
	public List<TaskInstance> getRunningTasks(@NotNull Set<String> taskNames) {

		List<TaskInstance> instances = processManager.getRunningTasks();
		List<TaskInstance> result = CollectionUtils.list();
		for (TaskInstance instance : instances) {
			if (!instance.hasEnded() && taskNames.contains(instance.getName())) {
				result.add(instance);
			}
		}

		return result;
	}

	/**
	 * Method setProcessManager sets the processManager of this ProcessHelper object.
	 *
	 * @param processManager the processManager of this ProcessHelper object.
	 *
	 */
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
