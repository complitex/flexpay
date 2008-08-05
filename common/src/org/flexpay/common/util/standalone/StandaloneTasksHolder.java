package org.flexpay.common.util.standalone;

import org.apache.log4j.Logger;
import org.flexpay.common.process.ProcessManager;

import java.util.Collections;
import java.util.List;

/**
 * Holder for a list of task to run in a standalone application
 */
public class StandaloneTasksHolder {

	private Logger log = Logger.getLogger(getClass());

	private static StandaloneTasksHolder instance = new StandaloneTasksHolder();

	private StandaloneTasksHolder() {

	}

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

		if (log.isDebugEnabled()) {
			log.debug("About to execute " + standaloneTasks.size() + " tasks");
		}

		for (StandaloneTask task : standaloneTasks) {
			task.execute();
		}

		ProcessManager.unload();
	}

	public static StandaloneTasksHolder getInstance() {
		return instance;
	}
}
