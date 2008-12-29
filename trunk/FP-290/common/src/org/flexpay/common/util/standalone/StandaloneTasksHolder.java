package org.flexpay.common.util.standalone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Holder for a list of task to run in a standalone application
 */
public class StandaloneTasksHolder {

	private Logger log = LoggerFactory.getLogger(getClass());

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

		log.debug("About to execute {} tasks", standaloneTasks.size());

		for (StandaloneTask task : standaloneTasks) {
			task.execute();
		}
	}

	public static StandaloneTasksHolder getInstance() {
		return instance;
	}
}
