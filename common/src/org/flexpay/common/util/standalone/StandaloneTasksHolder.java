package org.flexpay.common.util.standalone;

import java.util.List;
import java.util.Collections;

/**
 * Holder for a list of task to run in a standalone application
 */
public class StandaloneTasksHolder {

	private List<StandaloneTask> standaloneTasks = Collections.emptyList();

	/**
	 * setup list of tasks to execute
	 *
	 * @param standaloneTasks List of tasks
	 */
	public void setStandaloneTasks(List<StandaloneTask> standaloneTasks) {
		this.standaloneTasks = standaloneTasks;
	}

	/**
	 * Execute tasks
	 */
	public void executeTasks() {
		for (StandaloneTask task : standaloneTasks) {
			task.execute();
		}
	}
}
